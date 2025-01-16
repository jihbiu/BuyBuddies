package com.pwojtowicz.buybuddies.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.auth.SignInResult
import com.pwojtowicz.buybuddies.auth.SignInState
import com.pwojtowicz.buybuddies.auth.UserData
import com.pwojtowicz.buybuddies.data.api.AuthApiService
import com.pwojtowicz.buybuddies.data.api.ShiroApiClient
import com.pwojtowicz.buybuddies.data.dto.UserDTO
import com.pwojtowicz.buybuddies.data.entity.User
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(
    private val application: BuyBuddiesApplication,
) : ViewModel() {
    private val authClient = application.authorizationClient
    private val preferencesManager = application.preferencesManager

    private val userRepository = application.userRepository
    private val groceryListRepository = application.groceryListRepository

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        checkIfSignedIn()
    }

    private fun checkIfSignedIn() {
        val currentUser = authClient.getSignedInUser()
        val isFirstInstall = preferencesManager.isFirstInstall

        _state.update { it.copy(
            isSignedIn = currentUser != null && !isFirstInstall
        )}

        Log.d(TAG, "Current user: $currentUser, First install: $isFirstInstall")
    }


    fun startSignIn(signInClick: () -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            try {
                withTimeout(30_000) {
                    signInClick()
                }
            } catch (e: Exception) {
                when (e) {
                    is TimeoutCancellationException -> handleError("Sign in timed out")
                    else -> handleError(e)
                }
            } finally {
                setLoading(false)
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            setLoading(true)
            try {
                processSignIn(result)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun processSignIn(result: SignInResult) {
        if (result.data == null) {
            throw Exception("Sign in failed: No user data")
        }

        try {
            val remoteUser = authenticateUser(result)
            userRepository.saveUser(
                User(
                    firebaseUid = remoteUser.firebaseUid,
                    username = remoteUser.name,
                    email = remoteUser.email
                )
            )
            Log.d(TAG, "Successfully saved user to local database: ${remoteUser.firebaseUid}")

            loadUserData()
            updateSuccessState(result.data)
        } catch (e: Exception) {
            Log.e(TAG, "Sign in error", e)
            throw e
        }
    }

    private suspend fun authenticateUser(result: SignInResult): UserDTO {
        val idToken = authClient.getIdToken() ?: throw Exception("Failed to get ID token")
        Log.d(TAG, "ID Token: $idToken")


        val authService = ShiroApiClient.getAuthService(idToken)
        val userData = result.data!!
        val userDTO = createUserDTO(userData)

        return authService.createOrUpdateUser(userDTO)
    }


    private fun createUserDTO(userData: UserData) = UserDTO(
        id = null,
        firebaseUid = userData.firebaseUid,
        name = userData.username ?: "",
        email = userData.email ?: ""
    )

    private suspend fun updateUser(userDTO: UserDTO, authService: AuthApiService): UserDTO {
        Log.d(TAG, "Starting user update for UID: ${userDTO.firebaseUid}")
        return try {
            Log.d(TAG, "User data being sent: $userDTO")
            val updatedUser = authService.updateUserData(userDTO)
            Log.d(TAG, "Update successful, received: $updatedUser")
            updatedUser
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user data: ${e.message}", e)
            when (e) {
                is HttpException -> {
                    Log.e(TAG, "HTTP Error code: ${e.code()}")
                    Log.e(TAG, "HTTP Error message: ${e.message()}")
                }
                is IOException -> {
                    Log.e(TAG, "Network Error: ${e.message}")
                }
            }
            userDTO
        }
    }

    private suspend fun loadUserData() {
        try {
            groceryListRepository.fetchUserLists()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading user data", e)
        }
    }


    fun signOut() {
        viewModelScope.launch {
            try {
                authClient.signOut()
                resetState()
            } catch (e: Exception) {
                _state.update {
                    it.copy(signInError = e.message)
                }
                Log.e(TAG, "Error during sign out, ${state.value.signInError}", e)
            }
        }
    }

    private fun updateSuccessState(userData: UserData) {
        preferencesManager.isFirstInstall = false
        _state.update {
            it.copy(
                isSignInSuccessful = true,
                signInError = null,
                isSignedIn = true
            )
        }
    }

    private fun handleError(error: String) {
        Log.e(TAG, "Auth error: $error")
        _state.update {
            it.copy(
                isSignInSuccessful = false,
                signInError = error,
                isSignedIn = false
            )
        }
    }

    private fun handleError(exception: Throwable) {
        handleError(exception.message ?: "Unknown error occurred")
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    fun resetState() {
        _state.update {
            SignInState(
                isSignInSuccessful = false,
                isLoading = false,
                isSignedIn = authClient.getSignedInUser() != null,
                signInError = null
            )
        }
    }

    fun resetLoadingState() {
        _state.update {
            it.copy(
                isLoading = false,
                signInError = null
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(signInError = null) }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}

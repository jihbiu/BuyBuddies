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
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AuthViewModel(
    private val application: BuyBuddiesApplication,
) : ViewModel() {

    private val authClient = application.authorizationClient
    private val userRepository = application.userRepository
    private val groceryListRepository = application.groceryListRepository

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        checkIfSignedIn()
    }

    private fun checkIfSignedIn() {
        val currentUser = authClient.getSignedInUser()
        _state.update { it.copy(isSignedIn = currentUser != null) }
        Log.d(TAG, "Current user: $currentUser")
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
            val user = authenticateUser(result)
            loadUserData(user.firebaseUid)
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

        return if (result.isNewUser) {
            createNewUser(userData, authService)
        } else {
            getOrCreateExistingUser(userData, authService)
        }
    }

    private suspend fun createNewUser(userData: UserData, authService: AuthApiService): UserDTO {
        val userDTO = createUserDTO(userData)
        Log.d(TAG, "Creating new user with data: $userDTO")
        return authService.createUser(userDTO)
    }

    private fun createUserDTO(userData: UserData) = UserDTO(
        id = null,
        firebaseUid = userData.firebaseUid,
        name = userData.username ?: "",
        email = userData.email ?: ""
    )

    private suspend fun getOrCreateExistingUser(userData: UserData, authService: AuthApiService): UserDTO {
        return try {
            Log.d(TAG, "Getting existing user with firebaseUid: ${userData.firebaseUid}")
            authService.getUserByFirebaseId(userData.firebaseUid)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user data", e)
            createNewUser(userData, authService)
        }
    }

    private suspend fun loadUserData(firebaseUid: String) {
        try {
            groceryListRepository.fetchUserLists(firebaseUid)
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

    companion object {
        private const val TAG = "AuthViewModel"
    }
}

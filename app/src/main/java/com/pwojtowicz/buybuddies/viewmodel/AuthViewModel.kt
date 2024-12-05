package com.pwojtowicz.buybuddies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.auth.SignInResult
import com.pwojtowicz.buybuddies.auth.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authorizationClient: AuthorizationClient) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        checkSignInStatus()
    }

    private fun checkSignInStatus() {
        _state.update { it.copy(
            isSignedIn = authorizationClient.isUserSignedIn(),
            userData = authorizationClient.getSignedInUser()
        ) }
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            when (result) {
                is SignInResult.Success -> it.copy(
                    isSignInSuccessful = true,
                    signInError = null,
                    isSignedIn = true,
                    userData = result.userData
                )
                is SignInResult.Error -> it.copy(
                    isSignInSuccessful = false,
                    signInError = result.message,
                    isSignedIn = false
                )
            }
        }
    }

    fun startSignIn(signInAction: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, signInError = null) }
                signInAction()
            } catch (e: Exception) {
                _state.update { it.copy(signInError = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                authorizationClient.signOut()
                _state.update {
                    it.copy(
                        isSignedIn = false,
                        isSignInSuccessful = false,
                        userData = null,
                        signInError = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(signInError = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetState() {
        _state.update { it.copy(
            isSignInSuccessful = false,
            signInError = null
        ) }
    }
}
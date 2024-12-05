package com.pwojtowicz.buybuddies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pwojtowicz.buybuddies.auth.AuthorizationClient

class AuthViewModelFactory(private val authorizationClient: AuthorizationClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authorizationClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
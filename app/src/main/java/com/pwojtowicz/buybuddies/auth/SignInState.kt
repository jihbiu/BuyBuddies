package com.pwojtowicz.buybuddies.auth

data class SignInState(
    val isLoading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val isSignedIn: Boolean = false,
    val userData: UserData? = null
)
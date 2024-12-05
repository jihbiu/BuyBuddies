package com.pwojtowicz.buybuddies.auth

sealed class SignInResult {
    data class Success(val userData: UserData) : SignInResult()
    data class Error(val message: String) : SignInResult()
}
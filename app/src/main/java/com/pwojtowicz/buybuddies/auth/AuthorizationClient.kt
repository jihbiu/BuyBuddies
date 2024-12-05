package com.pwojtowicz.buybuddies.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.pwojtowicz.buybuddies.R
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthorizationClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun getSignedInUser(): UserData?{
        return auth.currentUser?.let { user ->
            UserData(
                userId = user.uid,
                username = user.displayName,
                profilePictureUrl = user.photoUrl?.toString()
            )
        }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult.Success(
                UserData(
                    userId = user?.uid ?: "",
                    username = user?.displayName,
                    profilePictureUrl = user?.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign in with intent: ${e.message}")
            if (e is CancellationException) throw e
            SignInResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign in: ${e.message}")
            if (e is CancellationException) throw e
            null
        }

        // Try Sign-Up
        return result?.pendingIntent?.intentSender ?: run {
            try {
                oneTapClient.beginSignIn(buildSignUpRequest()).await().pendingIntent?.intentSender
            } catch (e: Exception) {
                Log.e(TAG, "Error during sign in: ${e.message}")
                if (e is CancellationException) throw e
                null
            }
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign out: ${e.message}")
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun buildSignUpRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    companion object {
        private const val TAG = "AuthorizationClient"
    }
}
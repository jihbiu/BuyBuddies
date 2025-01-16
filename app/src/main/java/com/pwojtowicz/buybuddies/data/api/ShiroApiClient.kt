package com.pwojtowicz.buybuddies.data.api

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShiroApiClient {
    const val BASE_URL = "......"
    private const val TAG = "ShiroApiClient"

    private fun createAuthenticatedClient(token: String): OkHttpClient {
        Log.d(TAG, "Creating authenticated client")
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()

                Log.d(TAG, "Making request:")
                Log.d(TAG, "URL: ${request.url}")
                Log.d(TAG, "Method: ${request.method}")
                Log.d(TAG, "Headers: ${request.headers}")

                try {
                    val response = chain.proceed(request)
                    Log.d(TAG, "Response code: ${response.code}")
                    Log.d(TAG, "Response message: ${response.message}")
                    response
                } catch (e: Exception) {
                    Log.e(TAG, "Request failed", e)
                    throw e
                }
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    fun createAuthenticatedRetrofit(token: String): Retrofit {
        Log.d(TAG, "Creating Retrofit with base URL: $BASE_URL")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createAuthenticatedClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(token: String): AuthApiService {6
        Log.d(TAG, "Getting AuthService")
        return createAuthenticatedRetrofit(token).create(AuthApiService::class.java)
    }

    fun getGroceryListService(token: String): GroceryListApiService {
        Log.d(TAG, "Getting GroceryListService")
        return createAuthenticatedRetrofit(token).create(GroceryListApiService::class.java)
    }
}
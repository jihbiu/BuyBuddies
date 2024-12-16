package com.pwojtowicz.buybuddies.data.api

import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShiroApiClient {
    const val BASE_URL = ""
//    const val BASE_URL =


    private fun createAuthenticatedClient(token: String) = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    fun createAuthenticatedRetrofit(token: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createAuthenticatedClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(token: String): AuthApiService =
        createAuthenticatedRetrofit(token).create(AuthApiService::class.java)

    fun getGroceryListService(token: String): GroceryListApiService =
        createAuthenticatedRetrofit(token).create(GroceryListApiService::class.java)
}
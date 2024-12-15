package com.pwojtowicz.buybuddies.data.api

import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShiroApiClient {
    const val BASE_URL = "http://192.168.0.108:8080/"

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

}
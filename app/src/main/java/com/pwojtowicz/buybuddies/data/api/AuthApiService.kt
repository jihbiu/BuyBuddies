package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @POST("api/users")
    suspend fun createUser(@Body userDTO: UserDTO): UserDTO

    @GET("api/users/firebase/{firebaseUid}")  // Updated endpoint
    suspend fun getUserByFirebaseId(@Path("firebaseUid") firebaseId: String): UserDTO
}
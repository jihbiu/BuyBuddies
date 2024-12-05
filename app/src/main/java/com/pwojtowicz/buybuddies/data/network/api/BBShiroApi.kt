package com.pwojtowicz.buybuddies.data.network.api

import android.net.Network
import com.pwojtowicz.buybuddies.data.network.model.NetworkUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BBShiroApi {
    @POST("users")
    suspend fun createUser(@Body user: NetworkUser): Response<NetworkUser>

}
package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroceryListApiService {
    @GET("api/grocery-lists/user/{firebaseUid}")
    suspend fun getGroceryListsByUser(@Path("firebaseUid") firebaseUid: String): List<GroceryListDTO>

    @POST("api/grocery-lists")
    suspend fun createGroceryList(@Body groceryListDTO: GroceryListDTO): GroceryListDTO

    @PUT("api/grocery-lists/{id}")
    suspend fun updateGroceryList(@Path("id") id: Long, @Body groceryListDTO: GroceryListDTO): GroceryListDTO

    @DELETE("api/grocery-lists")
    suspend fun deleteGroceryList(@Body groceryList: GroceryListDTO)
}
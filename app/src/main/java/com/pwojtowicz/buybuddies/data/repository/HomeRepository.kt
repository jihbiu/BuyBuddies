package com.pwojtowicz.buybuddies.data.repository

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.api.HomeApiService
import com.pwojtowicz.buybuddies.data.api.ShiroApiClient
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import com.pwojtowicz.buybuddies.data.entity.Home
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val application: BuyBuddiesApplication
//    private val homeApi: HomeApiService = ShiroRetrofitClient.homeService,
//    private val homeDao: HomeDao,
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val homeDao = buyBuddiesDB.homeDao()

    suspend fun insertHome(home: Home): Long {
        try {
            return homeDao.insert(home)
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error inserting Home", e)
            throw e
        }
    }

    suspend fun deleteAll() {
        try {
            homeDao.deleteAll()
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error deleting all homes", e)
            throw e
        }
    }

    fun getHomeById(id: Long): Flow<Home> {
        return homeDao.getById(id)
    }

    fun getAllHomes(): Flow<List<Home>> {
        return homeDao.getAll()
    }
//    private suspend fun getAuthToken(): String? {
//        return auth.currentUser?.getIdToken(false)?.await()?.token
//    }
//
//    suspend fun refreshHomes() {
//        try{
//            val userId = auth.currentUser?.uid ?: return
//            val token = getAuthToken() ?: throw Exception("Not authenticated")
//
//            //Fetch data
//            val ownedHomes = homeApi.getHomesByOwner(
//                ownerId = userId,
//                authHeader = "Bearer $token"
//            )
//
//            val memberHomes = homeApi.getHomesByMember(
//                userId = userId,
//                authHeader = "Bearer $token"
//            )
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
}
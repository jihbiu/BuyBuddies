package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.api.ShiroApiClient
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GroceryListRepository(
    private val application: BuyBuddiesApplication
) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val groceryListDao = buyBuddiesDB.groceryListDao()

    fun getGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAll()


    private suspend fun getService(): GroceryListApiService {
        val token = application.authorizationClient.getIdToken()
            ?: throw Exception("Failed to get ID token")
        return ShiroApiClient.getGroceryListService(token)
    }

    suspend fun fetchUserLists(firebaseUid: String) {
        try {
            val remoteLists = getService().getGroceryListsByUser(firebaseUid)
            val entities = remoteLists.map { dto ->
                GroceryList(
                    ownerId = firebaseUid,
                    name = dto.name,
                    description = dto.description,
                    listStatus = GroceryListStatus.ACTIVE.name,
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis().toString()
                )
            }
            groceryListDao.insertAll(entities)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery lists", e)
            throw e
        }
    }

    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAll()

    fun getAllGroceryListDTOs(): Flow<List<GroceryListDTO>> = groceryListDao.getAll()
        .map { entities ->
            entities.map { entity ->
                GroceryListDTO(
                    name = entity.name,
                    description = entity.description,
                    ownerId = entity.ownerId,
                    status = entity.listStatus
                )
            }
        }

    suspend fun insertGroceryList(groceryList: GroceryList): Long {
        try {
            if (groceryList.name.isBlank()) {
                throw IllegalArgumentException("List name cannot be empty")
            }

            if (groceryListDao.exists(groceryList.name, groceryList.ownerId)) {
                throw IllegalArgumentException("A list with this name already exists")
            }

            val token = application.authorizationClient.getIdToken() ?: return -1
            val groceryListDTO = GroceryListDTO(
                name = groceryList.name,
                description = groceryList.description,
                ownerId = groceryList.ownerId,
                status = groceryList.listStatus
            )

            val service = ShiroApiClient.getGroceryListService(token)
            service.createGroceryList(groceryListDTO)

            return groceryListDao.insert(groceryList)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list", e)
            throw e
        }
    }

    suspend fun deleteGroceryList(groceryListId: Long) {
        try {
            val groceryList = groceryListDao.getById(groceryListId)
                ?: throw IllegalStateException("List not found")

            val currentUser = application.authorizationClient.getSignedInUser()
                ?: throw IllegalStateException("No user signed in")

            //Remote deletion
            val token = application.authorizationClient.getIdToken()
                ?: throw Exception("Failed to get ID token")

            val service = ShiroApiClient.getGroceryListService(token)
            val listDTO = GroceryListDTO(
                name = groceryList.name,
                description = groceryList.description,
                ownerId = groceryList.ownerId,
                status = groceryList.listStatus
            )
            service.deleteGroceryList(listDTO)

            //Local deletion
            groceryListDao.deleteById(groceryListId)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery list", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "GroceryListRepository"
    }
}
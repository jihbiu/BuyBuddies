package com.pwojtowicz.buybuddies.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.api.ShiroApiClient
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class GroceryListRepository(
    private val application: BuyBuddiesApplication
) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val groceryListDao = buyBuddiesDB.groceryListDao()

    private suspend fun getService(): GroceryListApiService {
        Log.d(TAG, "Getting grocery list service")
        val token = application.authorizationClient.getIdToken()
            ?: throw Exception("Failed to get ID token")
        return ShiroApiClient.getGroceryListService(token)
    }

    suspend fun fetchUserLists() {
        Log.i(TAG, "Fetching user's grocery lists")
        try {
            val remoteLists = getService().getMyLists()
            Log.d(TAG, "Received ${remoteLists.size} lists from remote")

            val entities = remoteLists.map { dto ->
                GroceryList(
                    name = dto.name,
                    description = dto.description,
                    ownerId = dto.ownerId,
                    listStatus = dto.status ?: GroceryListStatus.ACTIVE.name,
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis().toString()
                )
            }
            groceryListDao.insertAll(entities)
            Log.i(TAG, "Successfully saved ${entities.size} lists to local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery lists", e)
            throw handleApiError(e)
        }
    }

    suspend fun createGroceryList(groceryList: GroceryList): Long {
        Log.i(TAG, "Creating new grocery list: ${groceryList.name}")
        try {
            validateGroceryList(groceryList)?.let { throw it }

            val dto = GroceryListDTO(
                name = groceryList.name,
                description = groceryList.description,
                ownerId = groceryList.ownerId,
                homeId = groceryList.homeId,
                status = groceryList.listStatus
            )

            // Create on remote
            val createdList = getService().createGroceryList(dto)
            Log.d(TAG, "Successfully created list on remote with ID: ${createdList.id}")

            // Save to local DB
            return groceryListDao.insert(groceryList.copy(id = createdList.id))
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list", e)
            throw handleApiError(e)
        }
    }

    suspend fun addMember(listId: Long, memberFirebaseUid: String) {
        Log.i(TAG, "Adding member $memberFirebaseUid to list $listId")
        try {
            val updatedList = getService().addMember(listId, memberFirebaseUid)
            groceryListDao.getById(listId)?.let { localList ->
                groceryListDao.update(localList)
                Log.i(TAG, "Successfully updated local list with new member")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding member to list", e)
            throw handleApiError(e)
        }
    }

    suspend fun removeMember(listId: Long, memberFirebaseUid: String) {
        Log.i(TAG, "Removing member $memberFirebaseUid from list $listId")
        try {
            val updatedList = getService().removeMember(listId, memberFirebaseUid)
            groceryListDao.getById(listId)?.let { localList ->
                groceryListDao.update(localList)
                Log.i(TAG, "Successfully updated local list after member removal")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removing member from list", e)
            throw handleApiError(e)
        }
    }



    suspend fun deleteGroceryList(listId: Long) {
        Log.i(TAG, "Deleting grocery list: $listId")
        try {
            getService().deleteGroceryList(listId)
            Log.d(TAG, "Successfully deleted list from remote")
            groceryListDao.deleteById(listId)
            Log.i(TAG, "Successfully deleted list from local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery list", e)
            throw handleApiError(e)
        }
    }

    private suspend fun validateGroceryList(groceryList: GroceryList): Throwable? {
        return when {
            groceryList.name.isBlank() -> {
                Log.w(TAG, "Attempted to create list with empty name")
                IllegalArgumentException("List name cannot be empty")
            }
            groceryListDao.exists(groceryList.name, groceryList.ownerId) -> {
                Log.w(TAG, "List with name '${groceryList.name}' already exists for user ${groceryList.ownerId}")
                IllegalArgumentException("A list with this name already exists")
            }
            else -> null
        }
    }


    private fun handleApiError(e: Exception): Throwable {
        return when (e) {
            is HttpException -> when (e.code()) {
                401 -> IllegalStateException("Authentication failed - please log in again")
                403 -> IllegalStateException("Not authorized for this operation")
                404 -> IllegalStateException("List not found")
                else -> IllegalStateException("Server error: ${e.message}")
            }
            else -> e
        }
    }

    fun getLocalGroceryLists(): Flow<List<GroceryList>> {
        Log.d(TAG, "Getting grocery lists from local DB")
        return groceryListDao.getAll()
    }

    fun getListsForCurrentUser(userId: String): Flow<List<GroceryList>> {
        Log.d(TAG, "Getting grocery lists from local DB for user: $userId")
        return groceryListDao.getListsForUser(userId)
    }

    companion object {
        private const val TAG = "GroceryListRepository"
    }
}
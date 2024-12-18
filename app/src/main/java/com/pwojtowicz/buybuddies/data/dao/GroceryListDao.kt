package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocery_lists")
    fun getAll(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE homeId = :homeId")
    fun getByHomeId(homeId: Long): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :firebaseUid")
    fun getByOwnerId(firebaseUid: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists ORDER BY sortOrder ASC")
    fun getAllGroceryListsSorted(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE id = :id")
    suspend fun getById(id: Long): GroceryList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryList: GroceryList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groceryLists: List<GroceryList>)

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_lists WHERE name = :name AND ownerId = :ownerId)")
    suspend fun exists(name: String, ownerId: String?): Boolean

    @Update
    suspend fun update(groceryList: GroceryList)

    @Update
    suspend fun updateGroceryLists(groceryLists: List<GroceryList>)

    @Delete
    suspend fun delete(groceryList: GroceryList)

    @Query("DELETE FROM grocery_lists WHERE ownerId = :firebaseUid")
    suspend fun deleteAllByOwnerId(firebaseUid: String)

    @Query("DELETE FROM grocery_lists")
    suspend fun deleteAll()

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :firebaseUid ORDER BY updatedAt DESC")
    suspend fun getLatestByOwnerId(firebaseUid: String): List<GroceryList>

    @Query("DELETE FROM grocery_lists WHERE id = :groceryListId")
    suspend fun deleteById(groceryListId: Long)
}

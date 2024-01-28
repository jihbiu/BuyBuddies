package com.pwojtowicz.buybuddies.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryItemDao {
    @Query("SELECT * FROM grocery_item")
    fun getAll(): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_item WHERE listId = :listId")
    fun getAllByListId(listId: Int): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_item WHERE id = :id")
    fun getById(id: Int): Flow<GroceryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryItem: GroceryItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryItems: List<GroceryItem>)

    @Update
    suspend fun update(groceryItem: GroceryItem)

    @Delete
    suspend fun delete(groceryItem: GroceryItem)

    @Query("DELETE FROM grocery_item")
    suspend fun deleteAll()
}

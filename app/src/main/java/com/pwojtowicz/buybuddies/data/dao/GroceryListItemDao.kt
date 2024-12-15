package com.pwojtowicz.buybuddies.data.dao

import androidx.room.*
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryListItemDao {
    @Query("SELECT * FROM grocery_items")
    fun getAll(): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE listId = :listId")
    fun getByListId(listId: Long): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE id = :id")
    fun getById(id: Long): Flow<GroceryListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryListItem: GroceryListItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryListItems: List<GroceryListItem>)

    @Update
    suspend fun update(groceryListItem: GroceryListItem)

    @Delete
    suspend fun delete(groceryListItem: GroceryListItem)

    @Query("DELETE FROM grocery_items")
    suspend fun deleteAll()
}

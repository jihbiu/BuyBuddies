package com.pwojtowicz.buybuddies.data.dao.groceryitem

import androidx.room.*
import com.pwojtowicz.buybuddies.data.model.groceryitem.GroceryItem
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryItemDao {
    @Query("SELECT * FROM grocery_items")
    fun getAll(): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_items WHERE listId = :listId")
    fun getAllByListId(listId: Long): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_items WHERE id = :id")
    fun getById(id: Long): Flow<GroceryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryItem: GroceryItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryItems: List<GroceryItem>)

    @Update
    suspend fun update(groceryItem: GroceryItem)

    @Delete
    suspend fun delete(groceryItem: GroceryItem)

    @Query("DELETE FROM grocery_items")
    suspend fun deleteAll()
}

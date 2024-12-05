package com.pwojtowicz.buybuddies.data.dao.grocerylist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListImage
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListImageDao {
    @Query("SELECT * FROM grocery_list_images")
    fun getAll(): Flow<List<GroceryListImage>>

    @Query("SELECT * FROM grocery_lists")
    suspend fun getAllList(): List<GroceryListImage>

    @Query("SELECT * FROM grocery_list_images WHERE id = :id")
    fun getById(id: Long): Flow<GroceryListImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryListImage: GroceryListImage)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryListImages: List<GroceryListImage>)

    @Update
    suspend fun update(groceryListImage: GroceryListImage)

    @Delete
    suspend fun delete(groceryListImage: GroceryListImage)

    @Query("DELETE FROM grocery_list_images")
    suspend fun deleteAll()
}

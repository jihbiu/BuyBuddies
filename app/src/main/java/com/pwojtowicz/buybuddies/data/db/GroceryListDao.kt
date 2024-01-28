package com.pwojtowicz.buybuddies.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocery_lists")
    fun getAll(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists")
    suspend fun getAllList(): List<GroceryList>

    @Query("SELECT * FROM grocery_lists WHERE id = :id")
    fun getById(id: Int): Flow<GroceryList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryList: GroceryList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryList: List<GroceryList>)

    @Update
    suspend fun update(groceryList: GroceryList)

    @Delete
    suspend fun delete(groceryList: GroceryList)

    @Query("DELETE FROM grocery_lists")
    suspend fun deleteAll()
}

package com.pwojtowicz.buybuddies.data.dao.grocerylist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocery_lists")
    fun getAll(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE id IN (:listIds)")
    fun getGroceryListsByIds(listIds: List<Long>): Flow<List<GroceryList>>

    //@Query("SELECT * FROM grocery_lists WHERE labelId = :labelId")
    //fun getGroceryListsByLabelId(labelId: Long?): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists ORDER BY sortOrder ASC")
    fun getAllGroceryListsSorted(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE id = :id")
    fun getById(id: Long): Flow<GroceryList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryList: GroceryList): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryList: List<GroceryList>)

    @Update
    suspend fun update(groceryList: GroceryList)

    @Update
    suspend fun updateGroceryLists(groceryLists: List<GroceryList>)

    @Delete
    suspend fun delete(groceryList: GroceryList)

    @Query("DELETE FROM grocery_lists")
    suspend fun deleteAll()
}

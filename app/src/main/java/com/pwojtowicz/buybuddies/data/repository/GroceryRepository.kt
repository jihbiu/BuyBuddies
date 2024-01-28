package com.pwojtowicz.buybuddies.data.repository

import android.app.Application
import androidx.compose.runtime.MutableState
import com.pwojtowicz.buybuddies.data.db.GroceryDatabase
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class GroceryRepository(private val application: Application) {
    private val groceryDB: GroceryDatabase = GroceryDatabase.getInstance(application)
    private val groceryListDao = groceryDB.groceryListDao()
    private val groceryItemDao = groceryDB.groceryItemDao()

    // ### Grocery List Operations ###
    fun getAllGroceryLists(): Flow<List<GroceryList>> { return groceryListDao.getAll() }
    suspend fun getGroceryListById(id: Int): Flow<GroceryList>{ return groceryListDao.getById(id) }
    suspend fun insertGroceryList(groceryList: GroceryList){ groceryListDao.insert(groceryList) }
    suspend fun insertAllGroceryLists(groceryLists: List<GroceryList>): List<GroceryList>{
        groceryListDao.insertAll(groceryLists)
        return groceryListDao.getAllList()
    }
    suspend fun updateGroceryList(groceryList: GroceryList){ groceryListDao.update(groceryList) }
    suspend fun deleteGroceryList(groceryList: GroceryList){ groceryListDao.delete(groceryList) }
    suspend fun deleteAllGroceryLists() { groceryListDao.deleteAll() }

    // ### Grocery Item Operations ###
    fun getAllGroceryItems(): Flow<List<GroceryItem>> { return groceryItemDao.getAll() }
    fun getAllGroceryItemsByListId(listId: Int): Flow<List<GroceryItem>> {return groceryItemDao.getAllByListId(listId)}
    fun getGroceryItemById(id: Int): Flow<GroceryItem> { return groceryItemDao.getById(id) }
    suspend fun updateGroceryItem(groceryItem: GroceryItem) { groceryItemDao.update(groceryItem) }
    suspend fun insertGroceryItem(groceryItem: GroceryItem) { groceryItemDao.insert(groceryItem) }
    suspend fun insertAllGroceryItems(groceryItems: List<GroceryItem>) { groceryItemDao.insertAll(groceryItems) }
    suspend fun deleteGroceryItem(groceryItem: GroceryItem) { groceryItemDao.delete(groceryItem) }
    suspend fun deleteAllGroceryItems() { groceryItemDao.deleteAll() }
}

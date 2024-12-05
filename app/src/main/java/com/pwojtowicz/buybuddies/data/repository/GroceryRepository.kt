package com.pwojtowicz.buybuddies.data.repository

import android.app.Application
import android.util.Log
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import com.pwojtowicz.buybuddies.data.model.groceryitem.GroceryItem
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListLabel
import kotlinx.coroutines.flow.Flow


class GroceryRepository(private val application: Application) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val groceryListDao = buyBuddiesDB.groceryListDao()
    private val groceryItemDao = buyBuddiesDB.groceryItemDao()
    private val groceryListLabelDao = buyBuddiesDB.groceryListLabelDao()
    private val depotDao = buyBuddiesDB.depotDao()
    private val userDao = buyBuddiesDB.userDao()

    // ### Grocery List Operations ###
    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryListsSorted()
    //fun getGroceryListsByLabelId(labelId: Long?): Flow<List<GroceryList>> { return groceryListDao.getGroceryListsByLabelId(labelId) }
    suspend fun insertGroceryList(groceryList: GroceryList): Long {
        try {
            return groceryListDao.insert(groceryList)
        } catch (e: Exception) {
            Log.e("GroceryRepository", "Error inserting GroceryList", e)
            throw e
        }
    }
    suspend fun deleteGroceryList(groceryList: GroceryList){ groceryListDao.delete(groceryList) }

    // ### Grocery Item Operations ###
    fun getAllGroceryItems(): Flow<List<GroceryItem>> { return groceryItemDao.getAll() }
    fun getAllGroceryItemsByListId(listId: Long): Flow<List<GroceryItem>> {return groceryItemDao.getAllByListId(listId)}
    fun getGroceryItemById(id: Long): Flow<GroceryItem> { return groceryItemDao.getById(id) }
    suspend fun updateGroceryItem(groceryItem: GroceryItem) { groceryItemDao.update(groceryItem) }
    suspend fun insertGroceryItem(groceryItem: GroceryItem) { groceryItemDao.insert(groceryItem) }
    suspend fun insertAllGroceryItems(groceryItems: List<GroceryItem>) { groceryItemDao.insertAll(groceryItems) }
    suspend fun deleteGroceryItem(groceryItem: GroceryItem) { groceryItemDao.delete(groceryItem) }
    suspend fun deleteAllGroceryItems() { groceryItemDao.deleteAll() }


    // ### GroceryListLabel Operations ###
    fun getAllGroceryListLabels(): Flow<List<GroceryListLabel>> {
        return groceryListLabelDao.getAll()
    }

    suspend fun getLabelById(id: Long): Flow<GroceryListLabel> {
        return groceryListLabelDao.getById(id)
    }
    fun getLabelsForList(listId: Long): Flow<List<GroceryListLabel>> {
        return groceryListLabelDao.getLabelsForList(listId)
    }
}

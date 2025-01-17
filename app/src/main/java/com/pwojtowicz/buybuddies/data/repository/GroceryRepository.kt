package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GroceryRepository @Inject constructor (
    private val groceryListDao: GroceryListDao,
    private val groceryItemDao: GroceryListItemDao,
    private val groceryListLabelDao: GroceryListLabelDao
) {

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
    fun getAllGroceryItemsByListId(listId: Long): Flow<List<GroceryListItem>> {return groceryItemDao.getByListId(listId)}
    suspend fun updateGroceryItem(groceryListItem: GroceryListItem) { groceryItemDao.update(groceryListItem) }
    suspend fun insertGroceryItem(groceryListItem: GroceryListItem) { groceryItemDao.insert(groceryListItem) }
    suspend fun deleteGroceryItem(groceryListItem: GroceryListItem) { groceryItemDao.delete(groceryListItem) }
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

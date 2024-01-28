package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.data.DataProvider
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
import com.pwojtowicz.buybuddies.data.model.GroceryListStatus
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroceryViewModel(application: Application): ViewModel(){

   private val repository = GroceryRepository(application)

    val groceryListsState: StateFlow<List<GroceryList>> = repository.getAllGroceryLists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _activeGroceryList = MutableStateFlow<GroceryList?>(null)
    val activeGroceryList: StateFlow<GroceryList?> = _activeGroceryList

    var groceryItems: Flow<List<GroceryItem>> = activeGroceryList
        .filterNotNull()
        .flatMapLatest { activeList  ->
            repository.getAllGroceryItemsByListId(activeList.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            /*
            repository.deleteAllGroceryLists()
            repository.deleteAllGroceryItems()


            val insertedLists = repository.insertAllGroceryLists(DataProvider.groceryLists)

            val testGroceryItems = DataProvider.generateRandomGroceryItems(insertedLists)
            repository.insertAllGroceryItems(testGroceryItems)
            */
        }
    }

    fun selectList(list: GroceryList) {
        viewModelScope.launch {
            _activeGroceryList.value = repository.getGroceryListById(list.id).firstOrNull()
        }
    }

    suspend fun getActiveList(): GroceryList? {
        return _activeGroceryList.value
    }

    fun getAllGroceryLists(): List<GroceryList> {
        return groceryListsState.value
    }

    fun addGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            repository.insertGroceryList(groceryList)
        }
    }

    fun updateGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            repository.updateGroceryList(groceryList)
            _activeGroceryList.value = groceryList
        }
    }

    fun deleteGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            repository.deleteGroceryList(groceryList)
        }
    }


    // Grocery Items
    fun addGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch {
            repository.insertGroceryItem(groceryItem)
        }
    }

    fun addGroceryItemByList(groceryItem: GroceryItem, groceryList: GroceryList) {
        val newGroceryItem = GroceryItem(
            id = groceryItem.id,
            listId = groceryList.id,
            name = groceryItem.name,
            count = groceryItem.count,
            isChecked = groceryItem.isChecked
        )
        viewModelScope.launch {
            repository.insertGroceryItem(newGroceryItem)
        }
    }

    fun updateGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch {
            repository.updateGroceryItem(groceryItem)
        }
    }

    fun deleteGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch {
            repository.deleteGroceryItem(groceryItem)
        }
    }
}

package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.data.model.groceryitem.GroceryItem
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroceryViewModel(application: Application): ViewModel() {
   private val repository = GroceryRepository(application)

   private val _activeGroceryListId = MutableStateFlow<Long?>(null)
   val activeGroceryListId: StateFlow<Long?> = _activeGroceryListId

   val groceryItemsList: StateFlow<List<GroceryItem>> = _activeGroceryListId.flatMapLatest { listId ->
      if (listId != null) {
         repository.getAllGroceryItemsByListId(listId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
         )
      } else {
         MutableStateFlow(emptyList<GroceryItem>()).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
         )
      }
   }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), emptyList())

   fun setActiveGroceryListId(listId: Long) {
      _activeGroceryListId.value = listId
   }

   fun createGroceryItem(listId: Long, depotId: Long?, name: String, quantity: Int = 0) {
      viewModelScope.launch {
         val newGroceryItem = GroceryItem(
            listId = listId,
            depotId = depotId,
            name = name,
            quantity = quantity
         )
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

   fun toggleGroceryItemChecked(groceryItem: GroceryItem) {
      val updatedItem = groceryItem.copy(isChecked = !groceryItem.isChecked)
      updateGroceryItem(updatedItem)
   }
}
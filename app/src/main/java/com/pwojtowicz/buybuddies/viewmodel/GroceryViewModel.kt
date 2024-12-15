package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
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

   val groceryListItemsList: StateFlow<List<GroceryListItem>> = _activeGroceryListId.flatMapLatest { listId ->
      if (listId != null) {
         repository.getAllGroceryItemsByListId(listId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
         )
      } else {
         MutableStateFlow(emptyList<GroceryListItem>()).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
         )
      }
   }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), emptyList())

   fun setActiveGroceryListId(listId: Long) {
      _activeGroceryListId.value = listId
   }

   fun createGroceryItem(listId: Long, name: String, quantity: Double = 0.0) {
      viewModelScope.launch {
         val newGroceryListItem = GroceryListItem(
            listId = listId,
            name = name,
            quantity = quantity
         )
         repository.insertGroceryItem(newGroceryListItem)
      }
   }

   fun updateGroceryItem(groceryListItem: GroceryListItem) {
      viewModelScope.launch {
         repository.updateGroceryItem(groceryListItem)
      }
   }

   fun deleteGroceryItem(groceryListItem: GroceryListItem) {
      viewModelScope.launch {
         repository.deleteGroceryItem(groceryListItem)
      }
   }

   fun toggleGroceryItemChecked(groceryListItem: GroceryListItem) {
      val updatedItem = groceryListItem.copy(isChecked = !groceryListItem.isChecked)
      updateGroceryItem(updatedItem)
   }
}
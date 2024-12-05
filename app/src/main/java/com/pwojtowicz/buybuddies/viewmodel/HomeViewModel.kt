package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.data.DataProvider.generateTestGroceryLists
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListLabel
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListStatus
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): ViewModel() {
    private val groceryRepository = GroceryRepository(application)

    val groceryLists = groceryRepository.getAllGroceryLists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 10L),
        emptyList()
    )

    val groceryListLabels = groceryRepository.getAllGroceryListLabels().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 10L),
        emptyList()
    )
    private var _searchText = MutableStateFlow("")
    private var _selectedStatus = MutableStateFlow<GroceryListStatus?>(null)
    private var _selectedLabel = MutableStateFlow<GroceryListLabel?>(null)
    val filteredGroceryLists: StateFlow<List<GroceryList>> = combine(
        groceryLists,
        _searchText,
        _selectedStatus,
        _selectedLabel
    ) { lists, search, status, label ->
        lists.filter { groceryList ->
            (search.isEmpty() || groceryList.name.contains(search, ignoreCase = true)) &&
                    (status == null || groceryList.listStatus == status.name) &&
                    (label == null || groceryRepository.getLabelsForList(groceryList.id).first().contains(label))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 10L),
        emptyList()
    )
    private val _showCard = MutableStateFlow(false)
    val showCard: MutableStateFlow<Boolean> = _showCard

    private val _showMenuSheet = MutableStateFlow(false)
    val showMenuSheet: MutableStateFlow<Boolean> = _showMenuSheet

    private val _newListId = MutableStateFlow<Long?>(null)
    val newListId: MutableStateFlow<Long?> = _newListId

    init {
        viewModelScope.launch {
            //TEST DATA

            groceryRepository.deleteAllGroceryItems()
            val testGroceryLists = generateTestGroceryLists()

            // Insert into database
            testGroceryLists.forEach { groceryList ->
                groceryRepository.insertGroceryList(groceryList)
            }
        }
    }

    fun setShowCardVisibility(newVisibility: Boolean) {
        _showCard.value = newVisibility
    }
    fun setShowMenuSheetVisibility(newVisibility: Boolean) {
        _showMenuSheet.value = newVisibility
    }

    fun createGroceryList(name: String, status: String = GroceryListStatus.ACTIVE.name) {
        viewModelScope.launch {
            val newGroceryList = GroceryList(name = name, listStatus = status)
            val newId = groceryRepository.insertGroceryList(newGroceryList)
            _newListId.value = newId
        }
    }

    fun getLabelByListId(id: Long) {
        viewModelScope.launch {
            groceryRepository.getLabelById(id).collect { label ->
                _selectedLabel.value = label
            }
        }
    }


    fun addGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            val id = groceryRepository.insertGroceryList(groceryList)
            _newListId.value = id
        }
    }

    fun changeListOrder(currentPosition: Int, newPosition: Int) {
        viewModelScope.launch {
            //groceryRepository.changeListOrder(currentPosition, newPosition)
        }
    }

    fun deleteGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            groceryRepository.deleteGroceryList(groceryList)
        }
    }

    fun resetNewListId(){
        _newListId.value = null
    }

    fun setSearchText(query: String) {
        _searchText.value = query
    }

    fun setSelectedStatus(status: GroceryListStatus?) {
        _selectedStatus.value = status
    }

    fun setSelectedLabel(label: GroceryListLabel?) {
        _selectedLabel.value = label
    }

}
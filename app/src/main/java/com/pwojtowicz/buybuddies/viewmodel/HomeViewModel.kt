package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.data.DataProvider.generateTestGroceryLists
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.data.entity.Home
import com.pwojtowicz.buybuddies.data.entity.User
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): ViewModel() {
    private val groceryRepository = GroceryRepository(application)
    private val userRepository = UserRepository(application)
    private val homeRepository = HomeRepository(application)

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
            try {
                userRepository.deleteAll()
                groceryRepository.deleteAllGroceryItems()
                homeRepository.deleteAll()

                val testUser = User(
                    firebaseUid = "test_user_123",
                    username = "Test User"
                )
                val userId = userRepository.insertUser(testUser)

                // Create and insert test homes
                val testHome = Home(
                    id = 1L,
                    name = "Test Home",
                    description = "Test home description",
                    ownerId = testUser.firebaseUid
                )
                homeRepository.insertHome(testHome)

                val testGroceryLists = generateTestGroceryLists(testUser.firebaseUid)
                testGroceryLists.forEach { groceryList ->
                    groceryRepository.insertGroceryList(groceryList)
                }

            } catch (e: Exception) {
                Log.e("ViewModel", "Error during initialization", e)
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


    suspend fun addGroceryList(groceryList: GroceryList): Long {
        return groceryRepository.insertGroceryList(groceryList)
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
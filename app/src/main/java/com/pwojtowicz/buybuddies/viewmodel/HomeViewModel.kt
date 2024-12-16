package com.pwojtowicz.buybuddies.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val application: BuyBuddiesApplication
) : ViewModel() {
    // Repositories
    private val groceryRepository = GroceryRepository(application)
    private val groceryListRepository = GroceryListRepository(application)
    private val userRepository = UserRepository(application)
    private val homeRepository = HomeRepository(application)

    // UI State
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    // Grocery Lists Data
    val groceryLists = groceryListRepository.getGroceryLists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        emptyList()
    )

    val groceryListLabels = groceryRepository.getAllGroceryListLabels().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        emptyList()
    )

    val filteredGroceryLists: StateFlow<List<GroceryList>> = combine(
        groceryLists,
        _uiState
    ) { lists, state ->
        lists.filter { groceryList ->
            (state.searchText.isEmpty() || groceryList.name.contains(state.searchText, ignoreCase = true)) &&
                    (state.selectedStatus == null || groceryList.listStatus == state.selectedStatus.name) &&
                    (state.selectedLabel == null || groceryRepository.getLabelsForList(groceryList.id).first().contains(state.selectedLabel))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        emptyList()
    )

    // UI State Updates
    private fun updateUiState(update: (HomeUiState) -> HomeUiState) {
        _uiState.value = update(_uiState.value)
    }

    // UI Event Handlers
    fun setShowCardVisibility(newVisibility: Boolean) {
        updateUiState { it.copy(showCard = newVisibility) }
    }

    fun setShowMenuSheetVisibility(newVisibility: Boolean) {
        updateUiState { it.copy(showMenuSheet = newVisibility) }
    }

    fun setSearchText(query: String) {
        updateUiState { it.copy(searchText = query) }
    }

    fun setSelectedStatus(status: GroceryListStatus?) {
        updateUiState { it.copy(selectedStatus = status) }
    }

    fun setSelectedLabel(label: GroceryListLabel?) {
        updateUiState { it.copy(selectedLabel = label) }
    }

    fun setLongPressedGroceryList(groceryListId: Long) {
        updateUiState { it.copy(longPressedListId = groceryListId) }
    }

    // Grocery List Operations
    fun createGroceryList(name: String, description: String = "") {
        viewModelScope.launch {
            try {
                val currentUser = application.authorizationClient.getSignedInUser()
                    ?: throw IllegalStateException("No user signed in")

                val newGroceryList = GroceryList(
                    name = name.trim(),
                    description = description.trim(),
                    ownerId = currentUser.firebaseUid,
                    listStatus = GroceryListStatus.ACTIVE.name,
                    createdAt = System.currentTimeMillis().toString()
                )

                val newId = groceryListRepository.insertGroceryList(newGroceryList)
                updateUiState { it.copy(
                    newListId = newId,
                    error = null
                )}
            } catch (e: Exception) {
                Log.e(TAG, "Error creating list", e)
                val errorMessage = when (e) {
                    is IllegalStateException -> "Please sign in to create a list"
                    else -> e.message ?: "Failed to create list"
                }
                updateUiState { it.copy(error = errorMessage) }
            }
        }
    }

    suspend fun addGroceryList(groceryList: GroceryList): Long {
        return groceryRepository.insertGroceryList(groceryList)
    }

    fun deleteGroceryList(groceryList: GroceryList) {
        viewModelScope.launch {
            try {
                groceryRepository.deleteGroceryList(groceryList)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting list", e)
                updateUiState { it.copy(error = e.message) }
            }
        }
    }

    fun deleteGroceryListById(groceryListId: Long) {
        viewModelScope.launch {
            try {
                groceryListRepository.deleteGroceryList(groceryListId)
                setShowMenuSheetVisibility(false)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting list", e)
                updateUiState { it.copy(
                    error = when (e) {
                        is IllegalStateException -> e.message ?: "Error accessing list"
                        else -> "Failed to delete list"
                    }
                )}
            }
        }
    }

    fun getLabelByListId(id: Long) {
        viewModelScope.launch {
            try {
                groceryRepository.getLabelById(id).collect { label ->
                    updateUiState { it.copy(selectedLabel = label) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting label", e)
                updateUiState { it.copy(error = e.message) }
            }
        }
    }

    // State Reset Functions
    fun resetNewListId() {
        updateUiState { it.copy(newListId = null) }
    }

    fun clearError() {
        updateUiState { it.copy(error = null) }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(
    val showCard: Boolean = false,
    val showMenuSheet: Boolean = false,
    val newListId: Long? = null,
    val longPressedListId: Long = 0,
    val searchText: String = "",
    val selectedStatus: GroceryListStatus? = null,
    val selectedLabel: GroceryListLabel? = null,
    val error: String? = null
)

//    init {
//        viewModelScope.launch {
//            try {
//                userRepository.deleteAll()
//                groceryRepository.deleteAllGroceryItems()
//                homeRepository.deleteAll()
//
//                val testUser = User(
//                    firebaseUid = "test_user_123",
//                    username = "Test User"
//                )
//                val userId = userRepository.insertUser(testUser)
//
//                // Create and insert test homes
//                val testHome = Home(
//                    id = 1L,
//                    name = "Test Home",
//                    description = "Test home description",
//                    ownerId = testUser.firebaseUid
//                )
//                homeRepository.insertHome(testHome)
//
//                val testGroceryLists = generateTestGroceryLists(testUser.firebaseUid)
//                testGroceryLists.forEach { groceryList ->
//                    groceryRepository.insertGroceryList(groceryList)
//                }
//
//            } catch (e: Exception) {
//                Log.e("ViewModel", "Error during initialization", e)
//            }
//        }
//    }
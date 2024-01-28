package com.pwojtowicz.buybuddies.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
import com.pwojtowicz.buybuddies.ui.Screens
import com.pwojtowicz.buybuddies.ui.components.CustomTopBar
import com.pwojtowicz.buybuddies.ui.components.GroceryItemAdd
import com.pwojtowicz.buybuddies.ui.components.GroceryItemInputRow
import com.pwojtowicz.buybuddies.ui.components.GroceryItemRow
import com.pwojtowicz.buybuddies.ui.components.LineSpacer
import com.pwojtowicz.buybuddies.ui.components.TopScreenPopup
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel

data class GroceryItemsListUIState(
    val showInputRow: Boolean,
    val setShowInputRow: (Boolean) -> Unit,
    val popupMessage: String,
    val setPopupMessage: (String) -> Unit,
    val setShowPopup: (Boolean) -> Unit
)

@Composable
fun GroceryItemsListScreen(
    navController: NavHostController,
    viewModel: GroceryViewModel,
    navigationPaddingValues: PaddingValues
){
    val groceryItems = viewModel.groceryItems.collectAsState(initial = emptyList()).value

    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    var showInputRow by remember { mutableStateOf(false) }

    val activeList by viewModel.activeGroceryList.collectAsState()


    val uiState = GroceryItemsListUIState(
        showInputRow = showInputRow,
        setShowInputRow = { showInputRow = it },
        popupMessage = popupMessage,
        setPopupMessage = { popupMessage = it },
        setShowPopup = { showPopup = it }
    )

    Scaffold(
        topBar = {
            activeList?.let {list ->
                CustomTopBar(
                    groceryList = list,
                    deleteList = {
                        viewModel.deleteGroceryList(list)
                        navController.navigate(Screens.Home.route)
                    },
                    editList = {
                        viewModel.updateGroceryList(it)
                    },
                    updateAsDone = {
                        viewModel.updateGroceryList(it)
                        navController.navigate(Screens.Home.route)
                    },
                )
            }?: run {
                TopScreenPopup(
                    message = "Problem loading List",
                    onDismiss = {}
                )
            }
        }
    ) {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ){
            GroceryItemsList(
                groceryItems = groceryItems,
                paddingValues = navigationPaddingValues,
                viewModel = viewModel,
                activeList = activeList,
                uiState = uiState
            )
        }
    }
}

@Composable
fun GroceryItemsList(
    groceryItems: List<GroceryItem>,
    paddingValues: PaddingValues,
    viewModel: GroceryViewModel,
    activeList: GroceryList?,
    uiState: GroceryItemsListUIState
) {
    LazyColumn(contentPadding = paddingValues){
        items(groceryItems.size) { id ->
            GroceryItemRow(
                groceryItem = groceryItems[id],
                onItemCheckChanged = { updatedItem -> viewModel.updateGroceryItem(updatedItem) },
                onClickEditGroceryItem = { editedItem -> viewModel.updateGroceryItem(editedItem) },
                onClickDeleteGroceryItem = { deletedItem -> viewModel.deleteGroceryItem(deletedItem) }
            )
            LineSpacer()
        }
        item {
            if (uiState.showInputRow) {
                GroceryItemInputRow(
                    onItemAdded = { name, count ->
                        activeList?.let { list ->
                            viewModel.addGroceryItem(
                                GroceryItem(
                                    listId = list.id,
                                    name = name,
                                    count = count
                                )
                            )
                        } ?: run{
                            uiState.setPopupMessage("Couldn't find the Grocery List")
                            uiState.setShowPopup(true)
                        }
                        uiState.setShowInputRow(false)
                    },
                    onCancel = {
                        uiState.setShowInputRow(false)
                    }
                )
            }
            else {
                GroceryItemAdd(onAddClick = { uiState.setShowInputRow(true) })
            }
        }
    }
}
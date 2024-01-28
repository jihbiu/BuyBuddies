package com.pwojtowicz.buybuddies.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pwojtowicz.buybuddies.ui.components.GroceryListContainer
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: GroceryViewModel,
    paddingValues: PaddingValues
) {
    val groceryLists = viewModel.groceryListsState.collectAsStateWithLifecycle()
    Column(){
        GroceryListContainer(
            groceryLists = groceryLists.value,
            paddingValues = paddingValues,
            viewModel = viewModel, // Pass ViewModel here
            navController = navController
        )
    }
}
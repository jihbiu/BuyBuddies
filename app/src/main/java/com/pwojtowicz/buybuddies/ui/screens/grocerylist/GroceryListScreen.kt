package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.ui.components.ContainerCard
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.groceryitem.GroceryItemRow
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun GroceryListScreen(
    application: Application,
    groceryListId: Long,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val viewModel: GroceryViewModel = viewModel(
        factory = GroceryViewModelFactory(application)
    )
    viewModel.setActiveGroceryListId(groceryListId)

    val groceryItemsList by viewModel.groceryItemsList.collectAsState()
    val showAddGroceryItem = remember { MutableStateFlow(false) }

    val activeItems = groceryItemsList.filter { it.listId == groceryListId && !it.isChecked }
    val completedItems = groceryItemsList.filter { it.listId == groceryListId && it.isChecked }


    Box(modifier = Modifier.padding(paddingValues)) {
        if (showAddGroceryItem.collectAsState().value) {
            AddGroceryItem(
                isVisible = showAddGroceryItem,
                onDismiss = { showAddGroceryItem.value = false },
                onAddItem = { name, quantity, unit ->
                    viewModel.createGroceryItem(
                        listId = groceryListId,
                        depotId = null,
                        name = name,
                        quantity = quantity
                    )
                }
            )
        }
        Column {
            GroceryListTopContainer(onProfileClick = {
                navigateToScreen(
                    navController = navController,
                    route = NavItems.Profile.route
                )
            })
            ContainerCard(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                contentPadding = 8.dp
            ) {
                LazyColumn {
                    item {
                        Text(
                            text = "Active Items",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                    items(activeItems, key = { it.id }) { groceryItem ->
                        GroceryItemRow(
                            groceryItem = groceryItem,
                            onCheckedChange = { viewModel.toggleGroceryItemChecked(groceryItem) },
                            onSaveChanges = { updatedItem -> viewModel.updateGroceryItem(updatedItem) },
                            onDelete = { viewModel.deleteGroceryItem(groceryItem) }
                        )
                    }

                    if (completedItems.isNotEmpty()) {
                        item {
                            Text(
                                text = "Completed Items",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                        items(completedItems, key = { it.id }) { groceryItem ->
                            GroceryItemRow(
                                groceryItem = groceryItem,
                                onCheckedChange = { viewModel.toggleGroceryItemChecked(groceryItem) },
                                onSaveChanges = { updatedItem -> viewModel.updateGroceryItem(updatedItem) },
                                onDelete = { viewModel.deleteGroceryItem(groceryItem) }
                            )
                        }
                    }
                }
            }
        }
        GroceryItemAddFAB(
            onClick = { showAddGroceryItem.value = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(60.dp)
        )
    }
}


@Preview()
@Composable
fun PreviewGroceryListScreen() {
    val paddingValues = PaddingValues(30.dp)
    val application: Application? = null
    val navController = rememberNavController()

    GroceryListScreen(
        application = application!!,
        groceryListId = 0,
        navController = navController,
        paddingValues = paddingValues
    )
}
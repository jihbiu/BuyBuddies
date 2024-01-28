package com.pwojtowicz.buybuddies.ui

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.data.model.GroceryList
import com.pwojtowicz.buybuddies.data.model.GroceryListStatus
import com.pwojtowicz.buybuddies.ui.components.GroceryListAddDialog
import com.pwojtowicz.buybuddies.ui.screens.GroceryItemsListScreen
import com.pwojtowicz.buybuddies.ui.screens.HomeScreen
import com.pwojtowicz.buybuddies.ui.screens.SettingsScreen
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel
import com.pwojtowicz.buybuddies.viewmodel.GroceryListsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val groceryViewModel: GroceryViewModel = viewModel(
        LocalViewModelStoreOwner.current!!,
        "TaskViewModel",
        GroceryListsViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    var showAddDialog by remember { mutableStateOf(false) }
    val onAddList = { showAddDialog = true }

    Scaffold(
        bottomBar = {BottomMenu(navController = navController, onAddList = onAddList) },
        content = { NavGraph(paddingValues = it, navController = navController, groceryViewModel)}
    )

    if (showAddDialog) {
        GroceryListAddDialog(
            onDismiss = { showAddDialog = false },
            onSave = { newListName ->
                val currentDate = getCurrentDate()
                groceryViewModel.addGroceryList(GroceryList(
                    name = newListName,
                    date = currentDate,
                    status = GroceryListStatus.ACTIVE
                ))
                showAddDialog = false
            }
        )
    }
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

@Composable
fun BottomMenu(navController: NavHostController, onAddList: () -> Unit){
    val screens = listOf(
        Screens.Home, Screens.AddGroceryList, Screens.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar{
        screens.forEach{screen ->
            NavigationBarItem(
                label = { Text(text = screen.title)},
                icon = { Icon(imageVector = screen.icon, contentDescription = "icon") },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    if (screen == Screens.AddGroceryList) {
                        onAddList()
                    } else {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}

@Composable
fun NavGraph(paddingValues: PaddingValues, navController: NavHostController, groceryViewModel: GroceryViewModel){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route){
            HomeScreen(
                navController = navController,
                viewModel = groceryViewModel,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AddGroceryList.route){

        }
        composable(route = Screens.Settings.route){ SettingsScreen() }
        composable(route = Screens.GroceryItems.route) {
            GroceryItemsListScreen(
                navController = navController,
                viewModel = groceryViewModel,
                navigationPaddingValues = paddingValues
            )
        }
    }
}
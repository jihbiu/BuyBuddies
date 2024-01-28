package com.pwojtowicz.buybuddies.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screens("home_screen","Home", Icons.Default.Home)
    object AddGroceryList : Screens("add_grocery_list", "Add", Icons.Default.Add)
    object Settings : Screens("settings", "Settings", Icons.Default.Settings)
    object GroceryItems : Screens("grocery_item_list", "", Icons.Default.ArrowForward)
}
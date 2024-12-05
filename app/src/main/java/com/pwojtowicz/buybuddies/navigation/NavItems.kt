package com.pwojtowicz.buybuddies.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItems(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Menu : NavItems("menu","Menu", Icons.Default.Menu)
    object Home : NavItems("home","Home", Icons.Default.Home)
    object Notification : NavItems("notification", "Notification", Icons.Default.Notifications)
    object Scanner : NavItems("scanner", "Scanner", Icons.Default.Search )
    object GroceryList : NavItems("grocery_list", "GroceryList", Icons.Default.Add)
    object Type : NavItems("type", "Type", Icons.Default.List)
    object Depot : NavItems("depot", "Depot", Icons.Default.List)
    object Settings : NavItems("settings", "Settings", Icons.Default.Settings)
    object Info : NavItems("info", "Info", Icons.Default.Info)
    object Profile : NavItems("profile", "Profile", Icons.Default.Person)
    object Login : NavItems("login", "Login", Icons.Default.Settings)
    object Register : NavItems("register", "Register", Icons.Default.Settings)
    object ForgotPassword : NavItems("forgot_password", "Forgot Password", Icons.Default.Close)
}
package com.pwojtowicz.buybuddies.navigation

import android.app.Activity.RESULT_OK
import android.app.Application
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.navigation.menu.BottomMenu
import com.pwojtowicz.buybuddies.navigation.menu.menudrawer.MenuDrawer
import com.pwojtowicz.buybuddies.ui.screens.auth.LoginScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.GroceryListScreen
import com.pwojtowicz.buybuddies.ui.screens.home.HomeScreen
import com.pwojtowicz.buybuddies.ui.screens.notifications.NotificationScreen
import com.pwojtowicz.buybuddies.ui.screens.profile.ProfileScreen
import com.pwojtowicz.buybuddies.ui.screens.scanner.ScannerScreen
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun Navigation(authorizationClient: AuthorizationClient) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                navController = navController,
                drawerState = drawerState
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomMenu(
                    navController = navController,
                    coroutineScope = coroutineScope,
                    drawerState = drawerState
                )
            },
            content = { paddingValues ->
                NavGraph(
                    paddingValues = paddingValues,
                    navController = navController,
                    authorizationClient = authorizationClient
                )
            }
        )
    }
}

@Composable
fun NavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    authorizationClient: AuthorizationClient
) {
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authorizationClient))
    val signInState by authViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (signInState.isSignedIn) "main" else "auth"
    ) {
        navigation(
            startDestination = NavItems.Login.route,
            route = "auth"
        ) {
            composable(NavItems.Login.route) {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            coroutineScope.launch {
                                val signInResult = authorizationClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                authViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                    if (signInState.isSignInSuccessful) {
                        Toast.makeText(
                            context,
                            "Sign in successful",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                        authViewModel.resetState()
                    }
                }

                LoginScreen(
                    state = signInState,
                    onSignInClick = {
                        authViewModel.startSignIn {
                            val signInIntentSender = authorizationClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@startSignIn
                                ).build()
                            )
                        }
                    }
                )
            }
            composable(NavItems.Register.route) {}
            composable(NavItems.ForgotPassword.route) {}
        }
        navigation(
            startDestination = NavItems.Home.route,
            route = "main"
        ) {
            composable(NavItems.Home.route) {
                HomeScreen(
                    application = application,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
            composable("${NavItems.GroceryList.route}/{groceryListId}") { backStackEntry ->
                val groceryListId = backStackEntry.arguments?.getString("groceryListId") ?: ""
                GroceryListScreen(
                    application = application,
                    groceryListId = groceryListId.toLong(),
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
            composable(NavItems.Profile.route) {
                ProfileScreen(
                    application = application,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
            composable(NavItems.Settings.route) {}
            composable(NavItems.Notification.route) { NotificationScreen() }
            composable(NavItems.Scanner.route) { ScannerScreen() }
        }
    }
}

fun navigateToScreen(
    navController: NavHostController,
    route: String,
    args: Map<String, String> = emptyMap()
) {
    val argRoute = buildString {
        append(route)
        if (args.isNotEmpty()) {
            args.forEach { (value) ->
                append("/$value")
            }
        }
    }

    navController.navigate(argRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
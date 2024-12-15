package com.pwojtowicz.buybuddies.navigation

import LoginScreen
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.navigation.menu.BottomMenu
import com.pwojtowicz.buybuddies.navigation.menu.menudrawer.MenuDrawer
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.GroceryListScreen
import com.pwojtowicz.buybuddies.ui.screens.home.HomeScreen
import com.pwojtowicz.buybuddies.ui.screens.notifications.NotificationScreen
import com.pwojtowicz.buybuddies.ui.screens.profile.ProfileScreen
import com.pwojtowicz.buybuddies.ui.screens.scanner.ScannerScreen
import com.pwojtowicz.buybuddies.ui.screens.settings.SettingsScreen
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    application: BuyBuddiesApplication,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application))
    val signInState by authViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if(signInState.isSignedIn) NavRoute.Main.route else NavRoute.Auth.route
    ) {
        authNavigation(navController, application, authViewModel)
        mainNavigation(navController, application, authViewModel)
    }
}

private fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    application: BuyBuddiesApplication,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = NavItems.Login.route,
        route = NavRoute.Auth.route
    ) {
        composable(NavItems.Login.route) {
            AuthContent(
                application = application,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(NavItems.Register.route) { }
        composable(NavItems.ForgotPassword.route) { }
    }
}

private fun NavGraphBuilder.mainNavigation(
    navController: NavHostController,
    application: BuyBuddiesApplication,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = NavItems.Home.route,
        route = NavRoute.Main.route
    ) {
        composable(NavItems.Home.route) {
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                HomeScreen(
                    application = application,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable("${NavItems.GroceryList.route}/{groceryListId}") { backStackEntry ->
            val groceryListId = backStackEntry.arguments?.getString("groceryListId") ?: ""
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                GroceryListScreen(
                    application = application,
                    groceryListId = groceryListId.toLong(),
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable(NavItems.Profile.route) {
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                ProfileScreen(
                    application = application,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable(NavItems.Settings.route) {
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                SettingsScreen()
            }
        }

        composable(NavItems.Notification.route) {
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                NotificationScreen()
            }
        }

        composable(NavItems.Scanner.route) {
            MainContent(
                navController = navController,
                application = application,
                authViewModel = authViewModel
            ) { paddingValues ->
                ScannerScreen()
            }
        }
    }
}

@Composable
fun AuthContent(
    application: BuyBuddiesApplication,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val signInState by authViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK){
                coroutineScope.launch {
                    val signInResult = application.authorizationClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    authViewModel.onSignInResult(signInResult)
                }
            }
            else{
                authViewModel.resetLoadingState()
            }
        }
    )

    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if(signInState.isSignInSuccessful){
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show()
            navController.navigate(NavRoute.Main.route){
                popUpTo(NavRoute.Auth.route) { inclusive = true }
            }
            authViewModel.resetState()
        }
    }

    LoginScreen(
        state = signInState,
        onSignInClick = {
            authViewModel.startSignIn {
                coroutineScope.launch {
                    val signInIntentSender = application.authorizationClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        }
    )
}

@Composable
private fun MainContent(
    navController: NavHostController,
    application: BuyBuddiesApplication,
    authViewModel: AuthViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                navController = navController,
                drawerState = drawerState,
                application = application,
                authViewModel = authViewModel
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
            }
        ) { paddingValues ->
            content(paddingValues)
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
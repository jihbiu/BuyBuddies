package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuDrawer(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .fillMaxHeight()
            .padding(top = 30.dp),
        color = bb_theme_main_color,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MenuDrawerProfile(
                onClick = {
                    navigateToScreenFromDrawer(
                        navController,
                        NavItems.Profile.route,
                        drawerState,
                        coroutineScope
                    )
                }
            )
            MenuDrawerItem(
                navItem = NavItems.Home,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Type,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Depot,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )

            MenuDrawerItem(
                navItem = NavItems.Settings,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Info,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
        }
    }
}



fun navigateToScreenFromDrawer(
    navController: NavHostController,
    route: String,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    navigateToScreen(
        navController = navController,
        route = route
    )
    coroutineScope.launch {
        drawerState.close()
    }
}


@Preview(showBackground = true, widthDp = 200)
@Composable
fun MenuDrawerPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    MaterialTheme {
        Surface {
            MenuDrawer(navController, drawerState)
        }
    }
}
package com.pwojtowicz.buybuddies.ui.screens.auth


import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.auth.SignInState
import com.pwojtowicz.buybuddies.ui.components.ContainerCard
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color

@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            snackbarHostState.showSnackbar(
                message = error
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .weight(0.5f)
                .fillMaxWidth()
                .background(bb_theme_main_color),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(id = R.string.welcome_message),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.weight(0.8f))

            Image(
                painter = painterResource(id = R.drawable.bb_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(horizontal = 60.dp)
                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        ContainerCard(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),  //bottom nav height
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                }
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bb_default_propfile_picture),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign in with Google")
                }
            }
        }
    }
}

//            Row(
//                modifier = Modifier
//                    .background(bb_theme_main_color)
//            ){
//
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {

//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                Text(
//                    text = "Welcome to BuyBuddies",
//                    style = MaterialTheme.typography.headlineMedium
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//

//            }

//            if (state.isLoading) {
//                CircularProgressIndicator()
//            }
//
//            SnackbarHost(
//                hostState = snackbarHostState,
//            )
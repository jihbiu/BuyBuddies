package com.pwojtowicz.buybuddies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.pwojtowicz.buybuddies.ui.Navigation
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent() {
        BuyBuddiesTheme {
            Navigation()
        }
    }
}


package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListMenuSheet(
    isVisible: MutableStateFlow<Boolean>,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onMarkAsDone: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    var isCardVisible = isVisible.collectAsState().value

    if (isCardVisible) {
        ModalBottomSheet(
            containerColor = bb_theme_card_clr_light,
            scrimColor = Color(0x33000000),
            onDismissRequest = {
                isVisible.value = false
                onDismiss() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextButton(
                    onClick = {
                        isCardVisible = false
                        onEdit()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Name")
                }
                TextButton(
                    onClick = {
                        isCardVisible = false
                        onMarkAsDone()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Done")
                }
                TextButton(
                    onClick = {
                        isCardVisible = false
                        onDelete()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete")
                }
                TextButton(
                    onClick = {
                        isCardVisible = false
                        onShare()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share")
                }
            }
        }
    }
}

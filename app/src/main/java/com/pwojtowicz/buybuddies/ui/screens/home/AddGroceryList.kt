package com.pwojtowicz.buybuddies.ui.screens.home


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.components.BBOutlinedTextField
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroceryList(
    isVisible: MutableStateFlow<Boolean>,
    onDismiss: () -> Unit,
    onCreateList: (String) -> Unit
) {
    var isCardVisible = isVisible.collectAsState().value
    var listName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }

    if (isCardVisible) {
        ModalBottomSheet(
            containerColor = bb_theme_card_clr_light,
            scrimColor = Color(0x33000000),
            onDismissRequest = {
                isCardVisible = false
                onDismiss()
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .requiredHeight(300.dp)
                    .padding(
                        start = 32.dp,
                        top = 4.dp,
                        end = 32.dp,
                        bottom = 32.dp
                    )
            ) {
                Text(
                    text = "Add new List",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        color = bb_theme_text_clr_dark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                BBOutlinedTextField(
                    value = listName,
                    onValueChange = {
                        listName = it
                        isError = it.isEmpty()
                                    },

                    isError = isError,
                    label = { Text(if (isError) "List Name - Required" else "List Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Spacer(
                    Modifier.weight(1.0f)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (listName.isBlank()) {
                                isError = true
                            }
                            else {
                                onCreateList(listName)
                                isCardVisible = false
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Create List")
                    }
                    OutlinedButton(
                        onClick = {
                            isError = false
                            isCardVisible = false
                            onDismiss()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroceryListCreationCardPreview() {
    val isVisible = MutableStateFlow(true)
    val onCreateList: (String) -> Unit = { }
    BuyBuddiesTheme {
        AddGroceryList(
            isVisible = isVisible,
            onDismiss = {},
            onCreateList = onCreateList,
        )
    }
}
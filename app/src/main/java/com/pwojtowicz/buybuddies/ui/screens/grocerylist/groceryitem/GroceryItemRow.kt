package com.pwojtowicz.buybuddies.ui.screens.grocerylist.groceryitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.ui.components.BBDropdownMenu
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_selected_clr
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_gray

@Composable
fun GroceryItemRow(
    groceryListItem: GroceryListItem,
    onCheckedChange: (Boolean) -> Unit,
    onSaveChanges: (GroceryListItem) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(groceryListItem.name) }
    var quantity by remember { mutableStateOf(groceryListItem.quantity.toString()) }
    var checked by remember { mutableStateOf(groceryListItem.isChecked) }

    var editable by remember { mutableStateOf(false) }
    var colorEditable by remember { mutableStateOf(bb_theme_text_clr_gray) }

    val unitOptions = listOf("Unit", "kg", "g", "l", "ml")
    var selectedUnitOption by remember { mutableStateOf(unitOptions[0]) }

    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(if (editable) bb_theme_main_selected_clr.copy(0.25f) else Color.Transparent)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onCheckedChange(it)
                }
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = name,
                onValueChange = { if (editable) name = it },
                readOnly = !editable,
                singleLine = true,
                textStyle = TextStyle(color = Color.DarkGray),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom)
                    .background(Color(0x4DFFFFFF))
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = quantity,
                onValueChange = { if (editable) quantity = it },
                readOnly = !editable,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(color = Color.DarkGray),
                modifier = Modifier
                    .width(50.dp)
                    .align(Alignment.Bottom)
                    .background(Color(0x4DFFFFFF)),
            )
            Spacer(Modifier.width(8.dp))
            BBDropdownMenu(
                selectedValue = selectedUnitOption,
                options = unitOptions,
                label = "Unit",
                onValueChange = { selectedUnitOption = it },
                textColor = bb_theme_text_clr_dark,
                backgroundColor = bb_theme_background_clr_light,
                dropdownItemColor = bb_theme_text_clr_dark,
                dropdownBackgroundColor = bb_theme_background_clr_light,
                labelColor = colorEditable,
                textStyle = TextStyle(fontSize = 14.sp),
                labelStyle = TextStyle(fontSize = 14.sp, color = colorEditable),
                modifier = Modifier.width(95.dp)
            )
            Box {
                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = colorEditable
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },

                    ) {
                    DropdownMenuItem(
                        text = { Text(if (editable) "Save" else "Edit") },
                        onClick = {
                            if (editable) {
                                val updatedItem = groceryListItem.copy(
                                    name = name,
                                    quantity = quantity.toDoubleOrNull() ?: 0.0,
                                    isChecked = checked,
                                )
                                onSaveChanges(updatedItem)
                            }
                            editable = !editable
                            colorEditable = if (editable) bb_theme_text_clr_dark
                            else bb_theme_text_clr_gray
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}
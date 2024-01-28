package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.data.model.GroceryItem

@Composable
fun GroceryItemAdd(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.weight(0.25f))
        Button(
            modifier = Modifier
                .weight(0.50f),
            onClick = onAddClick
        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Grocery",
            )
        }
        Spacer(modifier = Modifier.weight(0.25f))
    }
}

@Preview
@Composable
fun GroceryItemAddPreview(){
    GroceryItemAdd(onAddClick = {})
}
package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.ui.components.user.SideUserProfile

@Composable
fun GroceryListTopContainer(listName: String = "List Name", onProfileClick: () -> Unit) {
    val height = 100.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        SideUserProfile(height = height, onProfileClick)
        Spacer(modifier = Modifier.weight(1f))
        ListNameBox(listName = listName, height = height)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun ListNameBox(listName: String, height: Dp){
    val museoModernoRegular = FontFamily(Font(R.font.museo_moderno_regular))
    Box(
        modifier = Modifier
            .height(height),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = listName,
                style = TextStyle(
                    fontFamily = museoModernoRegular,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
            ListNameUnderlining(modifier = Modifier.padding())
        }
    }
}

@Composable
fun ListNameUnderlining(modifier: Modifier){
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        thickness = 1.dp,
        modifier = modifier
    )
}

@Preview
@Composable
fun GroceryListTopContainerPreview(){
    GroceryListTopContainer("List Name", {})
}
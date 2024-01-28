package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pwojtowicz.buybuddies.data.model.GroceryList
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pwojtowicz.buybuddies.ui.Screens
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel


@Composable
fun GroceryListContainer(
    groceryLists: List<GroceryList>,
    paddingValues: PaddingValues,
    viewModel: GroceryViewModel,
    navController: NavHostController
) {
    if(groceryLists.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Add some Grocery Lists :)",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
    else {
        LazyColumn(contentPadding = paddingValues) {
            items(groceryLists) { groceryList ->
                GroceryListItem(
                    groceryList = groceryList,
                    onItemClick = {
                        viewModel.selectList(groceryList)
                        navController.navigate(Screens.GroceryItems.route)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroceryListContainerPreview(){
    val groceryList = mutableListOf<List<GroceryList>>(
        listOf(
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021"),
            GroceryList(name = "Lista zakupów", date = "12.12.2021")
        )
    )
}

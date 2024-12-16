package com.pwojtowicz.buybuddies.data

import com.pwojtowicz.buybuddies.data.entity.GroceryList

object DataProvider {
    fun generateTestGroceryLists(ownerId: String): List<GroceryList> {
        return listOf(
            GroceryList(
                id = 1L,
                name = "Weekly Groceries",
                homeId = null,
                ownerId = ownerId
            ),
            GroceryList(
                id = 2L,
                name = "Party Supplies",
                homeId = null,
                ownerId = ownerId
            ),
            GroceryList(
                id = 3L,
                name = "Healthy Foods",
                homeId = 1L,
                ownerId = ownerId
            ),
            GroceryList(
                id = 4L,
                name = "Emergency Supplies",
                homeId = null,
                ownerId = ownerId
            ),
            GroceryList(
                id = 5L,
                name = "Monthly Stock-up",
                homeId = 1L,
                ownerId = ownerId
            )
        )
    }
}
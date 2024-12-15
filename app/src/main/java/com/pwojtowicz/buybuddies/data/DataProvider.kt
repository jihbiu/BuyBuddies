package com.pwojtowicz.buybuddies.data

import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import kotlin.random.Random

object DataProvider {
//    fun generateRandomGroceryItems(
//        groceryLists: List<GroceryList>,
//        numberOfItems: Int = 100
//    ): List<GroceryListItem> {
//        val randomItems = mutableListOf<GroceryListItem>()
//
//        for (i in 1..numberOfItems) {
//            val listId = groceryLists.random().id
//            val itemName = groceryItemNames.random()
//            val quantity = Random.nextDouble(0.1, 10.0)
//            val unit = MeasurementUnit.values().random()
//
//            randomItems.add(
//                GroceryListItem(
//                    listId = listId,
//                    name = itemName,
//                    description = "Description for $itemName",
//                    quantity = quantity,
//                    unit = unit,
//                    isChecked = false,
//                    categoryId = Random.nextLong(1, 9),
//                )
//            )
//        }
//
//        return randomItems
//    }

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
//
//    private val groceryItemNames = listOf(
//        "Apples", "Bananas", "Oranges", "Grapes", "Strawberries", "Blueberries", "Raspberries",
//        "Lemons", "Limes", "Cherries", "Peaches", "Pears", "Plums", "Pineapples", "Mangoes",
//        "Watermelon", "Cantaloupe", "Honeydew", "Tomatoes", "Cucumbers", "Carrots", "Lettuce",
//        "Spinach", "Broccoli", "Cauliflower", "Bell Peppers", "Mushrooms", "Onions", "Garlic",
//        "Potatoes", "Sweet Potatoes", "Yams", "Corn", "Green Beans", "Peas", "Zucchini",
//        "Eggplant", "Pumpkin", "Celery", "Avocado", "Asparagus", "Kale", "Cabbage", "Brussels Sprouts",
//        "Leeks", "Beets", "Radishes", "Turnips", "Squash", "Chard", "Bok Choy", "Artichokes",
//        "Okra", "Collard Greens", "Fennel", "Mustard Greens", "Arugula", "Endive", "Alfalfa Sprouts",
//        "Milk", "Cheese", "Yogurt", "Butter", "Eggs", "Bread", "Rice", "Pasta", "Oats", "Flour",
//        "Sugar", "Honey", "Maple Syrup", "Tea", "Coffee", "Juice", "Soda", "Water", "Chicken",
//        "Beef", "Pork", "Turkey", "Lamb", "Fish", "Shrimp", "Tofu", "Beans", "Lentils", "Nuts",
//        "Seeds", "Olive Oil", "Vinegar", "Salt", "Pepper", "Spices", "Herbs", "Ketchup", "Mustard",
//        "Mayonnaise", "Soy Sauce", "Hot Sauce", "Chocolate", "Vanilla", "Cinnamon", "Mint"
//    )
}
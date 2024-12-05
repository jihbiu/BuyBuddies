package com.pwojtowicz.buybuddies.data

import com.pwojtowicz.buybuddies.data.model.groceryitem.GroceryItem
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object DataProvider {
    fun generateRandomGroceryItems(
        groceryLists: List<GroceryList>,
        numberOfItems: Int = 100): List<GroceryItem>
    {
        val randomItems = mutableListOf<GroceryItem>()

        for (i in 1..numberOfItems) {
            val listId = groceryLists.random().id
            val itemName = groceryItemNames.random()
            val count = Random.nextInt(1, 6)

            randomItems.add(
                GroceryItem(
                listId = listId,
                name = itemName,
                quantity = count,
                isChecked = false
            )
            )
        }

        return randomItems
    }

    private val groceryItemNames = listOf(
        "Apples", "Bananas", "Oranges", "Grapes", "Strawberries", "Blueberries", "Raspberries",
        "Lemons", "Limes", "Cherries", "Peaches", "Pears", "Plums", "Pineapples", "Mangoes",
        "Watermelon", "Cantaloupe", "Honeydew", "Tomatoes", "Cucumbers", "Carrots", "Lettuce",
        "Spinach", "Broccoli", "Cauliflower", "Bell Peppers", "Mushrooms", "Onions", "Garlic",
        "Potatoes", "Sweet Potatoes", "Yams", "Corn", "Green Beans", "Peas", "Zucchini",
        "Eggplant", "Pumpkin", "Celery", "Avocado", "Asparagus", "Kale", "Cabbage", "Brussels Sprouts",
        "Leeks", "Beets", "Radishes", "Turnips", "Squash", "Chard", "Bok Choy", "Artichokes",
        "Okra", "Collard Greens", "Fennel", "Mustard Greens", "Arugula", "Endive", "Alfalfa Sprouts",
        "Milk", "Cheese", "Yogurt", "Butter", "Eggs", "Bread", "Rice", "Pasta", "Oats", "Flour",
        "Sugar", "Honey", "Maple Syrup", "Tea", "Coffee", "Juice", "Soda", "Water", "Chicken",
        "Beef", "Pork", "Turkey", "Lamb", "Fish", "Shrimp", "Tofu", "Beans", "Lentils", "Nuts",
        "Seeds", "Olive Oil", "Vinegar", "Salt", "Pepper", "Spices", "Herbs", "Ketchup", "Mustard",
        "Mayonnaise", "Soy Sauce", "Hot Sauce", "Chocolate", "Vanilla", "Cinnamon", "Mint"
    )

    fun generateTestGroceryLists(): List<GroceryList> {
        return listOf(
            GroceryList(
                id = 1,
                name = "Weekly Groceries",
                sortOrder = 1
            ),
            GroceryList(
                id = 2,
                name = "Party Supplies",
                sortOrder = 2
            ),
            GroceryList(
                id = 3,
                name = "Healthy Foods",
                sortOrder = 3
            ),
            GroceryList(
                id = 4,
                name = "Emergency Supplies",
                sortOrder = 4
            ),
            GroceryList(
                id = 5,
                name = "Monthly Stock-up",
                sortOrder = 5
            )
        )
    }
}

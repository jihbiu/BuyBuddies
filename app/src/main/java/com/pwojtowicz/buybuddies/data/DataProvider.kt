package com.pwojtowicz.buybuddies.data

import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList
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

            randomItems.add(GroceryItem(
                listId = listId,
                name = itemName,
                count = count,
                isChecked = false
            ))
        }

        return randomItems
    }

    val groceryLists = listOf<GroceryList>(
        GroceryList(name = "Zakupy na święta", date = "24.12.2023"),
        GroceryList(name = "Tygodniowe zakupy", date = "05.01.2023"),
        GroceryList(name = "Przyjęcie urodzinowe", date = "15.02.2023"),
        GroceryList(name = "Grillowanie w ogrodzie", date = "30.04.2023"),
        GroceryList(name = "Zakupy na piknik", date = "15.06.2023"),
        GroceryList(name = "Produkty na wypieki", date = "22.09.2023"),
        GroceryList(name = "Zakupy na weekend", date = "12.11.2023"),
        GroceryList(name = "Zakupy do biura", date = "03.12.2023"),
        GroceryList(name = "Specjalne zakupy świąteczne", date = "20.12.2023"),
        GroceryList(name = "Zakupy na imprezę", date = "10.01.2023"),
        GroceryList(name = "Zakupy szkolne", date = "02.09.2023"),
        GroceryList(name = "Warzywa i owoce", date = "05.05.2023"),
        GroceryList(name = "Zakupy na grilla", date = "18.06.2023"),
        GroceryList(name = "Zakupy na Wielkanoc", date = "01.04.2023"),
        GroceryList(name = "Zakupy na Dzień Matki", date = "20.05.2023"),
        GroceryList(name = "Zakupy na Halloween", date = "30.10.2023"),
        GroceryList(name = "Zakupy na Nowy Rok", date = "30.12.2023"),
        GroceryList(name = "Zakupy na Walentynki", date = "12.02.2023"),
        GroceryList(name = "Zakupy na Dzień Dziecka", date = "30.05.2023")
    )

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
}

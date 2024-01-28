package com.pwojtowicz.buybuddies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE
}

@Entity(tableName = "grocery_lists")
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String,
    val status: GroceryListStatus = GroceryListStatus.ACTIVE
)

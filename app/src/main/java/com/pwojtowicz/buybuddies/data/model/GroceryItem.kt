package com.pwojtowicz.buybuddies.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_item", foreignKeys = [
    ForeignKey(
        entity = GroceryList::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class GroceryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int = 0,
    val name: String,
    val count: Int = 0,
    var isChecked: Boolean = false
)

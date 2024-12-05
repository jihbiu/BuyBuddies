package com.pwojtowicz.buybuddies.data.model.grocerylist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "grocery_list_images",
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["groceryListId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroceryListImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val groceryListId: Long,
    val imagePath: String
)

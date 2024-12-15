package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit

@Entity(
    tableName = "grocery_items",
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["listId"]), Index(value = ["categoryId"])]
)
data class GroceryListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val name: String,
    val description: String? = "",
    val quantity: Double,
    val unit: MeasurementUnit = MeasurementUnit.PIECE,
    val categoryId: Long? = null,
    val isChecked: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

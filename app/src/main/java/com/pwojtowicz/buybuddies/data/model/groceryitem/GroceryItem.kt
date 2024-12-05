package com.pwojtowicz.buybuddies.data.model.groceryitem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.model.depot.Depot
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList

@Entity(tableName = "grocery_items",
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("listId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Depot::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("depotId"),
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["listId"]), Index(value = ["depotId"])]
)
data class GroceryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val depotId: Long? = null,
    val name: String,
    val quantity: Int,
    val isChecked: Boolean = false
)

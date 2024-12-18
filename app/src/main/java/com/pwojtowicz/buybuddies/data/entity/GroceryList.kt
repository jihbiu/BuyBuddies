package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "grocery_lists",
    foreignKeys = [
        ForeignKey(
            entity = Home::class,
            parentColumns = ["id"],
            childColumns = ["homeId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["firebaseUid"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
)
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: String? = null,
    val homeId: Long? = null,
    val name: String = "",
    val description: String = "",
    val listStatus: String = GroceryListStatus.ACTIVE.name,
    var sortOrder: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: String = ""
)
enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE
}

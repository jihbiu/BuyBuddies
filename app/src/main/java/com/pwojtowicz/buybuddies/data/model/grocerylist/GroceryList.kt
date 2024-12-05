package com.pwojtowicz.buybuddies.data.model.grocerylist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.model.user.User

@Entity(tableName = "grocery_lists", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.SET_NULL
    )
])
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long? = null,
    val name: String,
    val createDate: String = "",
    val listStatus: String = GroceryListStatus.ACTIVE.name,
    var sortOrder: Int = 0
)
enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE
}

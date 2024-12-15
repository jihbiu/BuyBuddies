package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.pwojtowicz.buybuddies.data.enums.MemberRole

@Entity(
    tableName = "grocery_list_members",
    primaryKeys = ["groceryListId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["groceryListId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["firebaseUid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroceryListMember(
    val groceryListId: Long,
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER
)
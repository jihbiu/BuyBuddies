package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.entity.base.BaseEntity
import com.pwojtowicz.buybuddies.data.enums.MemberRole

@Entity(
    tableName = "grocery_list_members",
    primaryKeys = ["groceryListId", "memberId"],
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
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroceryListMember(
    val groceryListId: Long,
    val memberId: String,
//    val role: MemberRole = MemberRole.MEMBER,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = ""
) : BaseEntity
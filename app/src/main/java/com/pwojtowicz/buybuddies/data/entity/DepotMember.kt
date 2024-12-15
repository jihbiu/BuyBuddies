package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.pwojtowicz.buybuddies.data.enums.MemberRole


@Entity(
    tableName = "depot_members",
    primaryKeys = ["depotId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = Depot::class,
            parentColumns = ["id"],
            childColumns = ["depotId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DepotMember(
    val depotId: Long,
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER
)
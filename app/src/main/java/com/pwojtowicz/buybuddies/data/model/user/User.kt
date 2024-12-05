package com.pwojtowicz.buybuddies.data.model.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = UserAvatar::class,
            parentColumns = ["id"],
            childColumns = ["avatarId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["avatarId"])
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val avatarId: Long? = null,
    val username: String = "",
)
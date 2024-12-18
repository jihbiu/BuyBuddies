package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_avatars")
data class UserAvatar(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imgPath: String
)

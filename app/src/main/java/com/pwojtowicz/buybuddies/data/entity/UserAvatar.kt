package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.entity.base.BaseEntity

@Entity(tableName = "user_avatars")
data class UserAvatar(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imgPath: String,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = ""
) : BaseEntity
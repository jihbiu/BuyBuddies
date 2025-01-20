package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.entity.base.BaseDto

data class UserDTO(
    val id: Long?,
    val firebaseUid: String,
    val email: String,
    val name: String,
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto

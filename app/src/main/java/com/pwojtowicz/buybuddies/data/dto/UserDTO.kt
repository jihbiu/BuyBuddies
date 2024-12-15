package com.pwojtowicz.buybuddies.data.dto

data class UserDTO(
    val id: Long?,
    val firebaseUid: String,
    val email: String,
    val name: String
)

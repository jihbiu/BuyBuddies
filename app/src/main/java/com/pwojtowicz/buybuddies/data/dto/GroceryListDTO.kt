package com.pwojtowicz.buybuddies.data.dto

data class GroceryListDTO(
    val name: String,
    val description: String = "",
    val ownerId: String?,
    val status: String,
    val memberIds: Set<String> = emptySet(),
)

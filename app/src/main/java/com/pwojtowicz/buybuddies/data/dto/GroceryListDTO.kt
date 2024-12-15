package com.pwojtowicz.buybuddies.data.dto

data class GroceryListDTO(
    val id: Long,
    val name: String,
    val description: String,
    val ownerId: Long,
    val ownerName: String,
    val memberIds: List<String> = emptyList(),
)

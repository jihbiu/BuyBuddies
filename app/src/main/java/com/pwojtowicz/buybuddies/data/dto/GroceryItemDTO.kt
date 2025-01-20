package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.entity.base.BaseDto

data class GroceryItemDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val barcode: String?,
    val category: String,
    val unit: String,
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto

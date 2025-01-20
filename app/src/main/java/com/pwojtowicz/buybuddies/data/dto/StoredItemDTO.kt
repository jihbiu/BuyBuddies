package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.entity.base.BaseDto

data class StoredItemDTO(
    val id: Long?,
    val groceryItemName: String,
    val depotId: Long,
    val depotName: String,
    val quantity: Double,
    val unit: String,
    val expirationDate: String,
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto
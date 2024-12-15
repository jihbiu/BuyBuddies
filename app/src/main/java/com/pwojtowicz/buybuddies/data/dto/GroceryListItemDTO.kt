package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus


data class GroceryListItemDTO(
    val id: Long,
    val groceryListId: Long,
//    val groceryItemId: Long,
    val groceryItemName: String,
    val quantity: Double,
    val unit: String,
    val notes: String?,
    val status: PurchaseStatus,
)
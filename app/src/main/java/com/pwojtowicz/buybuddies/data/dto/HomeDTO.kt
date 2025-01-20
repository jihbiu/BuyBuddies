package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.entity.base.BaseDto

data class HomeDTO(
    val id: Long?,
    val name: String,
    val description: String?,
    val ownerId: Long,
    val membersIds: List<Long> = emptyList(),
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto
package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.entity.base.BaseDto

data class DepotDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val homeId: Long,
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto {

}

package com.pwojtowicz.buybuddies.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.entity.base.BaseEntity

@Entity(tableName = "depots")
data class Depot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val homeId: Long?,
    val ownerId: String,
    val barcodeNumber: String,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = ""
) : BaseEntity
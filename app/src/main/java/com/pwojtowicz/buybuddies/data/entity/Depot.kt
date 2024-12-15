package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "depots")
data class Depot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val homeId: Long?,
    val ownerId: String,
    val barcodeNumber: String,
    val updatedAt: Long = System.currentTimeMillis()
)
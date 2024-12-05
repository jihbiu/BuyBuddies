package com.pwojtowicz.buybuddies.data.model.depot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "depots")
data class Depot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcodeNumber: String,
)

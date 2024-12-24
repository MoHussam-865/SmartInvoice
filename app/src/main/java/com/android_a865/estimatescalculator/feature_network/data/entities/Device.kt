package com.android_a865.estimatescalculator.feature_network.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Devices")
data class Device(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val ipAddress: String,
    val role: Int,
)

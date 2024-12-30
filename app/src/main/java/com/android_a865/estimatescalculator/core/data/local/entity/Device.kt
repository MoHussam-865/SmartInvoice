package com.android_a865.estimatescalculator.core.data.local.entity

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

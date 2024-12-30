package com.android_a865.estimatescalculator.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientId: Int?,
    val client: String?,
    val type: String,
    val date: Long,
    val total: Double
)
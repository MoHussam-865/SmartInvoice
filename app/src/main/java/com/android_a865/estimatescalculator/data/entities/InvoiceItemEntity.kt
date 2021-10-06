package com.android_a865.estimatescalculator.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
* This entity maps the cross relation  between: (n to m)
*
*   Invoices, Items, Clients
*
*
* */

@Entity(
    tableName = "InvoiceItems",
    primaryKeys = ["invoiceId", "itemId"]
)
data class InvoiceItemEntity(
    val invoiceId: Int,
    val itemId: Int,
    val name: String,
    val qty: Double,
    val price: Double,
    val total: Double
)
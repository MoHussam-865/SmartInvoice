package com.android_a865.estimatescalculator.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.android_a865.estimatescalculator.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.data.entities.InvoiceItemEntity

// get all data for the invoice
data class FullInvoice(
    @Embedded
    val invoice: InvoiceEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId"
    )
    val items: List<InvoiceItemEntity>,

    )
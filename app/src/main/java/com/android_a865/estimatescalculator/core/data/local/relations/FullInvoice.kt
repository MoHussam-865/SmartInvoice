package com.android_a865.estimatescalculator.core.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceEntity
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceItemEntity

// get all data for the invoice
data class FullInvoice(
    @Embedded
    val invoice: InvoiceEntity,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: Client?,

    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId"
    )
    val items: List<InvoiceItemEntity>,

    )
package com.android_a865.estimatescalculator.feature_main.data.mapper


import com.android_a865.estimatescalculator.feature_main.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.feature_main.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.feature_main.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_main.data.relations.FullInvoice
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.utils.Path
import com.android_a865.estimatescalculator.utils.toJson
import com.android_a865.estimatescalculator.utils.toObject


// database entity
fun Item.toEntity() = ItemEntity(
    id = id,
    name = name,
    path = path.path,
    price = price,
    isFolder = isFolder
)


fun ItemEntity.toItem() = Item(
    id = id,
    name = name,
    path = Path(path),
    price = price,
    isFolder = isFolder
)


private fun ItemEntity.toInvoiceItem() = InvoiceItem(
    id = id,
    name = name,
    price = price,
    isFolder = isFolder
)


fun List<ItemEntity>.toInvoiceItems() = map { it.toInvoiceItem() }
fun List<ItemEntity>.toItems() = map { it.toItem() }
fun List<Item>.toEntities() = map { it.toEntity() }

fun InvoiceItemEntity.toInvoiceItem() = InvoiceItem(
    id = itemId,
    name = name,
    price = price,
    qty = qty,
)

fun InvoiceItem.toEntity(invoiceId: Int) = InvoiceItemEntity(
    invoiceId = invoiceId,
    itemId = id,
    name = name,
    qty = qty,
    price = price,
    total = total
)

fun FullInvoice.toInvoice() = Invoice(
    id = invoice.id,
    date = invoice.date,
    client = invoice.client?.toObject(),
    type = InvoiceTypes.valueOf(invoice.type),
    items = items.map { it.toInvoiceItem() }
)

fun Invoice.toEntity() = FullInvoice(
    invoice = InvoiceEntity(
        id = id,
        clientId = client?.id,
        client = client?.toJson(),
        type = type.name,
        date = date,
        total = items.sumOf { it.total }
    ),

    items = items.map { it.toEntity(id) }
)

fun List<FullInvoice>.toInvoices() = map { it.toInvoice() }




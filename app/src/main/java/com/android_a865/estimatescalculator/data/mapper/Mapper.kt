package com.android_a865.estimatescalculator.data.mapper


import com.android_a865.estimatescalculator.data.entities.ItemEntity
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.utils.Path


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


fun List<ItemEntity>.toInvoiceItems(): List<InvoiceItem> = map { it.toInvoiceItem() }
fun List<ItemEntity>.toItems() = map { it.toItem() }
fun List<Item>.toEntities() = map { it.toEntity() }

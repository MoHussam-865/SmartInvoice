package com.android_a865.estimatescalculator.data.mapper


import com.android_a865.estimatescalculator.domain.InvoiceItem
import com.android_a865.estimatescalculator.domain.Item
import com.android_a865.estimatescalculator.data.entities.ItemEntity
import com.android_a865.estimatescalculator.utils.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mapper @Inject constructor() {


    // database entity
    fun itemToEntity(item: Item) = item.run {
        ItemEntity(
            id = id,
            name = name,
            path = path.path,
            price = price,
            isFolder = isFolder
        )
    }

    fun itemFromEntity(item: ItemEntity) = item.run {
        Item(
            id = id,
            name = name,
            path = Path(path),
            price = price,
            isFolder = isFolder
        )
    }

    private fun invoiceItemFromEntity(item: ItemEntity) = item.run {
        InvoiceItem(
                id = id,
                name = name,
                price = price,
                isFolder = isFolder
        )
    }

    fun invoiceItemsFromEntities(items: List<ItemEntity>) = items.map { invoiceItemFromEntity(it) }
    fun itemsFromEntities(items: List<ItemEntity>) = items.map { itemFromEntity(it) }
    fun itemsToEntities(items: List<Item>) = items.map { itemToEntity(it) }
}
package com.android_a865.estimatescalculator.database.mapper


import com.android_a865.estimatescalculator.database.domain.Item
import com.android_a865.estimatescalculator.database.entities.ItemEntity
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
            qty = qty,
            value = value,
            value2 = value2,
            isFolder = isFolder
        )
    }

    fun itemFromEntity(item: ItemEntity) = item.run {
        Item(
            id = id,
            name = name,
            path = Path(path),
            qty = qty,
            value = value,
            value2 = value2,
            isFolder = isFolder
        )
    }

    fun itemsFromEntities(items: List<ItemEntity>) = items.map { itemFromEntity(it) }
    fun itemsToEntities(items: List<Item>) = items.map { itemToEntity(it) }
}
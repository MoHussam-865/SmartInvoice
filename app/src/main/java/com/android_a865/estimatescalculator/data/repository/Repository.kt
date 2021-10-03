package com.android_a865.estimatescalculator.data.repository

import com.android_a865.estimatescalculator.data.dao.ItemsDao
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.data.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        private val dao: ItemsDao,
        private val mapper: Mapper
) {

    fun getItems(path: String): Flow<List<Item>> = dao.getItemsEntity(path).map {
        mapper.itemsFromEntities(it)
    }

    suspend fun insertItem(item: Item) = dao.insertItemEntity(mapper.itemToEntity(item))

    suspend fun updateItem(item: Item) = dao.updateItem(mapper.itemToEntity(item))

    suspend fun deleteItems(items: List<Item>) {
        dao.deleteItems(
                mapper.itemsToEntities(items)
        )
    }

    fun getInvoiceItems(path: String): Flow<List<InvoiceItem>> = dao.getItemsEntity(path).map {
        mapper.invoiceItemsFromEntities(it)
    }

    suspend fun getItemById(id: Int): Item = mapper.itemFromEntity(dao.getItemByID(id))

}
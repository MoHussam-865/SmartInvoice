package com.android_a865.estimatescalculator.data.repository

import com.android_a865.estimatescalculator.data.dao.ItemsDao
import com.android_a865.estimatescalculator.data.entities.ItemEntity
import com.android_a865.estimatescalculator.data.mapper.*
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepositoryImpl @Inject constructor(
    private val dao: ItemsDao,
): ItemsRepository {

    override fun getItems(path: String) = dao.getItemsEntity(path)

    override suspend fun getItemById(id: Int) = dao.getItemByID(id)

    override suspend fun insertItem(item: ItemEntity) = dao.insertItemEntity(item)

    override suspend fun updateItem(item: ItemEntity) = dao.updateItem(item)

    override suspend fun deleteItems(items: List<ItemEntity>) = dao.deleteItems(items)

}
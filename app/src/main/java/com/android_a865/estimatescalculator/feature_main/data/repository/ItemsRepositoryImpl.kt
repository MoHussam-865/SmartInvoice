package com.android_a865.estimatescalculator.feature_main.data.repository

import com.android_a865.estimatescalculator.feature_main.data.dao.ItemsDao
import com.android_a865.estimatescalculator.feature_main.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_main.domain.repository.ItemsRepository
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
package com.android_a865.estimatescalculator.domain.repository

import com.android_a865.estimatescalculator.data.entities.ItemEntity
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItems(path: String): Flow<List<ItemEntity>>

    suspend fun getItemById(id: Int): ItemEntity

    suspend fun insertItem(item: ItemEntity)

    suspend fun updateItem(item: ItemEntity)

    suspend fun deleteItems(items: List<ItemEntity>)

}
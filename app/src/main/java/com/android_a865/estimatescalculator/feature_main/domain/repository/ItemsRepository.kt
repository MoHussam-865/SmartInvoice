package com.android_a865.estimatescalculator.feature_main.domain.repository

import com.android_a865.estimatescalculator.feature_main.data.entities.ItemEntity
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItems(path: String): Flow<List<ItemEntity>>

    suspend fun getItemById(id: Int): ItemEntity

    suspend fun insertItem(item: ItemEntity): Long

    suspend fun updateItem(item: ItemEntity)

    suspend fun deleteItems(items: List<ItemEntity>)

}
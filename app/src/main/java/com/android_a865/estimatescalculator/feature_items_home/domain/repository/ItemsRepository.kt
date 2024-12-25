package com.android_a865.estimatescalculator.feature_items_home.domain.repository

import com.android_a865.estimatescalculator.feature_items_home.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItems(path: String): Flow<List<ItemEntity>>

    suspend fun getFolderSubItems(path: String): List<ItemEntity>

    suspend fun getItemFriends(path: String): List<ItemEntity>

    suspend fun getItemById(id: Int): ItemEntity

    suspend fun insertItem(item: ItemEntity): Long

    suspend fun getAllowedName(item: Item): String

    suspend fun updateItem(item: ItemEntity)

    suspend fun deleteItems(items: List<ItemEntity>)

}
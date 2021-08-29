package com.android_a865.estimatescalculator.database

import com.android_a865.estimatescalculator.database.domain.Item
import com.android_a865.estimatescalculator.database.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        db: MyRoomDatabase,
        private val mapper: Mapper
) {
    private val dao = db.getItemsDao()

    fun getItems(path: String): Flow<List<Item>> = dao.getItemsEntity(path).map {
        mapper.itemsFromEntities(it)
    }

    suspend fun insertItem(item: Item) = dao.insertItemEntity(mapper.itemToEntity(item))

    suspend fun updateItem(item: Item) = dao.updateItemEntity(mapper.itemToEntity(item))

    suspend fun deleteItems(items: List<Item>) {
        dao.deleteItems(
            mapper.itemsToEntities(items)
        )
    }

}
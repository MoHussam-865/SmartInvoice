package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toEntity
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Item
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository

class AddItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item): Int {
        //val name = repository.getAllowedName(item)
        val itemId = repository.insertItem(item.toEntity())
        return itemId.toInt()
    }
}
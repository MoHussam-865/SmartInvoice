package com.android_a865.estimatescalculator.feature_main.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_main.data.mapper.toEntity
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.feature_main.domain.repository.ItemsRepository

class AddItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item): Int {
        val itemId = repository.insertItem(item.toEntity())
        return itemId.toInt()
    }
}
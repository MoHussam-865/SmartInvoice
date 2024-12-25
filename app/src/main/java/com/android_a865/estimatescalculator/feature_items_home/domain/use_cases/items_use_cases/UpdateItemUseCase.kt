package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toEntity
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Item
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository

class UpdateItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item) {
        repository.updateItem(item.toEntity())
    }
}
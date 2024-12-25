package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toEntities
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Item
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository

class DeleteItemsUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(items: List<Item>) {
        repository.deleteItems(items.toEntities())
    }
}
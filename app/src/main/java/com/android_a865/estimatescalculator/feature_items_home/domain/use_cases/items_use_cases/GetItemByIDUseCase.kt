package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toItem
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Item
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository

class GetItemByIDUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(id: Int): Item {
        return repository.getItemById(id).toItem()
    }
}
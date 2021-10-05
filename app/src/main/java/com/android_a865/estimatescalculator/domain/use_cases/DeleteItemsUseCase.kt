package com.android_a865.estimatescalculator.domain.use_cases

import com.android_a865.estimatescalculator.data.mapper.toEntities
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository

class DeleteItemsUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(items: List<Item>) {
        repository.deleteItems(items.toEntities())
    }
}
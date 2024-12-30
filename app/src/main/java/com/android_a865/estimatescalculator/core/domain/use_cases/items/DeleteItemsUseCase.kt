package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntities
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item

class DeleteItemsUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(items: List<Item>) {
        repository.deleteItems(items.toEntities())
    }
}
package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item

class UpdateItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item) {
        repository.updateItem(item.toEntity())
    }
}
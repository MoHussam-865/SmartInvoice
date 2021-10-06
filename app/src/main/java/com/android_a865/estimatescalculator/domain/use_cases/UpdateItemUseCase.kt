package com.android_a865.estimatescalculator.domain.use_cases

import com.android_a865.estimatescalculator.data.mapper.toEntity
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository

class UpdateItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item) {
        repository.updateItem(item.toEntity())
    }
}
package com.android_a865.estimatescalculator.domain.use_cases

import com.android_a865.estimatescalculator.data.mapper.toEntity
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository

class AddItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item) {


        repository.insertItem(item.toEntity())
    }
}
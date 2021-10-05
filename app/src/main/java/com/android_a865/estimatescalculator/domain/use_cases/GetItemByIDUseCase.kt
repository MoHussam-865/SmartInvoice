package com.android_a865.estimatescalculator.domain.use_cases

import com.android_a865.estimatescalculator.data.mapper.toItem
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository

class GetItemByIDUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(id: Int): Item {
        return repository.getItemById(id).toItem()
    }
}
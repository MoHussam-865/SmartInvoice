package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toItem
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item

class GetItemByIDUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(id: Int): Item {
        return repository.getItemById(id).toItem()
    }
}
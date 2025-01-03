package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.entity.Item
import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository

class AddItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item): Int {


        val itemId = repository.insertItem(item.toEntity())
        return itemId.toInt()

    }
}
package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item

class AddItemUseCase(
    val repository: ItemsRepository
) {
    suspend operator fun invoke(item: Item): Int {
        //val name = repository.getAllowedName(item)
        val itemId = repository.insertItem(item.toEntity())
        return itemId.toInt()
    }
}
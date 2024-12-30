package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toItems
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetItemsUseCase(
    val repository: ItemsRepository
) {

    operator fun invoke(path: String): Flow<List<Item>> {
        return repository.getItems(path).map {
            it.toItems()
        }
    }

}
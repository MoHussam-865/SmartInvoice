package com.android_a865.estimatescalculator.domain.use_cases.items_use_cases

import android.content.ClipData
import com.android_a865.estimatescalculator.data.mapper.toItems
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository
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
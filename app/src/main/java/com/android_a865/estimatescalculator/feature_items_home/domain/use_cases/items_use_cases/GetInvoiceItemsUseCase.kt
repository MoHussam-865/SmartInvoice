package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toInvoiceItems
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetInvoiceItemsUseCase(
    val repository: ItemsRepository
) {
    operator fun invoke(path: String): Flow<List<InvoiceItem>> {
        return repository.getItems(path).map {
            it.toInvoiceItems()
        }
    }
}
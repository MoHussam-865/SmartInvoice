package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toInvoiceItems
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem
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
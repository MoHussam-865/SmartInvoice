package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem

class ApplyDiscountUseCase(
    val repository: ItemsRepository
) {

    // returns invoiceItem friends to apply discount
    suspend operator fun invoke(item: InvoiceItem): List<Int> {
        val myItem = repository.getItemById(item.id)

         return repository.getItemFriends(myItem.path)
             .filter { it.id != item.id && !it.isFolder}
             .map { it.id }
    }
}
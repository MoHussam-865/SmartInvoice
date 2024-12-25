package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.invoice_use_cases

import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository

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
package com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases

import com.android_a865.estimatescalculator.feature_main.data.mapper.toInvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.utils.Path

class ApplyDiscountUseCase(
    val repository: ItemsRepository
) {

    // returns invoiceItem friends to apply discount
    suspend operator fun invoke(item: InvoiceItem): List<InvoiceItem> {
        val myItem = repository.getItemById(item.id)

         return repository.getItemFriends(myItem.path)
             .map { it.toInvoiceItem() }
             .filter { it.id == item.id }
    }
}
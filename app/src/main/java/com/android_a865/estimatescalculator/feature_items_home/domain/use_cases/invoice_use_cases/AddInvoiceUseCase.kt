package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.invoice_use_cases

import com.android_a865.estimatescalculator.feature_items_home.data.mapper.toEntity
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.InvoiceRepository

class AddInvoiceUseCase(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoice: Invoice) {
        repository.insertInvoice(invoice.toEntity())
    }
}
package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice

class AddInvoiceUseCase(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoice: Invoice) {

        repository.insertInvoice(invoice.toEntity())

    }
}
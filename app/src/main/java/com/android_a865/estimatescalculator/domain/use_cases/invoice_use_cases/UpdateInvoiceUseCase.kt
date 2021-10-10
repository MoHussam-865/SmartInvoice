package com.android_a865.estimatescalculator.domain.use_cases.invoice_use_cases

import com.android_a865.estimatescalculator.data.mapper.toEntity
import com.android_a865.estimatescalculator.domain.model.Invoice
import com.android_a865.estimatescalculator.domain.repository.InvoiceRepository

class UpdateInvoiceUseCase(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoice: Invoice) {
        repository.updateInvoice(invoice.toEntity())
    }
}
package com.android_a865.estimatescalculator.domain.use_cases.invoice_use_cases

import com.android_a865.estimatescalculator.data.mapper.toInvoices
import com.android_a865.estimatescalculator.domain.model.Invoice
import com.android_a865.estimatescalculator.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetInvoicesUseCase(
    private val repository: InvoiceRepository
) {
    operator fun invoke(): Flow<List<Invoice>> {
        return repository.getInvoices().map {
            it.toInvoices()
        }
    }
}
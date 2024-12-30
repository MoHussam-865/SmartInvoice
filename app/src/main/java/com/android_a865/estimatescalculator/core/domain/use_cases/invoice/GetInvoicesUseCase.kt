package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

import com.android_a865.estimatescalculator.core.data.local.mapper.toInvoices
import com.android_a865.estimatescalculator.core.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.core.enu.FilterOptions
import com.android_a865.estimatescalculator.core.enu.InvoiceTypes
import com.android_a865.estimatescalculator.core.utils.exhaustive
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetInvoicesUseCase(
    private val repository: InvoiceRepository
) {
    operator fun invoke(filter: FilterOptions = FilterOptions.All): Flow<List<Invoice>> {

        var invoices = repository.getInvoices().map {
            it.toInvoices()
        }

        when (filter) {
            FilterOptions.All -> { }
            FilterOptions.Draft -> {
                invoices = invoices.map { invoices0 ->
                    invoices0.filter { invoice ->
                        invoice.type == InvoiceTypes.Draft
                    }

                }
            }
            FilterOptions.Estimate -> {
                invoices = invoices.map { invoices0 ->
                    invoices0.filter { invoice ->
                        invoice.type == InvoiceTypes.Estimate
                    }

                }
            }
            FilterOptions.Invoice -> {
                invoices = invoices.map { invoices0 ->
                    invoices0.filter { invoice ->
                        invoice.type == InvoiceTypes.Invoice
                    }

                }
            }
        }.exhaustive

        return invoices
    }
}
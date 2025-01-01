package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

import com.android_a865.estimatescalculator.core.data.local.mapper.toInvoices
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.core.enu.FilterOptions
import com.android_a865.estimatescalculator.core.enu.InvoiceTypes
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.core.utils.exhaustive
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GetInvoicesUseCase(
    private val repository: InvoiceRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {
    operator fun invoke(filter: FilterOptions = FilterOptions.All): Flow<List<Invoice>> {

        CoroutineScope(Dispatchers.IO).launch {
            val role = settings.getAppSettings().first().myRole
            if (role == Role.Client) {
                val items = clientApiRepository.getInvoices(1)
                items.forEach {
                    repository.insertInvoice(it)
                }
            }
        }

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
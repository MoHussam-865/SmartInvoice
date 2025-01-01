package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class AddInvoiceUseCase(
    private val repository: InvoiceRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {
    suspend operator fun invoke(invoice: Invoice) {
        val role = settings.getAppSettings().first().myRole

        if (role == Role.Client) {
            clientApiRepository.sendData(invoice.toEntity())
        } else {
            repository.insertInvoice(invoice.toEntity())
        }
    }
}
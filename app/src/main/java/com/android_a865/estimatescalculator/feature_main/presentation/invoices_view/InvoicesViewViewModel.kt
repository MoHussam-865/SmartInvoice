package com.android_a865.estimatescalculator.feature_main.presentation.invoices_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.InvoiceUseCases
import com.android_a865.estimatescalculator.feature_settings.domain.models.AppSettings
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoicesViewViewModel @Inject constructor(
    invoiceUseCases: InvoiceUseCases,
    private val repository: SettingsRepository
): ViewModel() {

    val invoices = invoiceUseCases.getInvoices()

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            eventsChannel.send(
                WindowEvents.SetAppSettings(
                    repository.getAppSettings().first()
                )
            )
        }
    }



    fun onInvoiceClicked(invoice: Invoice) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.OpenInvoice(invoice))
    }

    fun onNewInvoiceClicked() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.OpenInvoice(null))
    }

    sealed class WindowEvents {
        data class OpenInvoice(val invoice: Invoice?): WindowEvents()
        data class SetAppSettings(val appSettings: AppSettings): WindowEvents()
    }


}
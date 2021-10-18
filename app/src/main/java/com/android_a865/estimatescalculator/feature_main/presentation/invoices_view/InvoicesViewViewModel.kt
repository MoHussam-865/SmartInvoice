package com.android_a865.estimatescalculator.feature_main.presentation.invoices_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.InvoiceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoicesViewViewModel @Inject constructor(
    invoiceUseCases: InvoiceUseCases
): ViewModel() {

    val invoices = invoiceUseCases.getInvoices()
    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()



    fun onInvoiceClicked(invoice: Invoice) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.OpenInvoice(invoice))
    }

    fun onNewInvoiceClicked() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.OpenInvoice(null))
    }

    sealed class WindowEvents {
        data class OpenInvoice(val invoice: Invoice?): WindowEvents()
    }


}
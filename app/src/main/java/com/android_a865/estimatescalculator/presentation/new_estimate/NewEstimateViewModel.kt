package com.android_a865.estimatescalculator.presentation.new_estimate

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.common.PdfMaker
import com.android_a865.estimatescalculator.domain.model.Invoice
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.domain.use_cases.invoice_use_cases.InvoiceUseCases
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
    private val invoiceUseCases: InvoiceUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val invoice = state.get<Invoice>("invoice")
    val itemsFlow = MutableStateFlow(invoice?.items ?: listOf())

    @ExperimentalCoroutinesApi
    val totalFlow = itemsFlow.flatMapLatest { items ->
        flowOf(items.sumOf { it.total })
    }


    private val eventsChannel = Channel<InvoiceWindowEvents>()
    val invoiceWindowEvents = eventsChannel.receiveAsFlow()


    fun onItemRemoveClicked(item: InvoiceItem) = itemsFlow.update {
        it.removeAllOf(item)
    }

    fun onOneItemAdded(item: InvoiceItem) = itemsFlow.update {
        it.addOneOf(item)
    }

    fun onOneItemRemoved(item: InvoiceItem) = itemsFlow.update {
        it.removeOneOf(item)
    }


    fun onItemQtyChanged(item: InvoiceItem, qty: String) = itemsFlow.update {
        it.setQtyTo(item, qty.double(1.0))
    }

    fun onItemsSelected(chosenItems: List<InvoiceItem>?) = viewModelScope.launch {
        chosenItems?.let { items ->
            // this delay is to solve an unexpected bug
            delay(100)
            itemsFlow.update { items }
        }
    }

    fun onOpenPdfClicked(context: Context?) {

        if (itemsFlow.value.isEmpty()) {
            showInvalidMessage("Add Items first to your invoice")
        } else {
            viewModelScope.launch {
                context?.let {
                    val fileName: String? =
                        PdfMaker().make(context, Invoice(items = itemsFlow.value))

                    fileName?.let {
                        eventsChannel.send(InvoiceWindowEvents.OpenPdf(it))
                    }
                }

            }
        }
    }

    fun onSaveClicked() {
        if (itemsFlow.value.isEmpty()) {
            showInvalidMessage("Add Items first")
        } else {
            viewModelScope.launch {
                invoiceUseCases.addInvoice(Invoice(items = itemsFlow.value))
                eventsChannel.send(InvoiceWindowEvents.NavigateBack)
            }
        }
    }

    private fun showInvalidMessage(message: String) = viewModelScope.launch {
        eventsChannel.send(InvoiceWindowEvents.ShowMessage(message))
    }


    sealed class InvoiceWindowEvents {
        data class OpenPdf(val fileName: String) : InvoiceWindowEvents()
        data class ShowMessage(val message: String) : InvoiceWindowEvents()
        object NavigateBack : InvoiceWindowEvents()
    }
}
package com.android_a865.estimatescalculator.feature_main.presentation.new_estimate

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.InvoiceUseCases
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

    var invoiceType = MutableStateFlow(invoice?.type ?: InvoiceTypes.Estimate)
    var invoiceClient = invoice?.client
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

    fun onOpenPdfClicked() {

        if (itemsFlow.value.isEmpty()) {
            showInvalidMessage("Add Items first to your invoice")
        } else {
            viewModelScope.launch {

                val invoice = getInvoice()
                eventsChannel.send(InvoiceWindowEvents.OpenPdf(invoice))
            }
        }
    }

    fun onSaveClicked() {
        if (itemsFlow.value.isEmpty()) {
            showInvalidMessage("Add Items first")
        } else {
            viewModelScope.launch {

                val myInvoice = getInvoice()
                if (invoice != null) {
                    invoiceUseCases.updateInvoice(myInvoice)
                } else {
                    invoiceUseCases.addInvoice(myInvoice)
                }

                eventsChannel.send(
                    InvoiceWindowEvents.NavigateBack(
                        "${invoiceType.value.name} Saved"
                    )
                )
            }
        }
    }

    private fun getInvoice(): Invoice {
        return invoice?.copy(
            type = invoiceType.value,
            client = invoiceClient,
            items = itemsFlow.value
        ) ?: Invoice(
            type = invoiceType.value,
            client = invoiceClient,
            items = itemsFlow.value
        )
    }

    private fun showInvalidMessage(message: String) = viewModelScope.launch {
        eventsChannel.send(InvoiceWindowEvents.ShowMessage(message))
    }

    fun onInvoiceTypeSelected(context: Context) {

        val types = InvoiceTypes.values().map { it.name }.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Choose Type")
            .setSingleChoiceItems(types, invoiceType.value.ordinal,
                DialogInterface.OnClickListener { dialog, i ->
                    invoiceType.value = InvoiceTypes.valueOf(types[i])
                    dialog.dismiss()
                }
            ).show()
    }


    sealed class InvoiceWindowEvents {
        data class OpenPdf(val invoice: Invoice) : InvoiceWindowEvents()
        data class ShowMessage(val message: String) : InvoiceWindowEvents()
        data class NavigateBack(val message: String) : InvoiceWindowEvents()
    }
}
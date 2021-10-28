package com.android_a865.estimatescalculator.feature_main.presentation.new_estimate

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_client.domain.use_cases.ClientsUseCases
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.InvoiceUseCases
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
    private val invoiceUseCases: InvoiceUseCases,
    private val clientsUseCases: ClientsUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val invoice = state.get<Invoice>("invoice")

    val invoiceType = MutableStateFlow(invoice?.type ?: InvoiceTypes.Estimate)
    private val clientId = MutableStateFlow(invoice?.client?.id ?: 0)
    @ExperimentalCoroutinesApi
    private val invoiceClient = clientId.flatMapLatest {
        if (it != 0) {
            clientsUseCases.getClient(it).map { thisClient ->
                thisClient ?: invoice?.client
            }
        } else {
            flowOf(null)
        }
    }

    @ExperimentalCoroutinesApi
    val client = invoiceClient.asLiveData()

    val itemsFlow = MutableStateFlow(invoice?.items ?: listOf())

    @ExperimentalCoroutinesApi
    val totalFlow = itemsFlow.flatMapLatest { items ->
        flowOf(items.sumOf { it.total })
    }


    private val eventsChannel = Channel<WindowEvents>()
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

    @ExperimentalCoroutinesApi
    fun onOpenPdfClicked() {

        if (itemsFlow.value.isEmpty()) {
            showInvalidMessage("Add Items first to your invoice")
        } else {
            viewModelScope.launch {

                val invoice = getInvoice()
                eventsChannel.send(WindowEvents.OpenPdf(invoice))
            }
        }
    }

    @ExperimentalCoroutinesApi
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
                    WindowEvents.NavigateBack(
                        "${invoiceType.value.name} Saved"
                    )
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getInvoice(): Invoice {
        return invoice?.copy(
            type = invoiceType.value,
            client = client.value,
            items = itemsFlow.value
        ) ?: Invoice(
            type = invoiceType.value,
            client = client.value,
            items = itemsFlow.value
        )
    }

    private fun showInvalidMessage(message: String) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.ShowMessage(message))
    }

    fun onInvoiceTypeSelected(context: Context) {

        val types = InvoiceTypes.values().map { it.name }.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Choose Type")
            .setSingleChoiceItems(types, invoiceType.value.ordinal) { dialog, i ->
                invoiceType.value = InvoiceTypes.valueOf(types[i])
                dialog.dismiss()
            }.show()
    }

    fun onClientChosen(client: Client?) {
        client?.let {
            clientId.value = it.id
        }
    }

    @ExperimentalCoroutinesApi
    fun onViewClientClicked() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            NewEstimateFragmentDirections.actionNewEstimateFragmentToClientViewFragment(
                client.value!!
            )
        ))
    }

    fun onChooseClientSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            NewEstimateFragmentDirections.actionNewEstimateFragmentToChooseClientFragment()
        ))
    }


    sealed class WindowEvents {
        data class OpenPdf(val invoice: Invoice) : WindowEvents()
        data class ShowMessage(val message: String) : WindowEvents()
        data class NavigateBack(val message: String) : WindowEvents()
        data class Navigate(val direction: NavDirections): WindowEvents()
    }
}
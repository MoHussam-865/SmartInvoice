package com.android_a865.estimatescalculator.features.new_estimate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android_a865.estimatescalculator.database.domain.Invoice
import com.android_a865.estimatescalculator.database.domain.InvoiceItem
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
        private val state: SavedStateHandle
): ViewModel() {

    private val items = state.get<List<InvoiceItem>>("choose_invoice_items")
    private val invoice = state.get<Invoice>("invoice")

    private val date = MutableStateFlow(invoice?.date ?: currentDate())

    val itemsFlow = MutableStateFlow(invoice?.items ?: listOf())

    private val totalFlow = itemsFlow.flatMapLatest { items ->
        state.set(
                "invoice",
                Invoice(
                        date = date.value,
                        items = items
                )
        )
        flowOf(items.sumOf { it.total })
    }
    val total = totalFlow.asLiveData()

    private val windowEventsChannel = Channel<InvoiceWindowEvents>()
    val windowEvents = windowEventsChannel.receiveAsFlow()



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
        it.setQtyTo(item, qty.toDouble())
    }

    fun onItemsSelected(chosenItems: List<InvoiceItem>?) = chosenItems?.let { items ->
        itemsFlow.update { items }
    }


    sealed class InvoiceWindowEvents {}
}
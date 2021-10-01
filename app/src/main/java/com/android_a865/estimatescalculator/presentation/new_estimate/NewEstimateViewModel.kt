package com.android_a865.estimatescalculator.presentation.new_estimate

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.android_a865.estimatescalculator.domain.Invoice
import com.android_a865.estimatescalculator.domain.InvoiceItem
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
        state: SavedStateHandle
): ViewModel() {

    private val invoice = state.get<Invoice>("invoice")

    private val date = MutableStateFlow(invoice?.date ?: currentDate())

    val itemsFlow = MutableStateFlow( invoice?.items ?: listOf())

    @ExperimentalCoroutinesApi
    val total = itemsFlow.flatMapLatest { items ->
        Log.d("NewEstimateViewModel", "items flow was changed ${items.size}  ")
        flowOf ( items.sumOf { it.total } )
    }




    fun onItemRemoveClicked(item: InvoiceItem) = itemsFlow.update { it.removeAllOf(item) }

    fun onOneItemAdded(item: InvoiceItem) = itemsFlow.update { it.addOneOf(item) }

    fun onOneItemRemoved(item: InvoiceItem) = itemsFlow.update { it.removeOneOf(item) }

    fun onItemQtyChanged(item: InvoiceItem, qty: String) = itemsFlow.update { it.setQtyTo(item, qty.toDouble()) }

    fun onItemsSelected(chosenItems: List<InvoiceItem>?) = chosenItems?.let { items ->
        itemsFlow.update { listOf() }
        itemsFlow.update { items }
    }

    fun saveInvoice() {
        Invoice(date = date.value, items = itemsFlow.value)
    }
}
package com.android_a865.estimatescalculator.presentation.new_estimate

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.domain.model.Invoice
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    private val invoice = state.get<Invoice>("invoice")
    val itemsFlow = MutableStateFlow( invoice?.items ?: listOf())

    @ExperimentalCoroutinesApi
    val totalFlow = itemsFlow.flatMapLatest { items ->

        items.forEach {
            Log.d("NewEstimateViewModel", "${it.name} ${it.qty}")
        }
        Log.d("NewEstimateViewModel", "the size = ${items.size}")

        flowOf(items.sumOf { it.total })
    }


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

    fun onItemsSelected(chosenItems: List<InvoiceItem>?) = viewModelScope.launch {
        chosenItems?.let { items ->
            // this delay is to solve an unexpected bug
            delay(100)
            itemsFlow.update { items }
        }
    }


}
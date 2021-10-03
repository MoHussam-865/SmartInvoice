package com.android_a865.estimatescalculator.features.new_estimate

import androidx.lifecycle.ViewModel
import com.android_a865.estimatescalculator.database.domain.InvoiceItem
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class NewEstimateViewModel @Inject constructor(
    //private val state: SavedStateHandle
) : ViewModel() {

    //private val invoice = state.get<Invoice>("invoice")
    val itemsFlow = MutableStateFlow(listOf<InvoiceItem>())

    @ExperimentalCoroutinesApi
    val totalFlow = itemsFlow.flatMapLatest { items ->
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

    fun onItemsSelected(chosenItems: List<InvoiceItem>?) = chosenItems?.let { items ->
        itemsFlow.update { items }
    }


}
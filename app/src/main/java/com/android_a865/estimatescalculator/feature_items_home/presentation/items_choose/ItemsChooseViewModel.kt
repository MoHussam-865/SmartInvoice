package com.android_a865.estimatescalculator.feature_items_home.presentation.items_choose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.core.domain.use_cases.items.ItemsUseCases
import com.android_a865.estimatescalculator.core.utils.Path
import com.android_a865.estimatescalculator.core.utils.addOf
import com.android_a865.estimatescalculator.core.utils.addOneOf
import com.android_a865.estimatescalculator.core.utils.include
import com.android_a865.estimatescalculator.core.utils.removeAllOf
import com.android_a865.estimatescalculator.core.utils.removeOneOf
import com.android_a865.estimatescalculator.core.utils.setQtyTo
import com.android_a865.estimatescalculator.core.utils.update0
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.core.data.local.entity.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsChooseViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    state: SavedStateHandle
) : ViewModel() {

    val currentPath = MutableStateFlow(state.get<Path>("path") ?: Path())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val itemsFlow = currentPath.flatMapLatest { path ->
        itemsUseCases.getInvoiceItems(path.path)
    }

    val selectedItems = MutableLiveData(
        state.get<Array<InvoiceItem>>("items")?.toList() ?: listOf()
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsData = combine(itemsFlow, selectedItems.asFlow()) { items, selected ->
        Pair(items, selected)
    }.flatMapLatest { (items, selected) ->
        flowOf(items.include(selected))
    }

    // Handle variables

    private val itemsWindowEventsChannel = Channel<ItemsWindowEvents>()
    val itemsWindowEvents = itemsWindowEventsChannel.receiveAsFlow()


    fun onBackPress() {
        if (currentPath.value.isRoot) goBack()
        else currentPath.value = currentPath.value.back()
    }

    private fun goBack() = viewModelScope.launch {
        itemsWindowEventsChannel.send(ItemsWindowEvents.GoBack)
    }

    fun onItemClicked(item: InvoiceItem) {
        if (item.isFolder) {
            viewModelScope.launch {
                val myItem: Item = itemsUseCases.getItemByID(item.id)
                currentPath.value = myItem.open()
            }
        }
    }

    fun onAddItemClicked(item: InvoiceItem) = selectedItems.update0 { it?.addOneOf(item) }

    fun onMinusItemClicked(item: InvoiceItem) = selectedItems.update0 { it?.removeOneOf(item) }

    fun onItemRemoveClicked(item: InvoiceItem) = selectedItems.update0 { it?.removeAllOf(item) }

    fun onQtySet(item: InvoiceItem, myQty: Double) {
        if (myQty > 0) {
            selectedItems.update0 {
                it?.setQtyTo(item, myQty)
            }
        } else {
            selectedItems.update0 { it?.removeAllOf(item) }
            viewModelScope.launch {
                showInvalidInputMessage("Quantity can't be less than 0")
            }
        }
    }

    fun onFabClicked() = viewModelScope.launch {
        itemsWindowEventsChannel.send(
            ItemsWindowEvents.NavigateTo(
                ItemsChooseFragmentDirections.actionItemsChooseFragmentToAddEditInvoiceItemFragment(
                    path = currentPath.value
                )
            )
        )
    }

    fun onInvoiceItemAdded(item: InvoiceItem) = selectedItems.update0 { it?.addOf(item) }

    private suspend fun showInvalidInputMessage(str: String) {
        itemsWindowEventsChannel.send(
            ItemsWindowEvents.InvalidInput(str)
        )
    }

    sealed class ItemsWindowEvents {
        data class NavigateTo(val direction: NavDirections) : ItemsWindowEvents()
        object GoBack : ItemsWindowEvents()
        data class InvalidInput(val msg: String): ItemsWindowEvents()
    }
}
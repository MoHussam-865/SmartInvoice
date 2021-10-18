package com.android_a865.estimatescalculator.feature_main.presentation.items_choose

import androidx.lifecycle.*
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.items_use_cases.ItemsUseCases
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsChooseViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    state: SavedStateHandle
) : ViewModel() {

    val currentPath = MutableStateFlow(state.get<Path>("path") ?: Path())

    @ExperimentalCoroutinesApi
    private val itemsFlow = currentPath.flatMapLatest { path ->
        itemsUseCases.getInvoiceItems(path.path)
    }

    val selectedItems = MutableLiveData(
        state.get<Array<InvoiceItem>>("items")?.toList() ?: listOf()
    )
    @ExperimentalCoroutinesApi
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
        else currentPath.update { it.back() }
    }

    private fun goBack() = viewModelScope.launch {
        itemsWindowEventsChannel.send(ItemsWindowEvents.GoBack)
    }

    fun onItemClicked(item: InvoiceItem) {
        if (item.isFolder) {
            viewModelScope.launch {
                val myItem: Item = itemsUseCases.getItemByID(item.id)
                currentPath.update { myItem.open() }
            }
        }
    }

    fun onAddItemClicked(item: InvoiceItem) = selectedItems.update { it?.addOneOf(item) }

    fun onMinusItemClicked(item: InvoiceItem) = selectedItems.update { it?.removeOneOf(item) }

    fun onItemRemoveClicked(item: InvoiceItem) = selectedItems.update { it?.removeAllOf(item) }

    fun onQtySet(item: InvoiceItem, myQty: Double) =
        selectedItems.update { it?.setQtyTo(item, myQty) }


    sealed class ItemsWindowEvents {
        //data class NavigateTo(val direction: NavDirections) : ItemsWindowEvents()
        object GoBack : ItemsWindowEvents()
    }
}
package com.android_a865.estimatescalculator.feature_main.presentation.selecting

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.items_use_cases.ItemsUseCases
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsSelectViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    state: SavedStateHandle
) : ViewModel() {

    val currentPath = state.get<Path>("path") ?: Path()
    private val itemId = state.get<Int>("id") ?: 0


    val itemsData = MutableLiveData(listOf<Item>())

    val all get() = itemsData.value.orEmpty().size
    @ExperimentalCoroutinesApi
    var numSelected = itemsData.asFlow().flatMapLatest {
        flowOf(it.numSelected)
    }


    private val itemsWindowEventsChannel = Channel<ItemsWindowEvents>()
    val itemsWindowEvents = itemsWindowEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            itemsUseCases.getItems(currentPath.path ).collect { items ->
                itemsData.value = items.selectOnlyWhere { itemId == it.id }
            }
        }
    }


    fun onSelectAllChanged(b: Boolean) = itemsData.update { it.selectAll(b) }


    fun onDeleteOptionSelected(context: Context) {
        val items = itemsData.value.filterSelected()
        // TODO get the total number affected by this
        AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.delete_items))
            .setMessage(String.format(context.resources.getString(R.string.delete_items_dialog),items.size))
            .setPositiveButton(context.resources.getString(R.string.delete)) { dialog, _ ->
                deleteSelected(items)
                dialog.dismiss()
            }
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }


    private fun deleteSelected(items: List<Item>) = viewModelScope.launch {
        itemsUseCases.deleteItems(items)
        itemsWindowEventsChannel.send(ItemsWindowEvents.NavigateBack)

    }

    fun onItemClicked(item: Item, position: Int) {
        item.changeSelection()
        itemsData.update { it }
        notifyAdapter(position)
    }

    fun onSelectChanged(item: Item, position: Int, b: Boolean) {
        if (item.isSelected != b) {
            item.isSelected = b
            itemsData.update { it }
            notifyAdapter(position)
        }
    }

    fun onEditOptionSelected() = itemsData.value.filterSelected().apply {

        if (this.size == 1) {
            val item = this[0]

            val action = if (item.isFolder) {

                SelectingFragmentDirections.actionSelectingFragmentToNewFolderFragment(
                    folder = item,
                    path = currentPath
                )

            } else {
                SelectingFragmentDirections.actionSelectingFragmentToNewItemFragment(
                    item = item,
                    path = currentPath
                )
            }

            navigate(action)
        }
    }


    private fun navigate(directions: NavDirections) = viewModelScope.launch {
        itemsWindowEventsChannel.send(ItemsWindowEvents.Navigate(directions))
    }

    private fun notifyAdapter(position: Int) = viewModelScope.launch {
        itemsWindowEventsChannel.send(ItemsWindowEvents.NotifyAdapter(position))
    }

    sealed class ItemsWindowEvents {
        data class NotifyAdapter(val position: Int) : ItemsWindowEvents()
        data class Navigate(val directions: NavDirections) : ItemsWindowEvents()
        object NavigateBack: ItemsWindowEvents()
    }

}
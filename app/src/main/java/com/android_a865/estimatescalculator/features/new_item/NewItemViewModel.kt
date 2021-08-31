package com.android_a865.estimatescalculator.features.new_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.database.Repository
import com.android_a865.estimatescalculator.database.domain.Item
import com.android_a865.estimatescalculator.utils.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val repository: Repository,
    state: SavedStateHandle
): ViewModel() {

    val item = state.get<Item>("item")
    val path = state.get<Path>("path") ?: Path()


    var itemName: String = item?.name ?: ""
    var itemPrice: String = item?.value?.toString() ?: ""


    private val addEditItemChannel = Channel<AddEditItemEvent>()
    val addEditItemEvent = addEditItemChannel.receiveAsFlow()

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditItemChannel.send(AddEditItemEvent.ShowInvalidInputMessage(text))
    }

    fun onSaveClicked() {
        if (itemName.isBlank() || itemPrice.isBlank()) {
            showInvalidInputMessage("fill the required fields")
        }

        if (item != null) {
            val updatedItem = item.copy(
                    name = itemName,
                    path = path,
                    value = itemPrice.toDouble()
            )
            updateItem(updatedItem)


        } else {
            val newItem = Item(
                    name = itemName,
                    path = path,
                    value = itemPrice.toDouble()
            )
            createItem(newItem)
        }


    }


    private fun updateItem(item: Item) = viewModelScope.launch {
        repository.updateItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult2)
    }

    private fun createItem(item: Item) = viewModelScope.launch {
        repository.insertItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult)
    }


    sealed class AddEditItemEvent {
        data class ShowInvalidInputMessage(val msg: String): AddEditItemEvent()
        object NavigateBackWithResult: AddEditItemEvent()
        object NavigateBackWithResult2: AddEditItemEvent()

    }

}
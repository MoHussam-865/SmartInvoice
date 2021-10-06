package com.android_a865.estimatescalculator.presentation.new_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.data.repository.ItemsRepositoryImpl
import com.android_a865.estimatescalculator.domain.model.Item
import com.android_a865.estimatescalculator.domain.use_cases.ItemsUseCases
import com.android_a865.estimatescalculator.utils.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    state: SavedStateHandle
): ViewModel() {

    val item = state.get<Item>("item")
    val path = state.get<Path>("path") ?: Path()


    var itemName: String = item?.name ?: ""
    var itemPrice: String = item?.price?.toString() ?: ""


    private val addEditItemChannel = Channel<AddEditItemEvent>()
    val addEditItemEvent = addEditItemChannel.receiveAsFlow()

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditItemChannel.send(AddEditItemEvent.ShowInvalidInputMessage(text))
    }

    fun onSaveClicked() {
        if (itemName.isBlank() || itemPrice.isBlank()) {
            showInvalidInputMessage("fill the required fields")
            return
        }

        if (item != null) {
            val updatedItem = item.copy(
                    name = itemName,
                    path = path,
                    price = itemPrice.toDouble()
            )
            updateItem(updatedItem)


        } else {
            val newItem = Item(
                    name = itemName,
                    path = path,
                    price = itemPrice.toDouble()
            )
            createItem(newItem)
        }


    }


    private fun updateItem(item: Item) = viewModelScope.launch {
        itemsUseCases.updateItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult2)
    }

    private fun createItem(item: Item) = viewModelScope.launch {
        itemsUseCases.addItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult)
    }


    sealed class AddEditItemEvent {
        data class ShowInvalidInputMessage(val msg: String): AddEditItemEvent()
        object NavigateBackWithResult: AddEditItemEvent()
        object NavigateBackWithResult2: AddEditItemEvent()

    }

}
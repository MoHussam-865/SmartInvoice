package com.android_a865.estimatescalculator.features.new_folder

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
class NewFolderViewModel @Inject constructor(

    private val repository: Repository,
    state: SavedStateHandle
): ViewModel() {

    private val folder = state.get<Item>("folder")
    private val path = state.get<Path>("path") ?: Path(".")

    var folderName: String = folder?.name ?: ""



    private val addEditItemChannel = Channel<AddEditItemEvent>()
    val addEditItemEvent = addEditItemChannel.receiveAsFlow()

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditItemChannel.send(AddEditItemEvent.ShowInvalidInputMessage(text))
    }

    fun onSaveClicked() {
        if (folderName.isBlank()) {
            showInvalidInputMessage("fill the required fields")
        }

        if (folder != null) {
            val updatedItem = folder.copy(
                name = folderName,
                path = path,
                isFolder = true
            )
            updateFolder(updatedItem)


        } else {
            val newItem = Item(
                name = folderName,
                path = path,
                isFolder = true
            )
            createFolder(newItem)
        }


    }


    private fun updateFolder(item: Item) = viewModelScope.launch {
        repository.updateItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult)
    }

    private fun createFolder(item: Item) = viewModelScope.launch {
        repository.insertItem(item)
        addEditItemChannel.send(AddEditItemEvent.NavigateBackWithResult)
    }


    sealed class AddEditItemEvent {
        data class ShowInvalidInputMessage(val msg: String): AddEditItemEvent()
        object NavigateBackWithResult: AddEditItemEvent()
    }


}
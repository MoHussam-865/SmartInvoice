package com.android_a865.estimatescalculator.features.main_page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.database.Repository
import com.android_a865.estimatescalculator.database.domain.Item
import com.android_a865.estimatescalculator.utils.Path
import com.android_a865.estimatescalculator.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: Repository,
    state: SavedStateHandle
): ViewModel() {

    val path = state.get<Path>("path")
    val currentPath = MutableStateFlow(path ?: Path())

    private val itemsFlow = currentPath.flatMapLatest { path ->
        repository.getItems(path.path)
    }

    val itemsData = itemsFlow.asLiveData()

    private val itemsWindowEventsChannel = Channel<ItemsWindowEvents>()
    val itemsWindowEvents = itemsWindowEventsChannel.receiveAsFlow()


    fun onItemClicked(item: Item) = currentPath.update { item.open() }


    fun onBackPressed() {
        if (currentPath.value.isRoot) {
            viewModelScope.launch {
                itemsWindowEventsChannel.send(ItemsWindowEvents.CloseTheApp)
            }
        } else {
            currentPath.update { it.back() }
        }
    }

    sealed class ItemsWindowEvents {
        object CloseTheApp: ItemsWindowEvents()
    }



}
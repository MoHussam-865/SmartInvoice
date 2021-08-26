package com.android_a865.estimatescalculator.features.main_page

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.database.Repository
import com.android_a865.estimatescalculator.database.domain.Item
import com.android_a865.estimatescalculator.database.entities.ItemEntity
import com.android_a865.estimatescalculator.utils.Path
import com.android_a865.estimatescalculator.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val currentPath = MutableStateFlow(Path())

    private val itemsFlow = currentPath.flatMapLatest { path ->
        repository.getItems(path.path)
    }

    val itemsData = itemsFlow.asLiveData()

    private val itemsWindowEventsChannel = Channel<ItemsWindowEvents>()
    val itemsWindowEvents = itemsWindowEventsChannel.receiveAsFlow()
    val isRoot get() = currentPath.value.isRoot


    fun onItemClicked(item: Item) = currentPath.update { item.open() }

    fun onItemLongClick(item: Item) { }

    fun onBackPressed() = currentPath.update { it.back() }




    private fun navigate(action: NavDirections) = viewModelScope.launch {
        itemsWindowEventsChannel.send(ItemsWindowEvents.NavigateTo(action))
    }


    sealed class ItemsWindowEvents {
        data class ConfirmationMessage(val msg: String) : ItemsWindowEvents()
        data class NavigateTo(val direction: NavDirections) : ItemsWindowEvents()
    }



}
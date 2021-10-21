package com.android_a865.estimatescalculator.feature_main.presentation.main_page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.items_use_cases.ItemsUseCases
import com.android_a865.estimatescalculator.utils.Path
import com.android_a865.estimatescalculator.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    state: SavedStateHandle
): ViewModel() {

    val path = state.get<Path>("path")
    val currentPath = MutableStateFlow(path ?: Path())

    @ExperimentalCoroutinesApi
    private val itemsFlow: Flow<List<Item>> = currentPath.flatMapLatest { path ->
        itemsUseCases.getItems(path.path)
    }

    @ExperimentalCoroutinesApi
    val itemsData = itemsFlow.asLiveData()

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    fun onItemClicked(item: Item) = currentPath.update { item.open() }


    fun onBackPressed() {
        if (currentPath.value.isRoot) {
            viewModelScope.launch {
                eventsChannel.send(WindowEvents.CloseTheApp)
            }
        } else {
            currentPath.update { it.back() }
        }
    }

    fun onNewItemSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            MainFragmentDirections.actionMainFragment2ToNewItemFragment(
                path = currentPath.value
            )
        ))
    }

    fun onNewFolderSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            MainFragmentDirections.actionMainFragment2ToNewFolderFragment(
                path = currentPath.value
            )
        ))
    }

    fun onNewEstimateSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            MainFragmentDirections.actionMainFragment2ToNewEstimateFragment()
        ))
    }

    fun onMyEstimateSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            MainFragmentDirections.actionMainFragment2ToInvoicesViewFragment()
        ))
    }

    fun onMyClientsSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            MainFragmentDirections.actionMainFragment2ToClientsFragment()
        ))
    }

    sealed class WindowEvents {
        object CloseTheApp: WindowEvents()
        data class Navigate(val direction: NavDirections): WindowEvents()
    }



}
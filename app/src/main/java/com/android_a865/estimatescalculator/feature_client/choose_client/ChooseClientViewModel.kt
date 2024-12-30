package com.android_a865.estimatescalculator.feature_client.choose_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.core.domain.use_cases.client.ClientsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseClientViewModel @Inject constructor(
    clientsUseCases: ClientsUseCases
): ViewModel() {

    val clients = clientsUseCases.getClients()

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    fun onFabClicked() = viewModelScope.launch {
        eventsChannel.send(
            WindowEvents.Navigate(
                ChooseClientFragmentDirections.actionChooseClientFragmentToAddEditClientFragment()
            )
        )
    }

    fun onItemClicked(client: Client) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.NavigateBackWithResult(client))
    }


    sealed class WindowEvents{
        data class Navigate(val direction: NavDirections): WindowEvents()
        data class NavigateBackWithResult(val client: Client): WindowEvents()

    }
}
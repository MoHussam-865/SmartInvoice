package com.android_a865.estimatescalculator.feature_client.presentation.client_view

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_client.domain.use_cases.ClientsUseCases
import com.android_a865.estimatescalculator.feature_client.presentation.add_edit_client.AddEditClientViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientsUseCases: ClientsUseCases,
    state: SavedStateHandle
): ViewModel() {

    val client = state.get<Client>("client")

    private val eventChannel = Channel<WindowEvents>()
    val windowEvents = eventChannel.receiveAsFlow()


    fun onCall1Clicked() = viewModelScope.launch {
        eventChannel.send(WindowEvents.MakePhoneCall(
            client?.phone1
        ))
    }

    fun onCall2Clicked() = viewModelScope.launch {
        eventChannel.send(WindowEvents.MakePhoneCall(
            client?.phone2
        ))
    }

    fun onSendEmailClicked() = viewModelScope.launch {
        eventChannel.send(WindowEvents.SendEmail(
            client?.email
        ))
    }

    fun onDeleteClientSelected(context: Context){
        AlertDialog.Builder(context)
            .setTitle("Delete Client")
            .setMessage("Are you sure you want to delete this Client\n you can't undo this")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteClient()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteClient() = viewModelScope.launch {
        clientsUseCases.deleteClient(client)
        eventChannel.send(WindowEvents.NavigateBack)
    }

    sealed class WindowEvents {
        data class MakePhoneCall(val phone: String?): WindowEvents()
        data class SendEmail(val email: String?): WindowEvents()
        object NavigateBack: WindowEvents()
    }

}
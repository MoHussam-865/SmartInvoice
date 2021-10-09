package com.android_a865.estimatescalculator.presentation.pdf_preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfPreviewViewModule @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {
    val fileName = state.get<String>("fileName")

    private val eventsChannel = Channel<InvoiceWindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    fun onSendPdfClicked() = viewModelScope.launch {
        fileName?.let {
            eventsChannel.send(InvoiceWindowEvents.SendPdf(it))
        }
    }

    fun onOpenPdfClicked() = viewModelScope.launch {
        fileName?.let {
            eventsChannel.send(InvoiceWindowEvents.OpenPdfExternally(it))
        }
    }


    sealed class InvoiceWindowEvents {
        data class OpenPdfExternally(val fileName: String) : InvoiceWindowEvents()
        data class SendPdf(val fileName: String) : InvoiceWindowEvents()
    }
}
package com.android_a865.estimatescalculator.feature_main.presentation.pdf_preview

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.common.PdfMaker
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfPreviewViewModule @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    val invoice = state.get<Invoice>("invoice")
    private var fileName: String? = null

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            eventsChannel.send(WindowEvents.SendContext)
        }
    }

    fun onStart(context: Context?) = context?.let { context0 ->
        invoice?.let { invoice0 ->
            fileName = PdfMaker().make(context0, invoice0)

            fileName?.let {
                viewModelScope.launch {
                    eventsChannel.send(WindowEvents.OpenPdf(it))
                }
            }
        }
    }


    fun onSendPdfClicked() = viewModelScope.launch {
        fileName?.let {
            eventsChannel.send(WindowEvents.SendPdf(it))
        }
    }

    fun onOpenPdfClicked() = viewModelScope.launch {
        fileName?.let {
            eventsChannel.send(WindowEvents.OpenPdfExternally(it))
        }
    }


    sealed class WindowEvents {
        data class OpenPdfExternally(val fileName: String) : WindowEvents()
        data class SendPdf(val fileName: String) : WindowEvents()
        data class OpenPdf(val fileName: String): WindowEvents()
        object SendContext : WindowEvents()
    }
}
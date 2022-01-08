package com.android_a865.estimatescalculator.feature_reports.presentation.report_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_main.presentation.main_page.MainFragmentViewModel
import com.android_a865.estimatescalculator.feature_reports.domain.model.ReportsTypes
import com.android_a865.estimatescalculator.feature_reports.domain.model.ReportsTypes.*
import com.android_a865.estimatescalculator.utils.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportListViewModel @Inject constructor(
    state: SavedStateHandle
): ViewModel() {

    private val reportType = state.get<ReportsTypes>("report_type")

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    init {
        when (reportType) {
            ClientsBased -> {
                showMessage("clients")
            }
            ItemsBased -> {
                showMessage("items")

            }
            DailyBased -> {
                showMessage("Daily")

            }
            null -> {
                showMessage("Error")
            }
        }.exhaustive
    }

    private fun showMessage(msg: String) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.ShowMessage(msg))
    }

    sealed class WindowEvents {
        data class ShowMessage(val msg: String): WindowEvents()
    }

}
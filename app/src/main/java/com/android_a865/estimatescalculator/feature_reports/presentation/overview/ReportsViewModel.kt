package com.android_a865.estimatescalculator.feature_reports.presentation.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.feature_reports.domain.model.ReportsTypes
import com.android_a865.estimatescalculator.feature_reports.domain.model.TypesNumber
import com.android_a865.estimatescalculator.feature_reports.domain.use_cases.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsUseCases: ReportUseCases
): ViewModel() {


    val numbers = MutableLiveData<TypesNumber>()
    val totalMoney = MutableLiveData(0.0)
    val clientsCount = MutableLiveData(0)
    val itemsCount = MutableLiveData(0)


    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            numbers.value = reportsUseCases.getNumberOf()
            totalMoney.value = reportsUseCases.getTotalMoney()
            clientsCount.value = reportsUseCases.getNumberOfClients()
            itemsCount.value = reportsUseCases.getNumberOfItems()
        }
    }


    fun onViewClientsClicked() {
        viewModelScope.launch {
            eventsChannel.send(WindowEvents.NavigateTo(
                ReportsMainFragmentDirections.actionReportsMainFragmentToFragmentReportList(
                    ReportsTypes.ClientsBased
                )
            ))
        }
    }

    fun onViewItemsClicked() {
        viewModelScope.launch {
            eventsChannel.send(WindowEvents.NavigateTo(
                ReportsMainFragmentDirections.actionReportsMainFragmentToFragmentReportList(
                    ReportsTypes.ItemsBased
                )
            ))
        }
    }

    sealed class WindowEvents {
        data class NavigateTo(val direction: NavDirections): WindowEvents()

    }

}
package com.android_a865.estimatescalculator.feature_reports.presentation.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.databinding.FragmentReportsMainBinding
import com.android_a865.estimatescalculator.feature_main.presentation.main_page.MainFragmentViewModel
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
    val totalMoney = MutableLiveData<Double>()
    val clientsCount = MutableLiveData<Int>()

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            numbers.value = reportsUseCases.getNumberOf()
            totalMoney.value = reportsUseCases.getTotalMoney()
            clientsCount.value = reportsUseCases.getNumberOfClients()
        }
    }


    fun onViewClientsClicked() {
        viewModelScope.launch {
            eventsChannel.send(WindowEvents.NavigateTo(
                ReportsMainFragmentDirections.actionReportsMainFragmentToFragmentReportList()
            ))
        }
    }

    sealed class WindowEvents {
        data class NavigateTo(val direction: NavDirections): WindowEvents()

    }

}
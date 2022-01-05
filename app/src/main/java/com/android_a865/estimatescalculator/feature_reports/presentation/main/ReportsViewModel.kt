package com.android_a865.estimatescalculator.feature_reports.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_reports.domain.model.TypesNumber
import com.android_a865.estimatescalculator.feature_reports.domain.use_cases.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsUseCases: ReportUseCases
): ViewModel() {

    val numbers = MutableLiveData<TypesNumber>()
    val totalMoney = MutableLiveData<Double>()

    init {
        viewModelScope.launch {
            numbers.value = reportsUseCases.getNumberOf()
            totalMoney.value = reportsUseCases.getTotalMoney()
        }
    }



}
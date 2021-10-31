package com.android_a865.estimatescalculator.feature_settings.presentation.settings

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import com.android_a865.estimatescalculator.utils.CURRENCIES
import com.android_a865.estimatescalculator.utils.DATE_FORMATS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    private val appSettingsFlow = repository.getAppSettings()
    private var dateFormat: String? = null
    private var currency: String? = null

    init {
        viewModelScope.launch {
            appSettingsFlow.collect {
                dateFormat = it.dateFormat
                currency = it.currency
            }
        }
    }


    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    fun onCompanyInfoSelected() = viewModelScope.launch {
        eventsChannel.send(WindowEvents.Navigate(
            SettingsFragmentDirections.actionSettingsFragmentToCompanyInfoFragment()
        ))
    }

    fun onDateFormatSelected(context: Context) {
        Log.d("SettingsViewModel", "$dateFormat")


        AlertDialog.Builder(context)
            .setTitle("Choose Format")
            .setSingleChoiceItems(
                DATE_FORMATS.toTypedArray(),
                DATE_FORMATS.indexOf(dateFormat)
            ) { dialog, i ->

                viewModelScope.launch {
                    repository.updateDateFormat(DATE_FORMATS[i])
                }

                dialog.dismiss()
            }.show()

    }

    fun onCurrencySelected(context: Context) {

        Log.d("SettingsViewModel", "$currency")


        AlertDialog.Builder(context)
            .setTitle("Choose Currency")
            .setSingleChoiceItems(
                CURRENCIES.toTypedArray(),
                CURRENCIES.indexOf(currency)
            ) { dialog, i ->

                viewModelScope.launch {
                    repository.updateCurrency(CURRENCIES[i])
                }

                dialog.dismiss()
            }.show()

    }

    sealed class WindowEvents {
        data class Navigate(val direction: NavDirections): WindowEvents()
    }

}
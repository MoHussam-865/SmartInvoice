package com.android_a865.estimatescalculator.feature_network.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_a865.estimatescalculator.feature_network.temp.Role
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefineMeViewModel @Inject constructor(
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    val deviceName = MutableLiveData("")
    val deviceType = MutableLiveData(Role.Solo)

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepo.getAppSettings().first()
            deviceName.value = settings.deviceName
            deviceType.value = settings.myRole
            eventsChannel.send(WindowEvents.SetValues)
        }
    }

    fun onFabClicked() = viewModelScope.launch {
        settingsRepo.updateRole(
            deviceName = deviceName.value ?: "",
            role = deviceType.value ?: Role.Solo
        )
        eventsChannel.send(WindowEvents.GoBack)
    }

    sealed class WindowEvents {
        data object GoBack : WindowEvents()
        data object SetValues : WindowEvents()
    }

}
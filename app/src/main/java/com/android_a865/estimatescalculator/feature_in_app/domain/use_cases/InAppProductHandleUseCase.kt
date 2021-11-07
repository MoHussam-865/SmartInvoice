package com.android_a865.estimatescalculator.feature_in_app.domain.use_cases

import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository

class InAppProductHandleUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(key: String, value: Boolean) {


    }
}
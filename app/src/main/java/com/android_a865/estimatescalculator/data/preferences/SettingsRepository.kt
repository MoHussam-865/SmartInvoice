package com.android_a865.estimatescalculator.data.preferences

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getAppSettings(): Flow<AppSettings>

    suspend fun updateExampleSetting(name: String)

}
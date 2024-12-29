package com.android_a865.estimatescalculator.feature_settings.domain.models

import com.android_a865.estimatescalculator.feature_network.temp.Role

data class AppSettings(
    val company: Company,
    val dateFormat: String,
    val currency: String,
    val isFirst: Boolean,
    val isSubscribed: Boolean,
    val myRole: Role,
    val deviceName: String,
    val deviceId: Long
)

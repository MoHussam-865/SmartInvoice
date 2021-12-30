package com.android_a865.estimatescalculator.feature_settings.domain.models

data class AppSettings(
    val company: Company,
    val dateFormat: String,
    val currency: String,
    val isFirst: Boolean,
    val isSubscribed: Boolean
)

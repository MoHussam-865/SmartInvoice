package com.android_a865.estimatescalculator.feature_reports.domain.model

import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice

data class DaysReport(
    val date: String,
    val invoices: List<Invoice>,
    val estimates: List<Invoice>,

    )
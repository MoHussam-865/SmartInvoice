package com.android_a865.estimatescalculator.feature_reports.domain.model

import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem

data class FullReport(
    val numberOfInvoices: Int,
    val numberOfEstimates: Int,
    val invoicesTotal: Double,
    val estimatesTotal: Double,

    val clientsReport: List<ClientReport>,
    val itemsReport: SmartList<InvoiceItem>,
)

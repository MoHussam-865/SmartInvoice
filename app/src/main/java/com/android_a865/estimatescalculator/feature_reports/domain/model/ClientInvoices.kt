package com.android_a865.estimatescalculator.feature_reports.domain.model

import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.presentation.invoices_view.FilterOptions

data class ClientInvoices(
    val client: Client,
    val invoices: List<Invoice>
)

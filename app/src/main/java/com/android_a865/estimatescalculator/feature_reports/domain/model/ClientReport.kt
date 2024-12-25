package com.android_a865.estimatescalculator.feature_reports.domain.model

import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem

data class ClientReport(
    val client: Client,
    val invoices: List<Invoice>,
    val estimates: List<Invoice>,
    val items: SmartList<InvoiceItem>
)
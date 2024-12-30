package com.android_a865.estimatescalculator.feature_settings.domain.models

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice

data class DatabaseHolder(
    val items: List<ItemEntity>,
    val clients: List<Client>,
    val invoices: List<FullInvoice>
)

package com.android_a865.estimatescalculator.feature_settings.domain.models

import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_items_home.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_items_home.data.relations.FullInvoice

data class DatabaseHolder(
    val items: List<ItemEntity>,
    val clients: List<ClientEntity>,
    val invoices: List<FullInvoice>
)

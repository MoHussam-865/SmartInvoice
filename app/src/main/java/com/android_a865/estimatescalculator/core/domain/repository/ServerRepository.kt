package com.android_a865.estimatescalculator.core.domain.repository

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.core.data.local.entity.Item

interface ServerRepository {

    fun getItems(): List<Item>

    fun getInvoices(): List<Invoice>

    fun getClients(): List<Client>


    fun sendInvoice()

}
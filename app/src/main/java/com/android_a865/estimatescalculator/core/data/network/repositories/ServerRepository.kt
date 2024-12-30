package com.android_a865.estimatescalculator.core.data.network.repositories

import com.android_a865.estimatescalculator.feature_items_home.domain.model.Invoice
import com.android_a865.estimatescalculator.core.data.local.entity.Item

interface ServerRepository {

    // used by the clients
    fun getServerIp(): String

    fun getItems(): List<Item>

    fun getInvoices(): List<Invoice>

    fun getClients(): List<Client>

    fun saveInvoice()

    fun addClient()

}
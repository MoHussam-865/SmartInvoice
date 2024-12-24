package com.android_a865.estimatescalculator.server_repo

import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.feature_main.domain.model.Item

interface ServerRepository {

    // used by the clients
    fun getServerIp(): String

    fun getItems(): List<Item>

    fun getInvoices(): List<Invoice>

    fun getClients(): List<Client>

    fun saveInvoice()

    fun addClient()

}
package com.android_a865.estimatescalculator.core.domain.repository

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice


interface ClientsApiRepository {

    suspend fun sendData(data: ToSend): Boolean

    // -------------------------

    suspend fun getItems(path: String): List<ItemEntity>

    suspend fun getClients(page: Int): List<Client>

    suspend fun getInvoices(page: Int): List<FullInvoice>

}
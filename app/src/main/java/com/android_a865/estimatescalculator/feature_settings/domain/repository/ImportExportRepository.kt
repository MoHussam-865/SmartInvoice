package com.android_a865.estimatescalculator.feature_settings.domain.repository

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice

interface ImportExportRepository {

    // Exporting
    suspend fun getAllInvoices(): List<FullInvoice>

    suspend fun getAllClients(): List<Client>

    suspend fun getAllItems(): List<ItemEntity>



    // Importing
    suspend fun insertInvoice(fullInvoice: FullInvoice)

    suspend fun insertItemEntity(itemEntity: ItemEntity)

    suspend fun insertClient(client: Client)


}
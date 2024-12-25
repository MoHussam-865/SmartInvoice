package com.android_a865.estimatescalculator.feature_settings.domain.repository

import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_items_home.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_items_home.data.relations.FullInvoice

interface ImportExportRepository {

    // Exporting
    suspend fun getAllInvoices(): List<FullInvoice>

    suspend fun getAllClients(): List<ClientEntity>

    suspend fun getAllItems(): List<ItemEntity>



    // Importing
    suspend fun insertInvoice(fullInvoice: FullInvoice)

    suspend fun insertItemEntity(itemEntity: ItemEntity)

    suspend fun insertClient(clientEntity: ClientEntity)


}
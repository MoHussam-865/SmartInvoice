package com.android_a865.estimatescalculator.feature_settings.data.repository

import com.android_a865.estimatescalculator.core.data.local.dao.ImportExportDao
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
import com.android_a865.estimatescalculator.feature_settings.domain.repository.ImportExportRepository

class ImportExportRepositoryImpl(
    private val dao: ImportExportDao
): ImportExportRepository {

    override suspend fun getAllInvoices(): List<FullInvoice> {
        return dao.getAllInvoices()
    }

    override suspend fun getAllClients(): List<Client> {
        return dao.getAllClients()
    }

    override suspend fun getAllItems(): List<ItemEntity> {
        return dao.getAllItems()
    }

    override suspend fun insertInvoice(fullInvoice: FullInvoice) {
        dao.insertInvoice(fullInvoice)
    }

    override suspend fun insertItemEntity(itemEntity: ItemEntity) {
        dao.insertItemEntity(itemEntity)
    }

    override suspend fun insertClient(client: Client) {
        dao.insertClient(client)
    }
}
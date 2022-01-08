package com.android_a865.estimatescalculator.feature_reports.data.repository

import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_main.data.relations.FullInvoice
import com.android_a865.estimatescalculator.feature_reports.data.dao.ReportingDao
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository

class ReportRepositoryImpl(
    private val dao: ReportingDao
): ReportRepository {

    override suspend fun getNumberOf(invoiceType: String): Int {
        return dao.getNumberOfInvoicesWithType(invoiceType)
    }

    override suspend fun getTotalOf(invoiceType: String): Double? {
        return dao.getTotalOf(invoiceType)
    }

    override suspend fun getNumberOfClients(): Int {
        return dao.getNumberOfClients()
    }

    override suspend fun getNumberOfItems(): Int {
        return dao.getNumberOfItems()
    }

    override suspend fun getClients(): List<ClientEntity> {
        return dao.getClients()
    }

    override suspend fun getClientInvoices(id: Int): List<FullInvoice> {
        return dao.getClientInvoices(id)
    }


}
package com.android_a865.estimatescalculator.core.data.local.repositories

import com.android_a865.estimatescalculator.core.data.local.dao.ReportingDao
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
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

    override suspend fun getClients(): List<Client> {
        return dao.getClients()
    }

    override suspend fun getClientInvoices(id: Int): List<FullInvoice> {
        return dao.getClientInvoices(id)
    }


    override suspend fun getInvoicesItems(): List<InvoiceItemEntity> {
        return dao.getInvoicesItems()
    }

    override suspend fun getInvoices(): List<FullInvoice> {
        return dao.getInvoices()
    }


}
package com.android_a865.estimatescalculator.feature_items_home.data.repository

import com.android_a865.estimatescalculator.feature_items_home.data.dao.InvoicesDao
import com.android_a865.estimatescalculator.feature_items_home.data.relations.FullInvoice
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow

class InvoiceRepositoryImpl(
    private val dao: InvoicesDao
) : InvoiceRepository {

    override fun getInvoices(): Flow<List<FullInvoice>> = dao.getInvoices()

    override suspend fun insertInvoice(invoice: FullInvoice) = dao.insertInvoice(invoice)

    override suspend fun updateInvoice(invoice: FullInvoice) = dao.updateInvoice(invoice)

    override suspend fun deleteInvoices(invoices: List<FullInvoice>) = dao.deleteInvoices(invoices)

}
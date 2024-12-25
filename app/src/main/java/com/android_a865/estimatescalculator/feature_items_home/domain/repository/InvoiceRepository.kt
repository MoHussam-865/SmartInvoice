package com.android_a865.estimatescalculator.feature_items_home.domain.repository

import com.android_a865.estimatescalculator.feature_items_home.data.relations.FullInvoice
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {

    fun getInvoices(): Flow<List<FullInvoice>>

    suspend fun insertInvoice(invoice: FullInvoice)

    suspend fun updateInvoice(invoice: FullInvoice)

    suspend fun deleteInvoices(invoices: List<FullInvoice>)

}
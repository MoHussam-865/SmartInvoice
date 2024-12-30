package com.android_a865.estimatescalculator.feature_reports.domain.repository

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice

interface ReportRepository {

    suspend fun getNumberOf(invoiceType: String): Int

    suspend fun getTotalOf(invoiceType: String): Double?

    suspend fun getNumberOfClients(): Int

    suspend fun getNumberOfItems(): Int

    suspend fun getClients(): List<Client>

    suspend fun getClientInvoices(id: Int): List<FullInvoice>

    suspend fun getInvoicesItems(): List<InvoiceItemEntity>

    suspend fun getInvoices(): List<FullInvoice>

}
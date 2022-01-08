package com.android_a865.estimatescalculator.feature_reports.domain.repository

import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_main.data.relations.FullInvoice

interface ReportRepository {

    suspend fun getNumberOf(invoiceType: String): Int

    suspend fun getTotalOf(invoiceType: String): Double?

    suspend fun getNumberOfClients(): Int

    suspend fun getNumberOfItems(): Int

    suspend fun getClients(): List<ClientEntity>

    suspend fun getClientInvoices(id: Int): List<FullInvoice>
}
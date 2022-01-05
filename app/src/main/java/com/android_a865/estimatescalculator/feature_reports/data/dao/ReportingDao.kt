package com.android_a865.estimatescalculator.feature_reports.data.dao

import androidx.room.*
import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.feature_main.data.relations.FullInvoice
import kotlinx.coroutines.flow.Flow


@Dao
interface ReportingDao {

    @Query("SELECT COUNT(*) FROM Invoices WHERE type = :invoiceType")
    suspend fun getNumberOfInvoicesWithType(invoiceType: String): Int

    @Query("SELECT SUM(total) FROM Invoices WHERE type = :invoiceType")
    suspend fun getTotalOf(invoiceType: String): Double

    @Query("SELECT COUNT(DISTINCT clientId) FROM Invoices")
    suspend fun getNumberOfClients(): Int


}
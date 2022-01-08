package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

import com.android_a865.estimatescalculator.feature_client.data.mapper.toClient
import com.android_a865.estimatescalculator.feature_main.data.mapper.toInvoice
import com.android_a865.estimatescalculator.feature_reports.domain.model.ClientInvoices
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository

class GetClientInvoicesUseCase(
   private val repository: ReportRepository
) {
    suspend operator fun invoke(): List<ClientInvoices> {

        val clients = repository.getClients()

        return clients.map { client ->

            val invoices = repository.getClientInvoices(client.id)

            ClientInvoices(
                client = client.toClient(),
                invoices = invoices.map { it.toInvoice() }
            )


        }
    }
}
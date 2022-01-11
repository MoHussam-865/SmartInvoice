package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.data.mapper.toInvoice
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_reports.domain.model.ClientsReport
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository
import com.android_a865.estimatescalculator.utils.addUnique
import com.android_a865.estimatescalculator.utils.date
import java.util.*

class FullReportUseCase(
    private val repository: ReportRepository
) {

    suspend operator fun invoke(): List<ClientsReport> {
        val bills = repository.getInvoices().map { it.toInvoice() }
        val allInvoices = bills.filter { it.type == InvoiceTypes.Invoice }
        val allEstimates = bills.filter { it.type == InvoiceTypes.Estimate }

        val numberOfInvoices = allInvoices.size
        val numberOfEstimates = allEstimates.size

        val invoicesTotal = allInvoices.sumOf { it.total }
        val estimatesTotal = allEstimates.sumOf { it.total }


        /** Client based Report */
        val clientsReport = mutableListOf<ClientsReport>()
        val clients = mutableListOf<Client>()
        // gets The Unique Clients
        allInvoices.forEach { invoice ->

            invoice.client?.let { client ->
                clients.addUnique(client) { listClient ->
                    listClient == client
                }
            }

        }

        // getClient Invoices & Items
        clients.map { client ->
            val clientInvoices = allInvoices.filter { invoice ->
                invoice.client == client
            }



            val clientItems = mutableListOf<InvoiceItem>()
            allInvoices.forEach { invoice ->

                invoice.items.forEach { item ->
                    clientItems.addUnique(
                        data = item,
                        onFound = { invoiceItem ->
                            return@addUnique item.copy(
                                qty = item.qty + invoiceItem.qty,
                                total = item.total + invoiceItem.total
                            )
                        },
                        equals = { invoiceItem ->
                            invoiceItem.id == item.id
                        }
                    )
                }
            }

            ClientsReport(
                client = client,
                invoices = clientInvoices,
                items = clientItems
            )
        }

        /** Items based Report */
        val allItems = mutableListOf<InvoiceItem>()
        allInvoices.forEach { invoice ->
            invoice.items.forEach { item ->
                allItems.addUnique(
                    data = item,
                    onFound = { invoiceItem ->
                        return@addUnique item.copy(
                            qty = item.qty + invoiceItem.qty,
                            total = item.total + invoiceItem.total
                        )
                    },
                    equals = { invoiceItem ->
                        item.id == invoiceItem.id
                    }
                )
            }
        }

        /** Days based Report */
        val uniqueDays = mutableListOf<String>()
        allInvoices.forEach { invoice ->
            val date = invoice.date.date("d/M/yyyy")


            uniqueDays.addUnique(
                data = date,
                equals = { myDate ->
                    myDate == date
                }
            )

        }

        return clientsReport

    }


}
package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_reports.domain.model.TypesNumber
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository

class GetInvoiceNumbersUseCase(
    private val repository: ReportRepository
) {

    suspend operator fun invoke(): TypesNumber {

        val numInvoices = repository.getNumberOf(InvoiceTypes.Invoice.name)
        val numEstimates = repository.getNumberOf(InvoiceTypes.Estimate.name)
        val numDrafts = repository.getNumberOf(InvoiceTypes.Draft.name)

        return TypesNumber(
            invoices = numInvoices,
            estimates = numEstimates,
            drafts = numDrafts
        )
    }

}
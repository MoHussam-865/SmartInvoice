package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceTypes
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository

class GetTotalMoneyUseCase(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(): Double {
        return repository.getTotalOf(InvoiceTypes.Invoice.name)
    }

}
package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

data class ReportUseCases(
    val getNumberOf: GetInvoiceNumbersUseCase,
    val getTotalMoney: GetTotalMoneyUseCase,
    val getNumberOfClients: GetNumberOfClientsUseCase,
)

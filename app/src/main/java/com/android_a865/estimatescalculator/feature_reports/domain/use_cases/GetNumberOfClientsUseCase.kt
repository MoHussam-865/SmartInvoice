package com.android_a865.estimatescalculator.feature_reports.domain.use_cases

import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository

class GetNumberOfClientsUseCase(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getNumberOfClients()
    }

}
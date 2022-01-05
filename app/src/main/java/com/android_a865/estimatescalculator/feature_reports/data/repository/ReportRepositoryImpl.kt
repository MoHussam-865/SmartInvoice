package com.android_a865.estimatescalculator.feature_reports.data.repository

import com.android_a865.estimatescalculator.feature_reports.data.dao.ReportingDao
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository

class ReportRepositoryImpl(
    private val dao: ReportingDao
): ReportRepository {

    override suspend fun getNumberOf(invoiceType: String): Int {
        return dao.getNumberOfInvoicesWithType(invoiceType)
    }

    override suspend fun getTotalOf(invoiceType: String): Double {
        return dao.getTotalOf(invoiceType)
    }
}
package com.android_a865.estimatescalculator.feature_reports.domain.repository

interface ReportRepository {

    suspend fun getNumberOf(invoiceType: String): Int

    suspend fun getTotalOf(invoiceType: String): Double


}
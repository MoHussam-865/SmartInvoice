package com.android_a865.estimatescalculator.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    suspend fun getNumberOfInvoices(): Int

    suspend fun isSubscribed(): Flow<Boolean>

}
package com.android_a865.estimatescalculator.core.data.local.repositories

import com.android_a865.estimatescalculator.core.data.local.dao.SubscriptionDao
import com.android_a865.estimatescalculator.core.domain.repository.SubscriptionRepository
import com.android_a865.estimatescalculator.feature_settings.data.data_source.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(
    private val dao: SubscriptionDao,
    private val preferences: PreferencesManager
): SubscriptionRepository {

    override suspend fun getNumberOfInvoices(): Int {
        return dao.getTotalNumberInInvoicesTable()
    }

    override suspend fun isSubscribed(): Flow<Boolean> {
        return preferences.preferencesFlow.map {
            it.isSubscribed
        }
    }
}
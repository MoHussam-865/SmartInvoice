package com.android_a865.estimatescalculator.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SubscriptionDao {

    @Query("SELECT COUNT(*) FROM Invoices")
    suspend fun getTotalNumberInInvoicesTable(): Int


}
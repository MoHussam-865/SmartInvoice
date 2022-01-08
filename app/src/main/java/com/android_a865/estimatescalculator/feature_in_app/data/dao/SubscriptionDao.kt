package com.android_a865.estimatescalculator.feature_in_app.data.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SubscriptionDao {

    @Query("SELECT COUNT(*) FROM Invoices")
    suspend fun getTotalNumberInInvoicesTable(): Int


}
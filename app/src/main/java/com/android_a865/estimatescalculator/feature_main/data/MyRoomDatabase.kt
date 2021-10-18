package com.android_a865.estimatescalculator.feature_main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.feature_main.data.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.feature_main.data.dao.InvoicesDao
import com.android_a865.estimatescalculator.feature_main.data.dao.ItemsDao
import com.android_a865.estimatescalculator.feature_main.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.feature_main.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.feature_main.data.entities.ItemEntity

@Database(
    entities = [
        ItemEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class
               ],

    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class MyRoomDatabase: RoomDatabase() {

    abstract fun getItemsDao(): ItemsDao

    abstract fun getInvoicesDao(): InvoicesDao

    companion object {
        // Room Database
        const val DATABASE_NAME = "InvoiceMaster.db"
        const val DATABASE_VERSION = 2
    }
}
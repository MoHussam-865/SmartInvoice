package com.android_a865.estimatescalculator.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.core.data.local.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.core.data.local.dao.ClientsDao
import com.android_a865.estimatescalculator.core.data.local.dao.DevicesDao
import com.android_a865.estimatescalculator.core.data.local.dao.ImportExportDao
import com.android_a865.estimatescalculator.core.data.local.dao.InvoicesDao
import com.android_a865.estimatescalculator.core.data.local.dao.ItemsDao
import com.android_a865.estimatescalculator.core.data.local.dao.ReportingDao
import com.android_a865.estimatescalculator.core.data.local.dao.SubscriptionDao
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.Device
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceEntity
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceItemEntity
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity

@Database(
    entities = [
        ItemEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class,
        Client::class,
        Device::class,
               ],
    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class MyRoomDatabase: RoomDatabase() {

    abstract fun getItemsDao(): ItemsDao

    abstract fun getInvoicesDao(): InvoicesDao

    abstract fun getClientsDao(): ClientsDao

    abstract fun getDevicesDao(): DevicesDao

    abstract fun getReportingDao(): ReportingDao

    abstract fun getSubscriptionDao(): SubscriptionDao

    abstract fun getImportExportDao(): ImportExportDao


    companion object {
        // Room Database
        const val DATABASE_NAME = "InvoiceMaster.db"
        const val DATABASE_VERSION = 3
    }
}
package com.android_a865.estimatescalculator.feature_items_home.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.feature_client.data.dao.ClientsDao
import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_settings.data.dao.ImportExportDao
import com.android_a865.estimatescalculator.feature_in_app.data.dao.SubscriptionDao
import com.android_a865.estimatescalculator.feature_items_home.data.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.feature_items_home.data.dao.InvoicesDao
import com.android_a865.estimatescalculator.feature_items_home.data.dao.ItemsDao
import com.android_a865.estimatescalculator.feature_items_home.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.feature_items_home.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.feature_items_home.data.entities.ItemEntity
import com.android_a865.estimatescalculator.feature_network.data.dao.DevicesDao
import com.android_a865.estimatescalculator.feature_network.data.entities.Device
import com.android_a865.estimatescalculator.feature_reports.data.dao.ReportingDao

@Database(
    entities = [
        ItemEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class,
        ClientEntity::class,
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
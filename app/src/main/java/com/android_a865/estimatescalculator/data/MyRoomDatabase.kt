package com.android_a865.estimatescalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.data.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.data.dao.InvoicesDao
import com.android_a865.estimatescalculator.data.dao.ItemsDao
import com.android_a865.estimatescalculator.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.data.entities.ItemEntity
import com.android_a865.estimatescalculator.domain.model.Invoice

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
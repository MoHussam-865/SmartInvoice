package com.android_a865.estimatescalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.data.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.data.dao.ItemsDao
import com.android_a865.estimatescalculator.data.entities.ItemEntity

@Database(
    entities = [
        ItemEntity::class,
               ],

    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class MyRoomDatabase: RoomDatabase() {

    abstract fun getItemsDao(): ItemsDao

    companion object {
        // Room Database
        const val DATABASE_NAME = "InvoiceMaster.db"
        const val DATABASE_VERSION = 1
    }
}
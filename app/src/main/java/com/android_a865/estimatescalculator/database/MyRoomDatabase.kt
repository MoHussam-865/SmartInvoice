package com.android_a865.estimatescalculator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android_a865.estimatescalculator.database.MyRoomDatabase.Companion.DATABASE_VERSION
import com.android_a865.estimatescalculator.database.dao.ItemsDao
import com.android_a865.estimatescalculator.database.entities.ItemEntity

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
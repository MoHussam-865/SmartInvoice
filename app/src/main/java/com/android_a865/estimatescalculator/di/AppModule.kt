package com.android_a865.estimatescalculator.di

import android.app.Application
import androidx.room.Room
import com.android_a865.estimatescalculator.database.MyRoomDatabase
import com.android_a865.estimatescalculator.database.MyRoomDatabase.Companion.DATABASE_NAME
import com.android_a865.estimatescalculator.database.dao.ItemsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideRoomDatabase(app: Application):MyRoomDatabase =
        Room.databaseBuilder(app, MyRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    
    @Provides @Singleton
    fun provideItemsDao(db: MyRoomDatabase): ItemsDao = db.getItemsDao()


}
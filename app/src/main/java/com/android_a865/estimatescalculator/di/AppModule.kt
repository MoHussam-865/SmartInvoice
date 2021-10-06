package com.android_a865.estimatescalculator.di

import android.app.Application
import androidx.room.Room
import com.android_a865.estimatescalculator.data.MyRoomDatabase
import com.android_a865.estimatescalculator.data.MyRoomDatabase.Companion.DATABASE_NAME
import com.android_a865.estimatescalculator.data.dao.ItemsDao
import com.android_a865.estimatescalculator.data.repository.InvoiceRepositoryImpl
import com.android_a865.estimatescalculator.data.repository.ItemsRepositoryImpl
import com.android_a865.estimatescalculator.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): MyRoomDatabase =
        Room.databaseBuilder(app, MyRoomDatabase::class.java, DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideItemsRepository(db: MyRoomDatabase): ItemsRepository {
        return ItemsRepositoryImpl(db.getItemsDao())
    }

    @Provides
    @Singleton
    fun provideInvoicesRepository(db: MyRoomDatabase): InvoiceRepository {
        return InvoiceRepositoryImpl(db.getInvoicesDao())
    }

    @Provides
    @Singleton
    fun provideItemsUseCases(repository: ItemsRepository): ItemsUseCases {
        return ItemsUseCases(
            getItems = GetItemsUseCase(repository),
            getInvoiceItems = GetInvoiceItemsUseCase(repository),
            getItemByID = GetItemByIDUseCase(repository),
            deleteItems = DeleteItemsUseCase(repository),
            addItem = AddItemUseCase(repository),
            updateItem = UpdateItemUseCase(repository),

            )
    }


}
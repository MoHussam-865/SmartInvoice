package com.android_a865.estimatescalculator.di

import android.app.Application
import androidx.room.Room
import com.android_a865.estimatescalculator.feature_client.data.repository.ClientsRepositoryImpl
import com.android_a865.estimatescalculator.feature_client.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.feature_client.domain.use_cases.*
import com.android_a865.estimatescalculator.feature_main.data.MyRoomDatabase
import com.android_a865.estimatescalculator.feature_main.data.MyRoomDatabase.Companion.DATABASE_NAME
import com.android_a865.estimatescalculator.feature_main.data.repository.InvoiceRepositoryImpl
import com.android_a865.estimatescalculator.feature_main.data.repository.ItemsRepositoryImpl
import com.android_a865.estimatescalculator.feature_main.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.feature_main.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.AddInvoiceUseCase
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.GetInvoicesUseCase
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.InvoiceUseCases
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.invoice_use_cases.UpdateInvoiceUseCase
import com.android_a865.estimatescalculator.feature_main.domain.use_cases.items_use_cases.*
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
    fun provideClientsRepository(db: MyRoomDatabase): ClientsRepository {
        return ClientsRepositoryImpl(db.getClientsDao())
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
            updateItem = UpdateItemUseCase(repository)

        )
    }

    @Provides
    @Singleton
    fun provideInvoicesUseCases(repository: InvoiceRepository): InvoiceUseCases {
        return InvoiceUseCases(
            getInvoices = GetInvoicesUseCase(repository),
            addInvoice = AddInvoiceUseCase(repository),
            updateInvoice = UpdateInvoiceUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideClientsUseCases(repository: ClientsRepository): ClientsUseCases {
        return ClientsUseCases(
            getClients = GetClientsUseCase(repository),
            addEditClient = AddEditClientUseCase(repository),
            deleteClient = DeleteClientUseCase(repository),
            getClient = GetClientByIdUseCase(repository)
        )
    }

}
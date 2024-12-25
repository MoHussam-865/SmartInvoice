package com.android_a865.estimatescalculator.di

import android.app.Application
import androidx.room.Room
import com.android_a865.estimatescalculator.feature_client.data.repository.ClientsRepositoryImpl
import com.android_a865.estimatescalculator.feature_client.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.feature_client.domain.use_cases.*
import com.android_a865.estimatescalculator.feature_in_app.data.repository.SubscriptionRepositoryImpl
import com.android_a865.estimatescalculator.feature_in_app.domain.repository.SubscriptionRepository
import com.android_a865.estimatescalculator.feature_in_app.domain.use_cases.SubscriptionUseCase
import com.android_a865.estimatescalculator.feature_items_home.data.MyRoomDatabase
import com.android_a865.estimatescalculator.feature_items_home.data.MyRoomDatabase.Companion.DATABASE_NAME
import com.android_a865.estimatescalculator.feature_items_home.data.repository.InvoiceRepositoryImpl
import com.android_a865.estimatescalculator.feature_items_home.data.repository.ItemsRepositoryImpl
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.feature_items_home.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.invoice_use_cases.*
import com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.items_use_cases.*
import com.android_a865.estimatescalculator.feature_network.data.api.MyApi
import com.android_a865.estimatescalculator.feature_network.data.dao.DevicesDao
import com.android_a865.estimatescalculator.feature_network.temp.Role
import com.android_a865.estimatescalculator.feature_reports.data.repository.ReportRepositoryImpl
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository
import com.android_a865.estimatescalculator.feature_settings.data.data_source.PreferencesManager
import com.android_a865.estimatescalculator.feature_settings.data.repository.ImportExportRepositoryImpl
import com.android_a865.estimatescalculator.feature_settings.data.repository.SettingsRepositoryImpl
import com.android_a865.estimatescalculator.feature_settings.domain.repository.ImportExportRepository
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ExportUseCase
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ImportExportUseCases
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ImportUseCase
import com.android_a865.estimatescalculator.utils.PORT_NUMBER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideSettingsRepository(preferencesManager: PreferencesManager): SettingsRepository {
        return SettingsRepositoryImpl(preferencesManager)
    }

    @Provides
    @Singleton
    fun provideReportRepository(db: MyRoomDatabase): ReportRepository {
        return ReportRepositoryImpl(db.getReportingDao())
    }


    @Provides
    @Singleton
    fun provideSubscriptionRepository(
        db: MyRoomDatabase,
        preferencesManager: PreferencesManager
    ): SubscriptionRepository {
        return SubscriptionRepositoryImpl(
            db.getSubscriptionDao(),
            preferencesManager
        )
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
            copyFolderUseCases = CopyFolderUseCase(repository),

        )
    }

    @Provides
    @Singleton
    fun provideInvoicesUseCases(
        repository: InvoiceRepository,
        repository2: ItemsRepository
    ): InvoiceUseCases {
        return InvoiceUseCases(
            getInvoices = GetInvoicesUseCase(repository),
            addInvoice = AddInvoiceUseCase(repository),
            updateInvoice = UpdateInvoiceUseCase(repository),
            applyDiscountUseCase = ApplyDiscountUseCase(repository2)
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


    @Provides
    @Singleton
    fun provideSubscriptionUseCases(repository: SubscriptionRepository): SubscriptionUseCase {
        return SubscriptionUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideImportExportRepository(db: MyRoomDatabase): ImportExportRepository {
        return ImportExportRepositoryImpl(db.getImportExportDao())
    }

    @Provides
    @Singleton
    fun provideImportExportUseCases(repository: ImportExportRepository): ImportExportUseCases {
        return ImportExportUseCases(
            export = ExportUseCase(repository),
            import = ImportUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDevicesDao(db: MyRoomDatabase): DevicesDao = db.getDevicesDao()

    @Provides
    @Singleton
    suspend fun provideRetrofitInstance(devicesDao: DevicesDao): MyApi? {

        val serverIp = devicesDao.getServer(Role.Server.ordinal)?.ipAddress ?: return null
        return Retrofit.Builder()
            .baseUrl("http://$serverIp:$PORT_NUMBER")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }
}
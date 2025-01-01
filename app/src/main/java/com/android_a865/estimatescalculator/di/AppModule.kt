package com.android_a865.estimatescalculator.di

import android.app.Application
import androidx.room.Room
import com.android_a865.estimatescalculator.core.data.local.MyRoomDatabase
import com.android_a865.estimatescalculator.core.data.local.MyRoomDatabase.Companion.DATABASE_NAME
import com.android_a865.estimatescalculator.core.data.local.dao.DevicesDao
import com.android_a865.estimatescalculator.core.data.local.repositories.ClientsRepositoryImpl
import com.android_a865.estimatescalculator.core.data.local.repositories.InvoiceRepositoryImpl
import com.android_a865.estimatescalculator.core.data.local.repositories.ItemsRepositoryImpl
import com.android_a865.estimatescalculator.core.data.local.repositories.ReportRepositoryImpl
import com.android_a865.estimatescalculator.core.data.local.repositories.SubscriptionRepositoryImpl
import com.android_a865.estimatescalculator.core.data.network.repositories.ClientsApiRepoImpl
import com.android_a865.estimatescalculator.core.data.network.retrofit.ClientApi
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.core.domain.repository.InvoiceRepository
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.domain.repository.SubscriptionRepository
import com.android_a865.estimatescalculator.core.domain.use_cases.client.AddEditClientUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.client.ClientsUseCases
import com.android_a865.estimatescalculator.core.domain.use_cases.client.DeleteClientUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.client.GetClientByIdUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.client.GetClientsUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.invoice.AddInvoiceUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.invoice.ApplyDiscountUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.invoice.GetInvoicesUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.invoice.InvoiceUseCases
import com.android_a865.estimatescalculator.core.domain.use_cases.invoice.UpdateInvoiceUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.AddItemUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.CopyFolderUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.DeleteItemsUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.GetInvoiceItemsUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.GetItemByIDUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.GetItemsUseCase
import com.android_a865.estimatescalculator.core.domain.use_cases.items.ItemsUseCases
import com.android_a865.estimatescalculator.core.domain.use_cases.items.UpdateItemUseCase
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.core.utils.PORT_NUMBER
import com.android_a865.estimatescalculator.feature_in_app.domain.use_cases.SubscriptionUseCase
import com.android_a865.estimatescalculator.feature_reports.domain.repository.ReportRepository
import com.android_a865.estimatescalculator.feature_settings.data.data_source.PreferencesManager
import com.android_a865.estimatescalculator.feature_settings.data.repository.ImportExportRepositoryImpl
import com.android_a865.estimatescalculator.feature_settings.data.repository.SettingsRepositoryImpl
import com.android_a865.estimatescalculator.feature_settings.domain.repository.ImportExportRepository
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ExportUseCase
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ImportExportUseCases
import com.android_a865.estimatescalculator.feature_settings.domain.use_cases.ImportUseCase
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
    fun provideItemsUseCases(
        repository: ItemsRepository,
        settings: SettingsRepository,
        api: ClientsApiRepository
    ): ItemsUseCases {
        return ItemsUseCases(
            getItems = GetItemsUseCase(repository,settings, api),
            getInvoiceItems = GetInvoiceItemsUseCase(repository),
            getItemByID = GetItemByIDUseCase(repository),
            deleteItems = DeleteItemsUseCase(repository),
            addItem = AddItemUseCase(repository,settings, api),
            updateItem = UpdateItemUseCase(repository),
            copyFolderUseCases = CopyFolderUseCase(repository),

        )
    }

    @Provides
    @Singleton
    fun provideInvoicesUseCases(
        repository: InvoiceRepository,
        repository2: ItemsRepository,
        settings: SettingsRepository,
        api: ClientsApiRepository
    ): InvoiceUseCases {
        return InvoiceUseCases(
            getInvoices = GetInvoicesUseCase(repository,settings, api),
            addInvoice = AddInvoiceUseCase(repository,settings, api),
            updateInvoice = UpdateInvoiceUseCase(repository),
            applyDiscountUseCase = ApplyDiscountUseCase(repository2)
        )
    }

    @Provides
    @Singleton
    fun provideClientsUseCases(
        repository: ClientsRepository,
        settings: SettingsRepository,
        api: ClientsApiRepository
    ): ClientsUseCases {
        return ClientsUseCases(
            getClients = GetClientsUseCase(repository,settings, api),
            addEditClient = AddEditClientUseCase(repository,settings, api),
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


    // ---------- Network ------------------


    @Provides
    @Singleton
    suspend fun provideRetrofitInstance(devicesDao: DevicesDao): ClientApi? {

        val serverIp = devicesDao.getServer(Role.Server.ordinal)?.ipAddress ?: return null
        return Retrofit.Builder()
            .baseUrl("http://$serverIp:$PORT_NUMBER")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClientsApiRepository(clientApi: ClientApi?): ClientsApiRepository {
        return ClientsApiRepoImpl(clientApi)
    }
}
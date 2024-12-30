package com.android_a865.estimatescalculator.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceEntity
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceItemEntity
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice

@Dao
interface ImportExportDao {

    // Exporting
    @Transaction
    @Query("SELECT * FROM Invoices")
    suspend fun getAllInvoices(): List<FullInvoice>

    @Query("SELECT * FROM Clients")
    suspend fun getAllClients(): List<Client>

    @Query("SELECT * FROM Items")
    suspend fun getAllItems(): List<ItemEntity>



    // Importing
    suspend fun insertInvoice(fullInvoice: FullInvoice) {
        fullInvoice.apply {
            val invoiceId = insertInvoice(invoice).toInt()

            items.forEach {
                insertInvoiceItem(it.copy(invoiceId = invoiceId))
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoiceEntity: InvoiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoiceItem(invoiceItemEntity: InvoiceItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemEntity(itemEntity: ItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)


}
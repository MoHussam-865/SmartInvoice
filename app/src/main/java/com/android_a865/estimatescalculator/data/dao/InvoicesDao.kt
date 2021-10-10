package com.android_a865.estimatescalculator.data.dao

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.android_a865.estimatescalculator.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.data.relations.FullInvoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoicesDao {

    @Transaction
    @Query("SELECT * FROM Invoices")
    fun getInvoices(): Flow<List<FullInvoice>>

    @Transaction
    @Query("SELECT * FROM Invoices WHERE id = :invoiceId")
    suspend fun getInvoiceById(invoiceId: Int): FullInvoice

    // Insert
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


    // Update Invoice
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun updateInvoice(newInvoice: FullInvoice) {

        val oldInvoice = getInvoiceById(newInvoice.invoice.id)
        updateInvoice(newInvoice.invoice)

        val newItems = newInvoice.items.toMutableList()
        val oldItems = oldInvoice.items.toMutableList()

        newItems.forEach { newItem ->

            //
            insertInvoiceItem(newItem)
            oldItems.removeIf {
                it.itemId == newItem.itemId
            }

        }

        // the remaining items are removed from the newInvoice
        oldItems.forEach { oldItem ->
            deleteInvoiceItem(oldItem)
        }

    }

    @Update
    suspend fun updateInvoice(invoiceEntity: InvoiceEntity)

    // Delete
    suspend fun deleteInvoices(invoices: List<FullInvoice>) = invoices.forEach { deleteInvoice(it) }

    suspend fun deleteInvoice(fullInvoice: FullInvoice) {
        fullInvoice.apply {
            deleteInvoice(invoice)
            deleteInvoiceItems(invoice.id)
        }
    }

    @Delete
    suspend fun deleteInvoice(invoiceEntity: InvoiceEntity)

    @Query("DELETE FROM InvoiceItems WHERE invoiceId = :invoiceId")
    suspend fun deleteInvoiceItems(invoiceId: Int)

    @Delete
    suspend fun deleteInvoiceItem(invoiceItemEntity: InvoiceItemEntity)

}
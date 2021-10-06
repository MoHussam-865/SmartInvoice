package com.android_a865.estimatescalculator.data.dao

import androidx.room.*
import com.android_a865.estimatescalculator.data.entities.InvoiceEntity
import com.android_a865.estimatescalculator.data.entities.InvoiceItemEntity
import com.android_a865.estimatescalculator.data.relations.FullInvoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoicesDao {

    @Transaction @Query("SELECT * FROM Invoices")
    fun getInvoices(): Flow<List<FullInvoice>>

    // Insert
    suspend fun insertInvoice(fullInvoice: FullInvoice) {
        fullInvoice.apply {
            val invoiceId = insertInvoice(invoice).toInt()

            items.forEach {
                insertInvoiceItem(
                    it.copy(invoiceId = invoiceId)
                )
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoiceEntity: InvoiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoiceItem(invoiceItemEntity: InvoiceItemEntity)


    // Update Invoice
    suspend fun updateInvoice(fullInvoice: FullInvoice) {
        fullInvoice.apply {
            updateInvoice(invoice)
            items.forEach { updateInvoiceItem(it) }
        }
    }
    @Update
    suspend fun updateInvoice(invoiceEntity: InvoiceEntity)
    @Update
    suspend fun updateInvoiceItem(invoiceItemEntity: InvoiceItemEntity)

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


}
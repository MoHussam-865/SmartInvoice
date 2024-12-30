package com.android_a865.estimatescalculator.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ClientsDao {

    @Query("SELECT * FROM Clients ORDER BY id DESC")
    fun getClientsEntity(): Flow<List<Client>>

    @Query("SELECT * FROM Clients WHERE id = :id")
    fun getClientById(id: Int): Flow<Client?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    suspend fun updateInvoicesClient(clientId: Int, client: String) {
        getInvoicesWithClientId(clientId).forEach {
            updateInvoice(
                it.copy(
                    client = client
                )
            )
        }
    }

    @Query("SELECT * FROM Invoices WHERE clientId = :id")
    suspend fun getInvoicesWithClientId(id: Int): List<InvoiceEntity>

    @Update
    suspend fun updateInvoice(invoiceEntity: InvoiceEntity)
}
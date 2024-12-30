package com.android_a865.estimatescalculator.core.domain.repository

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import kotlinx.coroutines.flow.Flow

interface ClientsRepository {

    fun getClients(): Flow<List<Client>>

    fun getClientById(id: Int): Flow<Client?>

    suspend fun insertClient(client: Client)

    suspend fun deleteClient(client: Client)

    suspend fun updateInvoicesClient(clientId: Int, client: String)

}
package com.android_a865.estimatescalculator.feature_client.domain.repository

import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

interface ClientsRepository {

    fun getClients(): Flow<List<ClientEntity>>

    fun getClientById(id: Int): Flow<ClientEntity?>

    suspend fun insertClient(client: ClientEntity)

    suspend fun deleteClient(client: ClientEntity)

}
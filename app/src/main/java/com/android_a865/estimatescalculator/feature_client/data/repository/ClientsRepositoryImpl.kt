package com.android_a865.estimatescalculator.feature_client.data.repository

import com.android_a865.estimatescalculator.feature_client.data.dao.ClientsDao
import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import com.android_a865.estimatescalculator.feature_client.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

class ClientsRepositoryImpl(
    private val dao: ClientsDao
): ClientsRepository {

    override fun getClients(): Flow<List<ClientEntity>> {
        return dao.getClientsEntity()
    }

    override suspend fun insertClient(client: ClientEntity) {
        dao.insertClient(client)
    }

    override suspend fun deleteClient(client: ClientEntity) {
        dao.deleteClient(client)
    }
}
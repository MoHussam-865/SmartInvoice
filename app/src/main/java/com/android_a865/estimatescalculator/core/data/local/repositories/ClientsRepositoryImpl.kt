package com.android_a865.estimatescalculator.core.data.local.repositories

import com.android_a865.estimatescalculator.core.data.local.dao.ClientsDao
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

class ClientsRepositoryImpl(
    private val dao: ClientsDao
): ClientsRepository {

    override fun getClients(): Flow<List<Client>> {
        return dao.getClientsEntity()
    }

    override fun getClientById(id: Int): Flow<Client?> {
        return dao.getClientById(id)
    }

    override suspend fun insertClient(client: Client) {
        dao.insertClient(client)
    }

    override suspend fun deleteClient(client: Client) {
        dao.deleteClient(client)
    }

    override suspend fun updateInvoicesClient(clientId: Int, client: String) {
        dao.updateInvoicesClient(clientId, client)
    }
}
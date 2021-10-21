package com.android_a865.estimatescalculator.feature_client.domain.use_cases

import com.android_a865.estimatescalculator.feature_client.data.mapper.toClient
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_client.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetClientsUseCase(
    private val repository: ClientsRepository
) {
    operator fun invoke(): Flow<List<Client>> {
        return repository.getClients().map { clients ->
            clients.map { client ->
                client.toClient()
            }
        }
    }

}
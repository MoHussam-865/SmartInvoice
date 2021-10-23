package com.android_a865.estimatescalculator.feature_client.domain.use_cases

import com.android_a865.estimatescalculator.feature_client.data.mapper.toEntity
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_client.domain.repository.ClientsRepository

class AddEditClientUseCase(
    private val repository: ClientsRepository
) {
    suspend operator fun invoke(client: Client) {
        // TODO
        // client is updated and we want to update all his invoices
        // if (client.id != 0)
        repository.insertClient(client.toEntity())
    }
}
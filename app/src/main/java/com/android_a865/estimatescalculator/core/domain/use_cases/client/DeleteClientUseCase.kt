package com.android_a865.estimatescalculator.core.domain.use_cases.client

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository

class DeleteClientUseCase(
    private val repository: ClientsRepository
) {
    suspend operator fun invoke(client: Client?) {
        client?.let {
            repository.deleteClient(it)
        }
    }
}
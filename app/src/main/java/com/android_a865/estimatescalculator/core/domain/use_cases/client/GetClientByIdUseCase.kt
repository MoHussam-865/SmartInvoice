package com.android_a865.estimatescalculator.core.domain.use_cases.client

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

class GetClientByIdUseCase(
    private val repository: ClientsRepository
) {
    operator fun invoke(id: Int): Flow<Client?> {
        return repository.getClientById(id)
    }
}
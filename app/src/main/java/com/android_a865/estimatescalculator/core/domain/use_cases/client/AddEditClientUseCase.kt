package com.android_a865.estimatescalculator.core.domain.use_cases.client

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.core.utils.toJson

class AddEditClientUseCase(
    private val repository: ClientsRepository
) {
    suspend operator fun invoke(client: Client) {
        repository.insertClient(client.toEntity())

        // client is updated and we want to update all his invoices
        if (client.id != 0) {
            repository.updateInvoicesClient(client.id, client.toJson())
        }
    }
}
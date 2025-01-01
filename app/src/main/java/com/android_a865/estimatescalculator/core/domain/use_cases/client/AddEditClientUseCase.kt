package com.android_a865.estimatescalculator.core.domain.use_cases.client

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.core.utils.toJson
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class AddEditClientUseCase(
    private val repository: ClientsRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {
    suspend operator fun invoke(client: Client) {

        val role = settings.getAppSettings().first().myRole

        if (role == Role.Client) {
            clientApiRepository.sendData(client)
        } else {
            repository.insertClient(client)

            // client is updated and we want to update all his invoices
            if (client.id != 0) {
                repository.updateInvoicesClient(client.id, client.toJson())
            }
        }
    }
}
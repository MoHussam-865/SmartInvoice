package com.android_a865.estimatescalculator.core.domain.use_cases.client

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.domain.repository.ClientsRepository
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

class GetClientsUseCase(
    private val repository: ClientsRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {
    operator fun invoke(searchQuery: String = ""): Flow<List<Client>> {

        CoroutineScope(Dispatchers.IO).launch {
            val role = settings.getAppSettings().first().myRole
            if (role == Role.Client) {
                val clients = clientApiRepository.getClients(1)
                clients.forEach {
                    repository.insertClient(it)
                }
            }
        }

        var clients = repository.getClients()
        if (searchQuery.isNotBlank()) {

            clients = clients.map { clients0 ->
                clients0.filter { client ->
                    client.name.toLowerCase(Locale.ROOT).contains(
                        searchQuery.toLowerCase(Locale.ROOT)
                    ) ||

                            client.org?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false ||

                            client.title?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false ||

                            client.phone1?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false ||

                            client.phone2?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false ||

                            client.email?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false ||

                            client.address?.toLowerCase(Locale.ROOT)?.contains(
                                searchQuery.toLowerCase(Locale.ROOT)
                            ) ?: false
                }
            }
        }
        return clients

    }

}
package com.android_a865.estimatescalculator.feature_client.domain.use_cases

data class ClientsUseCases(
    val getClients: GetClientsUseCase,
    val addEditClient: AddEditClientUseCase,
    val deleteClient: DeleteClientUseCase
)
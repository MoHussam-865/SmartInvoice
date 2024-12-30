package com.android_a865.estimatescalculator.core.domain.use_cases.client

data class ClientsUseCases(
    val getClients: GetClientsUseCase,
    val addEditClient: AddEditClientUseCase,
    val deleteClient: DeleteClientUseCase,
    val getClient: GetClientByIdUseCase
)
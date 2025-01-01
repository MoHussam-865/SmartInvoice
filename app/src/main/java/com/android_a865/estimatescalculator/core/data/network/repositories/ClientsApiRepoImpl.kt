package com.android_a865.estimatescalculator.core.data.network.repositories

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
import com.android_a865.estimatescalculator.core.domain.repository.ToSend
import com.android_a865.estimatescalculator.core.data.network.retrofit.ClientApi
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository

class ClientsApiRepoImpl(
    private val api: ClientApi?
) : ClientsApiRepository {


    override suspend fun sendData(data: ToSend): Boolean {
        return try {

            val response = api?.sendData(data) ?: return false

            if (response.isSuccessful && response.body() != null) {
                /** handel the response */
                true
            } else {
                throw Exception("Server Response Error Estimates might be inaccurate")
                // use existing database
            }
        } catch (e: Exception) {
            false
        }
    }


    override suspend fun getItems(path: String): List<ItemEntity> {

        try {
            val serverResponse = api?.getItems(path = path)

            serverResponse?.let { response ->
                if (response.isSuccessful && response.body() != null) {
                    /** handel the response */
                    return response.body()!!
                } else {
                    throw Exception("Server Response Error Estimates might be inaccurate")
                }
            }
        } catch (_: Exception) {       }
        return emptyList()
    }

    override suspend fun getClients(page: Int): List<Client> {
        try {
            val serverResponse = api?.getClients(page = page)

            serverResponse?.let { response ->
                if (response.isSuccessful && response.body() != null) {
                    /** handel the response */
                    return response.body()!!
                } else {
                    throw Exception("Server Response Error Estimates might be inaccurate")
                }
            }
        } catch (_: Exception) {   }

        return emptyList()
    }

    override suspend fun getInvoices(page: Int): List<FullInvoice> {
        try {
            val serverResponse = api?.getInvoices(page = page)

            serverResponse?.let { response ->
                if (response.isSuccessful && response.body() != null) {
                    /** handel the response */
                    return response.body()!!
                } else {
                    throw Exception("Server Response Error Estimates might be inaccurate")
                }
            }

        } catch (_: Exception) {
        }

        return emptyList()
    }


}
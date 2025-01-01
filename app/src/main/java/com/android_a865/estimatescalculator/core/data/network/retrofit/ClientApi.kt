package com.android_a865.estimatescalculator.core.data.network.retrofit

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
import com.android_a865.estimatescalculator.core.domain.repository.ToSend
import com.android_a865.estimatescalculator.core.utils.GET_CLIENTS_URL
import com.android_a865.estimatescalculator.core.utils.GET_INVOICES_URL
import com.android_a865.estimatescalculator.core.utils.GET_ITEMS_URL
import com.android_a865.estimatescalculator.core.utils.SAVE_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ClientApi {

    @POST(GET_ITEMS_URL)
    suspend fun getItems(@Body path: String): Response<List<ItemEntity>>

    @POST(GET_CLIENTS_URL)
    suspend fun getClients(@Body page: Int): Response<List<Client>>

    @POST(GET_INVOICES_URL)
    suspend fun getInvoices(@Body page: Int): Response<List<FullInvoice>>


    // ------

    @POST(SAVE_URL)
    suspend fun sendData(@Body data: ToSend): Response<Boolean>

}
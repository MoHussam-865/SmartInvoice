package com.android_a865.estimatescalculator.core.data.network.websocket

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
import com.android_a865.estimatescalculator.core.domain.repository.ToSend
import com.android_a865.estimatescalculator.core.utils.PORT_NUMBER
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient(
    private val serverIpAddress: String,
    private val onUpdate: (ToSend?) -> Unit
) {

    fun connectToUpdateEndpoint() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("ws://$serverIpAddress:$PORT_NUMBER/update/")
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, data: String) {
                super.onMessage(webSocket, data)

                val myData = parseData(data)
                onUpdate(myData)
            }
        }

        client.newWebSocket(request, listener)
    }

    private fun parseData(data: String): ToSend? {

        try {
            return Gson().fromJson(data, ItemEntity::class.java)
        } catch (_: Exception) {}

        try {
            return Gson().fromJson(data, Client::class.java)
        } catch (_: Exception) {}

        try {
            return Gson().fromJson(data, FullInvoice::class.java)
        } catch (_: Exception) {}
        return null
    }


}
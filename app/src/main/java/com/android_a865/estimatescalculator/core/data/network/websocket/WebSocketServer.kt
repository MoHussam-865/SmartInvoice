package com.android_a865.estimatescalculator.core.data.network.websocket

import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.data.local.entity.ItemEntity
import com.android_a865.estimatescalculator.core.data.local.relations.FullInvoice
import com.android_a865.estimatescalculator.core.domain.repository.ToSend
import com.android_a865.estimatescalculator.core.utils.PORT_NUMBER
import com.android_a865.estimatescalculator.core.utils.SAVE_URL
import com.android_a865.estimatescalculator.core.utils.toJson
import com.google.gson.Gson
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach

class WebSocketServer(

) {

    private val clients = mutableListOf<DefaultWebSocketServerSession>()

    // gets data (items, clients or invoices) to save them to the database and notify the
    // clients
    fun startServer(endpoint: String, onDataReceived: (ToSend?) -> ToSend?) {

        embeddedServer(CIO, port = PORT_NUMBER.toInt()) {

            install(WebSockets)

            routing {
                webSocket(SAVE_URL) {
                    clients += this
                    try {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val data = frame.readText()

                                onDataReceived(parseData(data))?.let {
                                    // TODO set the end point
                                    broadCast("", it)
                                }
                            }
                        }
                    } finally {
                        clients -= this
                    }
                }
            }

        }.start(wait = true)

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

    private suspend fun broadCast(endpoint: String, message: ToSend) {
        val jsonMessage = message.toJson()

        clients.forEach {
            it.send(Frame.Text(jsonMessage))
        }
    }






}
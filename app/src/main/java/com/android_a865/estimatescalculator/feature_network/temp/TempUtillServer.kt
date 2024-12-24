package com.android_a865.estimatescalculator.feature_network.temp



import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText


// Server
fun main() {
    val connectedClients = mutableSetOf<DefaultWebSocketServerSession>()

    embeddedServer(Netty, port = 8080) {
        install(WebSockets)

        routing {
            // HTTP GET endpoint
            get("/fetchData") {
                val responseData = mapOf("status" to "success", "data" to "Initial data fetched!")
                call.respond(responseData)
            }


            // WebSocket endpoint
            webSocket("/updates") {
                connectedClients.add(this)
                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            println("Received: ${frame.readText()}")
                        }
                    }
                } finally {
                    connectedClients.remove(this)
                }
            }
        }
    }.start(wait = true)
}

package com.android_a865.estimatescalculator.feature_network.temp

import com.android_a865.estimatescalculator.feature_network.data.entities.Device
import com.android_a865.estimatescalculator.utils.PORT_NUMBER
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val SERVER_IP = "192.168.1.12"


// client
fun main2() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://$SERVER_IP:$PORT_NUMBER")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Device::class.java)

    runBlocking {
//        val response = apiService.fetchData()
//        println("Fetched data: $response")
    }

}

fun main3(){

    val client = HttpClient {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket("ws://$SERVER_IP:$PORT_NUMBER/updates") {
            launch {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        println("Update: ${frame.readText()}")
                    }
                }
            }
            send(Frame.Text("Client connected!"))
        }
    }
}
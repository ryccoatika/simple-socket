package com.ryccoatika.socketserver

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket

class SocketServer(port: Int = 0) {
    private var serverSocket: ServerSocket = ServerSocket(port)
    private val incomingMessages = MutableStateFlow<Message?>(null)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    try {
                        val socket = serverSocket.accept()
                        listenMessage(socket)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    delay(2000)
                }
            }
        }
    }

    private fun listenMessage(socket: Socket) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = DataInputStream(socket.getInputStream())
                    while (true) {
                        if (inputStream.available() > 0) {
                            incomingMessages.value = Message(
                                hostAddress = socket.inetAddress.hostAddress ?: "",
                                message = inputStream.readUTF(),
                            )
                            delay(2000)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val messages: StateFlow<Message?> = incomingMessages.asStateFlow()

    val address: String?
        get() = serverSocket.inetAddress.hostAddress

    val port: Int
        get() = serverSocket.localPort

    fun shutdownServer() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                serverSocket.close()
            }
        }
    }
}
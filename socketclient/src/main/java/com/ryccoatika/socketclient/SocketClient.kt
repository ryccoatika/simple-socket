package com.ryccoatika.socketclient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.Socket

class SocketClient(
    private val host: String,
    private val port: Int,
) {
    private var socket: Socket? = null
    private var outputStream: DataOutputStream? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                socket = Socket(host, port)
                outputStream = DataOutputStream(socket?.getOutputStream())
            }
        }
    }

    fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                socket?.close()
            }
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                outputStream?.writeUTF(message)
            }
        }
    }
}
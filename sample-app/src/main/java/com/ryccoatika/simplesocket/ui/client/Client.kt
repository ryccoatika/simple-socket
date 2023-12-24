package com.ryccoatika.simplesocket.ui.client

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.simplesocket.ui.theme.AppTheme
import com.ryccoatika.socketclient.SocketClient
import com.ryccoatika.socketclient.SocketClientCallback
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Client(
    navigateUp: () -> Unit,
) {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var incomingMessages by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    var socketClient by remember { mutableStateOf<SocketClient?>(null) }
    val socketClientCallback = remember {
        object : SocketClientCallback {
            override fun onConnected() {
                Log.i("190401", "onConnected")
                isConnected = true
            }

            override fun onConnectionFailure(e: Exception) {
                Log.i("190401", "onConnectFailure: $e")
                isConnected = false
                socketClient = null
            }

            override fun onMessageReceived(message: String) {
                Log.i("190401", "onMessageReceived: $message")
                incomingMessages = message
            }

            override fun onDisconnected() {
                Log.i("190401", "onDisconnected")
                isConnected = false
                socketClient = null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                title = { Text("Client") },
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp,
                )
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = host,
                label = {
                    Text(text = "ip address")
                },
                enabled = !isConnected,
                onValueChange = {
                    host = it
                },
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = port.takeIf { it != 0 }?.toString() ?: "",
                label = {
                    Text(text = "port")
                },
                enabled = !isConnected,
                onValueChange = {
                    port = it.toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )
            )
            Spacer(Modifier.height(5.dp))
            if (!isConnected) {
                OutlinedButton(
                    onClick = {
                        socketClient = SocketClient(host, port).also {
                            it.setSocketClientCallback(socketClientCallback)
                            it.connect()
                        }
                    },
                ) {
                    Text(text = "Connect")
                }
            }
            if (isConnected) {
                OutlinedButton(
                    onClick = {
                        socketClient?.disconnect()
                        socketClient = null
                    },
                ) {
                    Text(text = "Disconnect")
                }
            }
            Spacer(Modifier.height(30.dp))
            if (isConnected) {
                if (incomingMessages.isNotEmpty()) {
                    Text("Incoming message from server:\n$incomingMessages")
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = message,
                    label = {
                        Text(text = "enter message")
                    },
                    onValueChange = {
                        message = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            socketClient?.sendMessage(message)
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClientPreview() {
    AppTheme {
        Client(
            navigateUp = {},
        )
    }
}
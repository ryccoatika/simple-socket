package com.ryccoatika.simplesocket.ui.server

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.simplesocket.ui.theme.AppTheme
import com.ryccoatika.socketserver.SocketServerCallback
import com.ryccoatika.socketserver.SocketServer
import com.ryccoatika.socketserver.models.Client
import com.ryccoatika.socketserver.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Server(
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val ipAddress = remember { NetworkUtils.getLocalIpv4Address(context) }
    val connectedClients = remember { mutableStateListOf<Client>() }
    var selectedClient by remember { mutableStateOf<Client?>(null) }
    var sendMessage by remember { mutableStateOf("") }
    val incomingMessages = remember { mutableStateMapOf<Client, String>() }
    val socketServer = remember {
        SocketServer(1111)
    }

    LaunchedEffect(socketServer) {
        socketServer.setSocketServerCallback(object :
            SocketServerCallback {
            override fun onClientConnected(client: Client) {
                Log.i("190401", "onClientConnected: $client")
                connectedClients += client
            }

            override fun onClientDisconnected(client: Client) {
                Log.i("190401", "onClientDisconnected: $client")
                connectedClients -= connectedClients
            }

            override fun onMessageReceived(client: Client, message: String) {
                Log.i("190401", "onMessageReceived: $client, $message")
                incomingMessages[client] = message
            }
        })
        socketServer.startServer()
    }

    BackHandler {
        socketServer.stopServer()
        navigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            socketServer.stopServer()
                            navigateUp()
                        },
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                title = { Text("Server") },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp,
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "IPAddress: $ipAddress")
            Text(text = "Port: ${socketServer.port}")
            Spacer(Modifier.height(10.dp))
            Text(text = "Connected Clients:")
            connectedClients.forEach { client ->
                Text(text = "address: ${client.hostAddress}")
                Text(text = "port: ${client.port}")
                Text(text = "localPort: ${client.localPort}")
            }
            Spacer(Modifier.height(10.dp))
            var showDropdown by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = showDropdown,
                onExpandedChange = {
                    showDropdown = !showDropdown
                }
            ) {
                TextField(
                    value = selectedClient?.hostAddress ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = !showDropdown }
                ) {
                    connectedClients.forEach { client ->
                        DropdownMenuItem(
                            text = {
                                Text(client.hostAddress)
                            },
                            onClick = {
                                selectedClient = client
                                showDropdown = !showDropdown
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = sendMessage,
                label = {
                    Text(text = "enter message")
                },
                onValueChange = {
                    sendMessage = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        selectedClient?.let {
                            socketServer.sendMessage(selectedClient, sendMessage)
                        }
                    }
                )
            )
            Spacer(Modifier.height(10.dp))
            Text(text = "Messages:")
            incomingMessages.keys.forEach { client ->
                val message = incomingMessages[client] ?: "-"
                Text(text = "from: ${client.hostAddress}")
                Text(text = "message: $message")
            }
        }
    }
}

@Preview
@Composable
private fun ServerPreview() {
    AppTheme {
        Server(
            navigateUp = {},
        )
    }
}
package com.ryccoatika.simplesocket.ui.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Client(
    navigateUp: () -> Unit,
) {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var socketClient by remember { mutableStateOf<SocketClient?>(null) }

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
                onValueChange = {
                    port = it.toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )
            )
            Spacer(Modifier.height(5.dp))
            Row {
                OutlinedButton(
                    onClick = {
                        socketClient = try {
                            SocketClient(
                                host = host,
                                port = port
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    },
                ) {
                    Text(text = "Connect")
                }
                OutlinedButton(
                    onClick = {
                        socketClient?.disconnect()
                    },
                ) {
                    Text(text = "Disconnect")
                }
            }
            Spacer(Modifier.height(30.dp))
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

@Preview
@Composable
private fun ClientPreview() {
    AppTheme {
        Client(
            navigateUp = {},
        )
    }
}
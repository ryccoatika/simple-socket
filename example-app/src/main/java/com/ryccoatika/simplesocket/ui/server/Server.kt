package com.ryccoatika.simplesocket.ui.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.simplesocket.ui.theme.AppTheme
import com.ryccoatika.socketserver.utils.NetworkUtils
import com.ryccoatika.socketserver.SocketServer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Server(
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val socketServer = remember { SocketServer(1111) }
    val ipAddress = remember { NetworkUtils.getLocalIpv4Address(context) }
    val message by socketServer.messages.collectAsState()

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
                title = { Text("Server") },
                actions = {
                    TextButton(
                        onClick = {
                            socketServer.shutdownServer()
                        },
                    ) {
                        Text("Shutdown")
                    }
                }
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
            Text(text = "Messages:")
            message?.let { message ->
                Text(text = "from: ${message.hostAddress}")
                Text(text = "message: ${message.message}")
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
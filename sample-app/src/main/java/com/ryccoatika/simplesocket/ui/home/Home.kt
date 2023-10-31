package com.ryccoatika.simplesocket.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.simplesocket.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    openClient: () -> Unit,
    openServer: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simple Socket") },
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ElevatedButton(onClick = openClient) {
                Text("Client")
            }
            ElevatedButton(onClick = openServer) {
                Text("Server")
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    AppTheme {
        Home(
            openClient = {},
            openServer = {},
        )
    }
}
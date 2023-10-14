package com.ryccoatika.simplesocket.ui.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.simplesocket.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Server(
    navigateUp: () -> Unit,
) {
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
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

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
package com.ryccoatika.simplesocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ryccoatika.simplesocket.ui.client.Client
import com.ryccoatika.simplesocket.ui.home.Home
import com.ryccoatika.simplesocket.ui.server.Server
import com.ryccoatika.simplesocket.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val focusManager = LocalFocusManager.current
            val navController = rememberNavController()
            AppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                    ) {
                        focusManager.clearFocus()
                    }
                ) {
                    composable("home") {
                        Home(
                            openClient = {
                                navController.navigate("client")
                            },
                            openServer = {
                                navController.navigate("server")
                            }
                        )
                    }
                    composable("client") {
                        Client(
                            navigateUp = navController::navigateUp,
                        )
                    }
                    composable("server") {
                        Server(
                            navigateUp = navController::navigateUp,
                        )
                    }
                }
            }
        }
    }
}

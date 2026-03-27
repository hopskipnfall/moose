package org.moose.client.ui

import androidx.compose.runtime.*

@Composable
fun App(client: org.moose.client.network.KailleraClient) {
    MooseTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        if (isLoggedIn) {
            MainScreen(
                client = client,
                onLogout = { 
                    client.disconnect()
                    isLoggedIn = false 
                }
            )
        } else {
            LoginScreen(
                onConnect = { address, username ->
                    client.connect(
                        address = address,
                        username = username,
                        onConnected = { isLoggedIn = true },
                        onError = { println("Connection Error: $it") }
                    )
                }
            )
        }
    }
}

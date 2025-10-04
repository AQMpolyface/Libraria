package org.polyface.libraria

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import libraria.composeapp.generated.resources.Res
import libraria.composeapp.generated.resources.compose_multiplatform


@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
    /*if (!isLoggedIn) {
        Column(
            modifier = Modifier
                .fillMaxSize(), // Make the Column take the whole screen
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            Text(
                username,
            )
            Text(
                password,
            )
            Button(onClick = {isLoggedIn = true}) {
                Text("Hello word")
            }
        }
    } else {*/
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(), // Make the Column take the whole screen,
                verticalArrangement = Arrangement.Center, // Center content vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Cyan)
                    .padding(16.dp), // optional padding
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* action here */ },
                ) {
                    Text("Hewo")
                }
                Spacer(Modifier.padding(5.dp))
                Button(
                    onClick = { /* action here */ }
                ) {
                    Text("Hewo 2")
                }
            }
        }


    }
}


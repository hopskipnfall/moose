package org.moose.client.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.moose.client.data.AccessLevel
import org.moose.client.data.MockData

@Composable
fun LoginScreen(onConnect: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var serverAddress by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(LightPurple.copy(alpha = 0.3f), DarkBackground),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glassmorphism panel
        Box(
            modifier = Modifier
                .width(400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceColor.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "MOOSE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "GAME SERVER CLIENT",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = LightPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = serverAddress,
                    onValueChange = { serverAddress = it },
                    label = { Text("Server Address", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = LightPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onConnect(serverAddress, username)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = NeonBlue)
                ) {
                    Text("CONNECT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

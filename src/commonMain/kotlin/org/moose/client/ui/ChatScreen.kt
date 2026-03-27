package org.moose.client.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.moose.client.data.ChatMessage
import org.moose.client.data.MessageType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun ChatArea(modifier: Modifier = Modifier, title: String = "#server-chat", messages: List<ChatMessage>, onSendMessage: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        // Chat Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(title, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Divider(color = SurfaceColor, thickness = 1.dp)

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message)
            }
        }

        // Input Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceColor)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Type a message in $title...", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = LightPurple
                )
            )
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "[${message.timestamp}]",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        
        Column {
            when (message.type) {
                MessageType.SERVER_WELCOME -> {
                    Text(text = "Server", color = LightPurple, fontWeight = FontWeight.Bold)
                    Text(text = message.content, color = TextPrimary)
                }
                MessageType.BOT_ANNOUNCEMENT -> {
                    Row {
                        Text(text = message.sender?.username ?: "Bot", color = NeonBlue, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(LightPurple.copy(alpha=0.3f)).padding(horizontal = 4.dp)) {
                            Text("Admin", color = LightPurple, fontSize = 10.sp)
                        }
                    }
                    Text(text = message.content, color = TextPrimary)
                }
                MessageType.USER_TEXT -> {
                    Text(text = message.sender?.username ?: "Unknown", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(text = message.content, color = TextSecondary)
                }
            }
        }
    }
}

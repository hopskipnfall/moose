package org.moose.client.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hopskipnfall.kaillera.protocol.model.GameStatus
import org.moose.client.data.AccessLevel
import org.moose.client.data.ChatMessage
import org.moose.client.data.MessageType
import org.moose.client.data.MockData
import org.moose.client.data.MooseGame
import org.moose.client.data.MooseUser
import org.moose.client.network.KailleraClient

@Composable
fun MainScreen(client: KailleraClient, onLogout: () -> Unit) {
    var activeChatTitle by remember { mutableStateOf("#server-chat") }
    
    val users by client.users.collectAsState()
    val games by client.games.collectAsState()
    val chatMessages by client.chatMessages.collectAsState()
    
    val admins = users.filter { it.accessLevel == AccessLevel.ADMIN }
    val lobbyUsers = users.filter { it.accessLevel != AccessLevel.ADMIN }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Left Sidebar (Navigation)
        NavigationSidebar(
            modifier = Modifier
                .width(220.dp)
                .fillMaxHeight()
                .background(SurfaceColor)
        )

        // Main Chat Area
        ChatArea(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            messages = chatMessages,
            title = activeChatTitle,
            onSendMessage = { client.sendChat(it) }
        )

        // Right Sidebar (User List)
        UserSidebar(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .background(SurfaceColor),
            users = lobbyUsers,
            games = games,
            admins = admins,
            onGameClick = { game -> 
                activeChatTitle = "Game: ${game.name}"
            }
        )
    }
}

@Composable
fun NavigationSidebar(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "MOOSE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Basic Navigation items
        NavigationItem(label = "Server Chat", isSelected = true)
        Spacer(modifier = Modifier.height(8.dp))
        NavigationItem(label = "Private Messages", isSelected = false)
    }
}

@Composable
fun NavigationItem(label: String, isSelected: Boolean) {
    val backgroundColor = if (isSelected) LightPurple.copy(alpha = 0.2f) else Color.Transparent
    val textColor = if (isSelected) TextPrimary else TextSecondary
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(text = label, color = textColor, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
fun UserSidebar(
    modifier: Modifier = Modifier, 
    users: List<MooseUser>, 
    games: List<MooseGame>,
    admins: List<MooseUser>,
    onGameClick: (MooseGame) -> Unit
) {
    val allLobbyUsers = (admins + users).distinctBy { it.username }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Text("LOBBY USERS (${allLobbyUsers.size})", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(allLobbyUsers) { user ->
            UserItem(user = user)
            Spacer(modifier = Modifier.height(8.dp))
        }

        val waitingGames = games.filter { it.status == GameStatus.WAITING }
        if (waitingGames.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("WAITING FOR PLAYERS (${waitingGames.size})", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(waitingGames) { game ->
                GameItem(game = game, onClick = { onGameClick(game) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        val playingGames = games.filter { it.status == GameStatus.PLAYING }
        if (playingGames.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("PLAYING (${playingGames.size})", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(playingGames) { game ->
                GameItem(game = game, onClick = { onGameClick(game) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun GameItem(game: MooseGame, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LightPurple.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(text = game.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Players: ${game.players.joinToString(", ") { it.username }}",
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
fun UserItem(user: MooseUser) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        // Simple avatar circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (user.accessLevel == AccessLevel.ADMIN) LightPurple else NeonBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(user.username.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = user.username, color = TextPrimary, fontWeight = FontWeight.Medium)
            if (user.accessLevel == AccessLevel.ADMIN) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(LightPurple.copy(alpha=0.3f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text("Admin", color = LightPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

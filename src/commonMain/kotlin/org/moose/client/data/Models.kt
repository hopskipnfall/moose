package org.moose.client.data

import io.github.hopskipnfall.kaillera.protocol.model.GameStatus

enum class AccessLevel {
    USER, ADMIN
}

data class MooseUser(
    val username: String,
    val accessLevel: AccessLevel
)

data class MooseGame(
    val id: String,
    val name: String,
    val status: GameStatus,
    val players: List<MooseUser>
)

enum class MessageType {
    USER_TEXT, SERVER_WELCOME, BOT_ANNOUNCEMENT
}

data class ChatMessage(
    val sender: MooseUser?,
    val timestamp: String,
    val content: String,
    val type: MessageType = MessageType.USER_TEXT
)

data class ChatRoom(
    val id: String,
    val name: String,
    val messages: List<ChatMessage>,
    val participants: List<MooseUser>
)

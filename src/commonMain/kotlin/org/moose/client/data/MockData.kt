package org.moose.client.data

import io.github.hopskipnfall.kaillera.protocol.model.GameStatus

object MockData {
    val currentUser = MooseUser("PixelKnight", AccessLevel.USER)

    val admins = listOf(
        MooseUser("FrostByte", AccessLevel.ADMIN),
        MooseUser("Nebula Admin", AccessLevel.ADMIN),
        MooseUser("VoidWalker", AccessLevel.ADMIN)
    )

    val lobbyUsers = listOf(
        currentUser,
        MooseUser("CyberPanda", AccessLevel.USER),
        MooseUser("G4merGrl", AccessLevel.USER)
    )

    val games = listOf(
        MooseGame(
            id = "g1",
            name = "Apex Legends - Ranked",
            status = GameStatus.WAITING,
            players = listOf(
                MooseUser("MysticMage", AccessLevel.USER),
                MooseUser("ShadowRanger", AccessLevel.USER)
            )
        ),
        MooseGame(
            id = "g2",
            name = "Street Fighter III: 3rd Strike",
            status = GameStatus.PLAYING,
            players = listOf(
                MooseUser("RyuMain", AccessLevel.USER),
                MooseUser("ChunLiGod", AccessLevel.USER)
            )
        )
    )

    val serverChatMessages = listOf(
        ChatMessage(
            sender = null,
            timestamp = "09:44",
            content = "Welcome to the official Moose Server Chat! Be respectful.",
            type = MessageType.SERVER_WELCOME
        ),
        ChatMessage(
            sender = currentUser,
            timestamp = "09:45",
            content = "Hey everyone! Ready for the tournament tonight?",
            type = MessageType.USER_TEXT
        ),
        ChatMessage(
            sender = games[0].players[1], // ShadowRanger
            timestamp = "09:46",
            content = "Can't wait! The new map looks awesome.",
            type = MessageType.USER_TEXT
        ),
        ChatMessage(
            sender = admins[0], // FrostByte
            timestamp = "09:47",
            content = "Remember to check the rules in #announcements!",
            type = MessageType.BOT_ANNOUNCEMENT
        ),
        ChatMessage(
            sender = games[0].players[0], // MysticMage
            timestamp = "09:48",
            content = "Looking for a party for Apex Legends. Anyone down?",
            type = MessageType.USER_TEXT
        ),
        ChatMessage(
            sender = currentUser,
            timestamp = "09:49",
            content = "I am! Let's get that W!",
            type = MessageType.USER_TEXT
        )
    )
}

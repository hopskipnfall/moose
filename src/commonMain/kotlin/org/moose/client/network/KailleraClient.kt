package org.moose.client.network

import io.github.hopskipnfall.kaillera.protocol.connection.ConnectMessageFactory
import io.github.hopskipnfall.kaillera.protocol.connection.RequestPrivateKailleraPortRequest
import io.github.hopskipnfall.kaillera.protocol.connection.RequestPrivateKailleraPortResponse
import io.github.hopskipnfall.kaillera.protocol.model.ConnectionType
import io.github.hopskipnfall.kaillera.protocol.v086.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.moose.client.data.AccessLevel
import org.moose.client.data.ChatMessage
import org.moose.client.data.MessageType
import org.moose.client.data.MooseGame
import org.moose.client.data.MooseUser

class KailleraClient(private val coroutineScope: CoroutineScope) {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket: ConnectedDatagramSocket? = null
    
    private var messageIdCounter = 0
    private var username: String = ""
    
    // UI State exposed
    private val _users = MutableStateFlow<List<MooseUser>>(emptyList())
    val users = _users.asStateFlow()

    private val _games = MutableStateFlow<List<MooseGame>>(emptyList())
    val games = _games.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    fun connect(address: String, username: String, onConnected: () -> Unit, onError: (String) -> Unit) {
        this.username = username
        coroutineScope.launch {
            try {
                val host = if (address.contains(":")) address.substringBefore(":") else address
                val port = if (address.contains(":")) address.substringAfter(":").toInt() else 27888
                
                val serverAddress = InetSocketAddress(host, port)
                val tempSocket = aSocket(selectorManager).udp().connect(serverAddress)
                
                // 1. Send Port Request
                val reqBuffer = Buffer()
                ConnectMessageFactory.write(reqBuffer, RequestPrivateKailleraPortRequest("0.83"))
                tempSocket.send(Datagram(io.ktor.utils.io.core.ByteReadPacket(reqBuffer.readByteArray()), serverAddress))
                
                // 2. Receive Port Response
                val responseDatagram = tempSocket.receive()
                val resBuffer = Buffer().apply { write(responseDatagram.packet.readBytes()) }
                val connectResponse = ConnectMessageFactory.read(resBuffer)
                
                tempSocket.close()

                if (connectResponse is RequestPrivateKailleraPortResponse) {
                    val privatePort = connectResponse.port
                    startSession(host, privatePort, onConnected)
                } else {
                    onError("Failed to get private port from server.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown connection error")
            }
        }
    }
    
    private suspend fun startSession(host: String, port: Int, onConnected: () -> Unit) {
        val serverAddress = InetSocketAddress(host, port)
        val sessionSocket = aSocket(selectorManager).udp().connect(serverAddress)
        socket = sessionSocket
        
        val userInfo = UserInformation(++messageIdCounter, username, "Moose KMP Client", ConnectionType.LAN)
        sendBundle(V086Bundle.Single(userInfo))

        coroutineScope.launch {
            try {
                while (isActive) {
                    val datagram = sessionSocket.receive()
                    val bytes = datagram.packet.readBytes()
                    val source = Buffer().apply { write(bytes) }
                    
                    try {
                        val receivedBundle = V086BundleSerializer.read(source, -1, "ISO-8859-1")
                        handleBundle(receivedBundle, onConnected)
                    } catch (e: Exception) {
                        println("Failed to parse bundle: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("Session ended: ${e.message}")
            }
        }
    }

    private suspend fun sendBundle(bundle: V086Bundle) {
        writeProtocolLog("OUTGOING: $bundle")
        val buffer = Buffer()
        V086BundleSerializer.write(buffer, bundle, "ISO-8859-1")
        val bytes = buffer.readByteArray()
        socket?.send(Datagram(io.ktor.utils.io.core.ByteReadPacket(bytes), socket!!.remoteAddress))
    }

    private suspend fun handleBundle(bundle: V086Bundle, onConnected: () -> Unit) {
        writeProtocolLog("INCOMING: $bundle")
        val messages = when (bundle) {
            is V086Bundle.Single -> listOf(bundle.message)
            is V086Bundle.Multi -> bundle.messages.filterNotNull()
        }
        
        for (msg in messages) {
            when (msg) {
                is ServerAck -> {
                    sendBundle(V086Bundle.Single(ClientAck(++messageIdCounter)))
                    onConnected()
                }
                is UserJoined -> {
                    val newUser = MooseUser(msg.username, AccessLevel.USER)
                    _users.value = (_users.value + newUser).distinctBy { it.username }
                }
                is QuitNotification -> {
                    _users.value = _users.value.filter { it.username != msg.username }
                }
                is ChatNotification -> {
                    val sender = _users.value.find { it.username == msg.username }
                    _chatMessages.value = _chatMessages.value + ChatMessage(
                        sender = sender,
                        timestamp = "Now",
                        content = msg.message,
                        type = MessageType.USER_TEXT
                    )
                }
                // Handle basic game states
                is CreateGameNotification -> {
                    val game = MooseGame(msg.gameId.toString(), "Game ${msg.gameId}", io.github.hopskipnfall.kaillera.protocol.model.GameStatus.WAITING, emptyList())
                    _games.value = (_games.value + game).distinctBy { it.id }
                }
                is CloseGame -> {
                    _games.value = _games.value.filter { it.id != msg.gameId.toString() }
                }
            }
        }
    }
    
    fun sendChat(message: String) {
        coroutineScope.launch {
            sendBundle(V086Bundle.Single(ChatRequest(++messageIdCounter, message)))
        }
    }

    fun disconnect() {
        socket?.close()
        socket = null
    }
}

package org.moose.client.network

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun writeProtocolLog(message: String) {
    try {
        val file = File("kaillera_protocol_log.txt")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        file.appendText("[$timestamp] $message\n")
    } catch (e: Exception) {
        println("Failed to write to log file: ${e.message}")
    }
}

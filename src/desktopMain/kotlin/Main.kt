import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.moose.client.ui.App
import org.moose.client.network.KailleraClient
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.cancel

fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val client = KailleraClient(coroutineScope)

    Window(
        onCloseRequest = {
            client.disconnect()
            exitApplication()
        },
        title = "Moose Game Server Client"
    ) {
        App(client)
    }
}

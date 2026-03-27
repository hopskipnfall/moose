import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.moose.client.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Moose Game Server Client"
    ) {
        App()
    }
}

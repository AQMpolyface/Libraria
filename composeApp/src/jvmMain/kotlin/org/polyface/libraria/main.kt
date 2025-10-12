package org.polyface.libraria

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Libraria",
    ) {
        App()
    }
}
@Composable
actual fun HideSystemUI() {}
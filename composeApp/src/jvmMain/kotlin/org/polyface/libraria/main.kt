package org.polyface.libraria

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

object MuPdfNativeLoader {
    init {
        try {
            System.loadLibrary("mupdf_java")
            println("MuPDF native library loaded successfully!")
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }
}


fun main() = application {
    MuPdfNativeLoader
    Window(
        onCloseRequest = ::exitApplication,
        title = "Libraria",
    ) {
        App()
    }
}
@Composable
actual fun HideSystemUI() {}
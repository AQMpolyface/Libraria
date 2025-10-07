package org.polyface.libraria

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skiko.toBitmap
import java.io.File
import java.awt.Desktop
import java.io.ByteArrayOutputStream

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Libraria",
    ) {
        App()
    }
}

actual  fun listFiles(path : String? ): Array<String> {
    val file = File(filesDir)
    if (!file.exists()) {
        file.mkdir()
    }

    val ff = file.listFiles() ?.filter { !it.isDirectory && it.isFile && it.name.endsWith(".pdf", ignoreCase = true) }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()


    return ff
}

fun getAppDataDir(): File {
    val home = System.getProperty("user.home")
    val os = System.getProperty("os.name").lowercase()

    val baseDir = when {
        os.contains("win") -> File(System.getenv("APPDATA") ?: "$home/AppData/Roaming")
        os.contains("mac") -> File("$home/Library/Application Support")
        else -> File("$home/.local/share") // Linux/BSD etc.
    }

    val appDir = File(baseDir, "MyAppName")
    if (!appDir.exists()) appDir.mkdirs()

    return appDir
}


actual fun getBaseDirectory(): String {
    return getAppDataDir().path
}
actual fun openFile(file: File, appIdentifier: String?) {
    if (!file.exists()) return

    try {
        if (appIdentifier != null) {
            // Open with specific app
            ProcessBuilder(appIdentifier, file.absolutePath).start()
        } else {
            // Open with default app
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


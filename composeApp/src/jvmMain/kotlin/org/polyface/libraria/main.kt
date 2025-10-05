package org.polyface.libraria

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skiko.toBitmap
import java.io.File
import java.awt.Desktop

actual var targetFiles : Array<String> = arrayOf("")

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Libraria",
    ) {
        App()
        targetFiles = listFiles()
    }
}

fun listFiles(): Array<String> {
    val file = File("./files")
    if (!file.exists()) {
        file.mkdir()
    }

    return file.listFiles() ?.filter { !it.isDirectory &&it.isFile && it.name.endsWith(".pdf", ignoreCase = true) }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()
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



actual fun renderPdfPage(path: String, page: Int): ImageBitmap? {
    val doc = PDDocument.load(File(path))
    val renderer = PDFRenderer(doc)
    val awtImage = renderer.renderImageWithDPI(page, 150f)
    doc.close()
    return awtImage.toBitmap().asComposeImageBitmap()
}

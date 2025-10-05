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
    return File("./files").listFiles().map { it.toString() }.toTypedArray()
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



@Composable
actual fun FilePicker() {

    Button(onClick = {
        val fileDialog = java.awt.FileDialog(null as java.awt.Frame?, "Pick a file")
        fileDialog.filenameFilter = java.io.FilenameFilter { _, name ->
            name.lowercase().endsWith(".pdf")
        }

        fileDialog.isVisible = true
        if (fileDialog.file != null) {
            val chosen = "${fileDialog.directory}${fileDialog.file}"
            println(chosen)
            moveFile(chosen)
            targetFiles = listFiles()

        }
    }) {
        Text("Pick a file")
    }
}
 fun moveFile(path : String) {
    val oldFile = File(path)
     val newFile = File("./files", oldFile.name)
     oldFile.copyTo(newFile)
}
// jvmMain
actual fun renderPdfPage(path: String, page: Int): ImageBitmap? {
    val doc = PDDocument.load(File(path))
    val renderer = PDFRenderer(doc)
    val awtImage = renderer.renderImageWithDPI(page, 150f)
    doc.close()
    return awtImage.toBitmap().asComposeImageBitmap()
}

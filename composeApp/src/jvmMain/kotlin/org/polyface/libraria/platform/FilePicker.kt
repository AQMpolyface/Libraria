package org.polyface.libraria.platform

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.polyface.libraria.baseDirectory
import org.polyface.libraria.filesDir
import java.io.File


@Composable
actual fun FilePicker(lambda: () -> Unit) {

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
            lambda()

        }
    }) {
        Text("Pick a file")
    }
}
fun moveFile(path : String) {
    val oldFile = File(path)
    val newFile = File("$baseDirectory$filesDir${oldFile.name}")
    oldFile.copyTo(newFile)
}

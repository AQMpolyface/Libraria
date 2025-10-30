package org.polyface.libraria.shared

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.polyface.libraria.filesDir
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun FilePicker(lambda: () -> Unit) {
    Button(onClick = {
        val dialog = FileDialog(null as Frame?, "Select File", FileDialog.LOAD)
        dialog.isVisible = true
        val file = dialog.file
        val directory = dialog.directory
        if (file != null && directory != null) {
            val sourceFile = File(directory, file)
            val destFile = File(filesDir, file)
            sourceFile.copyTo(destFile, overwrite = true)
            lambda()
        }
    }) {
        Text("Add book")
    }
}

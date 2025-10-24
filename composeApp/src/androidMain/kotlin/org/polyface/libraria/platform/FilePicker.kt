package org.polyface.libraria.platform

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File


@Composable
actual fun FilePicker(lambda: () -> Unit) {
    val context = LocalContext.current
    val result = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }

    Column {
        Button(onClick = { launcher.launch(arrayOf("application/pdf")) }) {
            Text("Select Document")
        }

        result.value?.let { uri ->
            println(uri)
            // Copy to app-internal storage
            LaunchedEffect(uri) {
                moveFile(uri, context, getFileName(context, uri))
                lambda()
            }
        }
    }
}

fun moveFile(uri: Uri, context: Context, newFileName: String) {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")

    val newFile = File(context.filesDir, newFileName)

    newFile.parentFile?.mkdirs()

    inputStream.use { input ->
        newFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    println("Successfully copied file to: ${newFile.absolutePath}")
}

fun getFileName(context: Context, uri: Uri): String {
    var name = ""
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
    } else if (uri.scheme == "file") {
        name = File(uri.path!!).name
    }
    return name
}

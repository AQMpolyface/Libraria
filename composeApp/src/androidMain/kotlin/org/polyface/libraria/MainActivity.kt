package org.polyface.libraria

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import java.io.File
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.asImageBitmap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap

lateinit var appContext: Context // Initialize this in your Android Application class

// AndroidMain
actual var targetFiles = arrayOf<String>()

fun initTargetDir(context: Context) {
    targetFiles = context.filesDir.listFiles()
        ?.filter { !it.isDirectory && it.isFile && it.name.endsWith(".pdf", ignoreCase = true) }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()
}

// In your Activity onCreate:
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize targetDir at runtime
        initTargetDir(this)

        setContent {
            App()
        }
    }
}
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
*/
@Preview
@Composable
fun AppAndroidPreview() {
    val context = LocalContext.current
    targetFiles = listFiles(context)

    App()

}
fun listFiles(context : Context): Array<String> {
    return context.filesDir
        .listFiles()
        ?.filter { it.isFile && it.name.endsWith(".pdf", ignoreCase = true)  }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()
}

actual fun openFile(file: File, appIdentifier: String?) {
    if (!file.exists()) return

    val uri: Uri =
        FileProvider.getUriForFile(appContext, "${appContext.packageName}.provider", file)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, getMimeType(file))
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (!appIdentifier.isNullOrEmpty()) {
            setPackage(appIdentifier)
        }
    }

    if (intent.resolveActivity(appContext.packageManager) != null) {
        appContext.startActivity(intent)
    } else {
        println("No app found to open file: ${file.name}")
    }
}

private fun getMimeType(file: File): String = when (file.extension.lowercase()) {
    "txt" -> "text/plain"
    "pdf" -> "application/pdf"
    "jpg", "jpeg" -> "image/jpeg"
    "png" -> "image/png"
    else -> "*/*"
}

actual fun renderPdfPage(path: String, page: Int): ImageBitmap? {
    println("path : $path")

    val file = File(path)

    val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    val renderer = PdfRenderer(fileDescriptor)
    val pageObj = renderer.openPage(page)
    val bitmap = createBitmap(pageObj.width, pageObj.height)
    pageObj.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    pageObj.close()
    renderer.close()
    return bitmap.asImageBitmap()
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





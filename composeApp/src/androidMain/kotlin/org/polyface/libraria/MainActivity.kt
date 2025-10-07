package org.polyface.libraria

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import java.io.ByteArrayOutputStream
import java.io.File

private lateinit var appContext: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize targetDir at runtime
        initFileListing(applicationContext)
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
    initFileListing(context)

    App()

}


fun initFileListing(context: Context) {
    appContext = context.applicationContext
}

actual fun listFiles(path: String?): Array<String> {
    val dir = if (path != null) File(path) else appContext.filesDir
    return dir.listFiles() ?.filter { !it.isDirectory && it.isFile && it.name.endsWith(".pdf", ignoreCase = true) }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()
}

actual fun getBaseDirectory(): String {
    return appContext.filesDir.path
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
/*
actual fun renderPdfPage(path: String, page: Int): ImageBitmap? {
    val file = File(path)
    if (!file.exists()) return null

    val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    val renderer = PdfRenderer(fileDescriptor)

    if (page < 0 || page >= renderer.pageCount) {
        renderer.close()
        fileDescriptor.close()
        return null
    }

    val pageObj = renderer.openPage(page)
    val bitmap = createBitmap(pageObj.width, pageObj.height)

    pageObj.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    pageObj.close()
    renderer.close()
    fileDescriptor.close()

    // Convert to PNG bytes
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    stream.close()

    // Ensure output directory exists
    val picDir = File(baseDirectory, pictureDir)
    if (!picDir.exists()) picDir.mkdirs()

    // Generate output filename safely
    val baseName = file.nameWithoutExtension
    val picFile = File(picDir, "$baseName-page$page.png")

    picFile.writeBytes(byteArray)

    return bitmap.asImageBitmap()
}*/






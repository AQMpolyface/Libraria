package org.polyface.libraria

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException


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
/*
@Throws(IOException::class)
fun openFile(context: Context, url: File) {
    // Create URI
    val file: File? = url
    val uri = Uri.fromFile(file)

    val intent = Intent(Intent.ACTION_VIEW)
    // Check what kind of file you are trying to open, by comparing the url with extensions.
    // When the if condition is matched, plugin sets the correct intent (mime) type,
    // so Android knew what application to use to open the file
    if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
        // Word document
        intent.setDataAndType(uri, "application/msword")
    } else if (url.toString().contains(".pdf")) {
        // PDF file
        intent.setDataAndType(uri, "application/pdf")
    } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
        // Powerpoint file
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
    } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
        // Excel file
        intent.setDataAndType(uri, "application/vnd.ms-excel")
    } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
        // WAV audio file
        intent.setDataAndType(uri, "application/x-wav")
    } else if (url.toString().contains(".rtf")) {
        // RTF file
        intent.setDataAndType(uri, "application/rtf")
    } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
        // WAV audio file
        intent.setDataAndType(uri, "audio/x-wav")
    } else if (url.toString().contains(".gif")) {
        // GIF file
        intent.setDataAndType(uri, "image/gif")
    } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString()
            .contains(".png")
    ) {
        // JPG file
        intent.setDataAndType(uri, "image/jpeg")
    } else if (url.toString().contains(".txt")) {
        // Text file
        intent.setDataAndType(uri, "text/plain")
    } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString()
            .contains(".mpeg") || url.toString().contains(".mpe") || url.toString()
            .contains(".mp4") || url.toString().contains(".avi")
    ) {
        // Video files
        intent.setDataAndType(uri, "video/*")
    } else {
        //if you want you can also define the intent type for any other file

        //additionally use else clause below, to manage other unknown extensions
        //in this case, Android will show all applications installed on the device
        //so you can choose which application to use

        intent.setDataAndType(uri, "*/*")
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}*/
actual fun openFile(file: File, appIdentifier: String?) {
    if (!file.exists()) return

    val uri: Uri =
        FileProvider.getUriForFile(appContext, "${appContext.packageName}.provider", file)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, getMimeType(file))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!appIdentifier.isNullOrEmpty()) {
            setPackage(appIdentifier)
        }
    }

    try {
        appContext.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        println("No app found to open file: ${file.name}")
    }
}
/*
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
}*/

private fun getMimeType(file: File): String = when (file.extension.lowercase()) {
    "txt" -> "text/plain"
    "pdf" -> "application/pdf"
    "jpg", "jpeg" -> "image/jpeg"
    "png" -> "image/png"
    else -> "*/*"
}






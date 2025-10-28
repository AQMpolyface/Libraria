package org.polyface.libraria.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import org.polyface.libraria.SUPPORTED_FORMAT
import java.io.File
import kotlin.collections.contains

private lateinit var appContext: Context

fun initFileListing(context: Context) {
    appContext = context.applicationContext
}

actual fun listFiles(path: String?): Array<String> {
    val dir = if (path != null) File(path) else appContext.filesDir
    return dir.listFiles() ?.filter { !it.isDirectory && it.isFile && SUPPORTED_FORMAT.contains(it.name.substringAfterLast(".").lowercase())   }
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

private fun getMimeType(file: File): String = when (file.extension.lowercase()) {
    "txt"  -> "text/plain"
    "pdf"  -> "application/pdf"
    "jpg", "jpeg" -> "image/jpeg"
    "png"  -> "image/png"

    "epub" -> "application/epub+zip"
    "fb2"  -> "application/x-fictionbook+xml"
    "mobi" -> "application/x-mobipocket-ebook"
    "cbz"  -> "application/vnd.comicbook+zip"
    "xps"  -> "application/oxps"

    else   -> "*/*"
}



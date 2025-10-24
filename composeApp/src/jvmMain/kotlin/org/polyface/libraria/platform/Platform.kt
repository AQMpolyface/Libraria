package org.polyface.libraria.platform

import org.polyface.libraria.filesDir
import java.awt.Desktop
import java.io.File


import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
actual  fun listFiles(path : String? ): Array<String> {
    println("listing")
    val file = File(filesDir)
    if (!file.exists()) {
        file.mkdir()
    }
    val space = " "
    println("list : ${file.listFiles().joinToString(space)}")
println(file.absolutePath)
    val ff = file.listFiles() ?.filter { !it.isDirectory && it.isFile && it.name.endsWith(".pdf", ignoreCase = true) }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()

println("resut : ${ff.joinToString { s -> s }}")
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

    val appDir = File(baseDir, "Libraria")
    if (!appDir.exists()) appDir.mkdirs()

    return appDir
}


actual fun getBaseDirectory(): String {
    return getAppDataDir().path + "/"
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

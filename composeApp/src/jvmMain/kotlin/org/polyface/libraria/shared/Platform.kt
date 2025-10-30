package org.polyface.libraria.shared

import org.polyface.libraria.filesDir
import java.awt.Desktop
import java.io.File


import org.polyface.libraria.SUPPORTED_FORMAT

class JvmPlatform : Platform {
    override val name: String = "JVM"
    override fun openUrl(url: String) {
        val desktop = Desktop.getDesktop()
        desktop.browse(java.net.URI.create(url))
    }
}

actual fun getPlatform(): Platform = JvmPlatform()

actual  fun listFiles(path : String? ): Array<String> {
    val file = File(filesDir)
    if (!file.exists()) {
        file.mkdir()
    }

    val ff = file.listFiles() ?.filter { !it.isDirectory && it.isFile && SUPPORTED_FORMAT.contains(it.name.substringAfterLast(".").lowercase())  }
        ?.map { it.absolutePath }
        ?.toTypedArray()
        ?: emptyArray()

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

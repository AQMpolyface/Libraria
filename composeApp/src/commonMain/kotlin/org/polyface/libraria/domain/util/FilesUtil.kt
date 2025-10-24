package org.polyface.libraria.domain.util

import org.polyface.libraria.baseDirectory
import org.polyface.libraria.filesDir
import java.io.File

fun deleteFileFromPath(path: String) {
    val file = File(path)
    val picFile = File("$filesDir${path.substringAfterLast("/").substringBeforeLast(".")}.png")

    picFile.delete()
    file.delete()
    println("yay deleted ${picFile.path} and ${file.path}")
}

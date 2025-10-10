package org.polyface.libraria

import java.io.File

fun deleteFileFromPath(path: String) {
    val file = File(path)
    val picFile = File("$baseDirectory$filesDir${path.substringAfterLast("/").substringBeforeLast(".")}.png")
    picFile.delete()
    file.delete()
    println("yay deleted ${picFile.path} ans ${file.path}")
}
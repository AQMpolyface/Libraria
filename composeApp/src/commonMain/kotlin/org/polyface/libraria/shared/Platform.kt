package org.polyface.libraria.shared

import java.io.File

interface Platform {
    val name: String
    fun openUrl(url: String)
}

expect fun getPlatform(): Platform

expect fun getBaseDirectory(): String

expect fun listFiles(path : String? = null) : Array<String>

package org.polyface.libraria

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
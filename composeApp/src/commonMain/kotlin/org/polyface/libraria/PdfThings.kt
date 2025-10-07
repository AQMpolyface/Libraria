package org.polyface.libraria

import androidx.compose.ui.graphics.ImageBitmap

expect  fun getBaseDirectory(): String
expect fun pngBitmapForPdf(path: String) : ImageBitmap
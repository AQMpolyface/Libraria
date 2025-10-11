package org.polyface.libraria.platform

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

expect  fun getBaseDirectory(): String
expect fun pngBitmapForPdf(path: String) : ImageBitmap

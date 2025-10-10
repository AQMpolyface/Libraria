package org.polyface.libraria

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

expect  fun getBaseDirectory(): String
expect fun pngBitmapForPdf(path: String) : ImageBitmap

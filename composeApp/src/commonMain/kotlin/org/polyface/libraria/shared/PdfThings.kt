package org.polyface.libraria.shared

import androidx.compose.ui.graphics.ImageBitmap

expect fun pngBitmapForPdf(path: String) : ImageBitmap

expect fun writePdfPageAsPng(pdfpath: String, pngPath: String, pageNumber: Int = 0)

package org.polyface.libraria.shared

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import com.artifex.mupdf.fitz.ColorSpace
import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
import com.artifex.mupdf.fitz.android.AndroidDrawDevice
import com.artifex.mupdf.fitz.Page
import org.polyface.libraria.pictureDir
import java.io.File

actual fun writePdfPageAsPng(pdfpath: String, pngPath : String,  pageNumber: Int) {
    val doc = Document.openDocument(pdfpath)
    val page = doc.loadPage(pageNumber)
    val matrix = Matrix(1f, 1f)
    val pixmap = page.toPixmap(matrix, ColorSpace.DeviceRGB, false)
   // AndroidDrawDevice.drawPage(page, matrix)
    pixmap.saveAsPNG(pngPath)
}

actual fun pngBitmapForPdf(path: String) : ImageBitmap {
    val picDir = File(pictureDir)
    if (!picDir.exists()) {
        picDir.mkdir()
    }

    val actualFileName = "$pictureDir${path.substringAfterLast('/').substringBeforeLast(".")}.png"

    val picFile = File(actualFileName)
    if (picFile.exists()) {
        return picFile.readBytes().decodeToImageBitmap()
    }

    writePdfPageAsPng(path, actualFileName)

    return picFile.readBytes().decodeToImageBitmap()
}

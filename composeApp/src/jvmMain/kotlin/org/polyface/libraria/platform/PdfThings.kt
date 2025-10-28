package org.polyface.libraria.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import com.artifex.mupdf.fitz.ColorSpace
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skiko.toBitmap
import org.polyface.libraria.baseDirectory
import org.polyface.libraria.pictureDir
import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
import java.awt.Color
import java.io.File


actual fun writePdfPageAsPng(pdfpath: String, pngPath : String,  pageNumber: Int ) {
    val doc = Document.openDocument(pdfpath)
    println("pages ${doc.countPages()}")
    val page = doc.loadPage(pageNumber)
    val matrix = Matrix(1f, 1f)
    val pixmap = page.toPixmap(matrix, ColorSpace.DeviceRGB, false)
    
    pixmap.saveAsPNG(pngPath)

    return
}

actual fun pngBitmapForPdf(path: String) : ImageBitmap {
    println("render")
    val picDir = File(pictureDir)
    if (!picDir.exists()) {
        picDir.mkdir()
    }

    val actualFileName = "$pictureDir${path.substringAfterLast('/').substringBeforeLast(".")}.png"
    println(" baseDirectory = $baseDirectory, pictureDir = $pictureDir")
    println("actualFileName = $actualFileName")

    val picFile = File(actualFileName)
    if (picFile.exists()) {
        return picFile.readBytes().decodeToImageBitmap()
    }

   writePdfPageAsPng(path, actualFileName)

    return picFile.readBytes().decodeToImageBitmap()
}


package org.polyface.libraria.platform

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.core.graphics.createBitmap
import org.polyface.libraria.pictureDir
import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
import java.io.ByteArrayOutputStream
import com.artifex.mupdf.fitz.ColorSpace
import java.io.File


actual fun writePdfPageAsPng(pdfpath: String, pngPath : String,  pageNumber: Int) {
    val doc = Document.openDocument(pdfpath)
    println("pages ${doc.countPages()}")
    val page = doc.loadPage(pageNumber)
    val matrix = Matrix(1f, 1f)

    val pixmap = page.toPixmap(matrix, ColorSpace.DeviceRGB, false)

    pixmap.saveAsPNG(pngPath)

    return
}

fun renderPdfPage(path: String, page: Int = 0): Bitmap {

    val file = File(path)

    val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    val renderer = PdfRenderer(fileDescriptor)
    val pageObj = renderer.openPage(page)
    val bitmap = createBitmap(pageObj.width, pageObj.height)

    pageObj.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    pageObj.close()
    renderer.close()
    fileDescriptor.close()


    return bitmap
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

   /* val bitmap = renderPdfPage(path)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    picFile.writeBytes(byteArray)*/
    writePdfPageAsPng(path, actualFileName)
    return picFile.readBytes().decodeToImageBitmap()
}

package org.polyface.libraria.platform

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.core.graphics.createBitmap
import org.polyface.libraria.baseDirectory
import org.polyface.libraria.pictureDir
import java.io.ByteArrayOutputStream
import java.io.File


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
    val picDir = File("$baseDirectory$pictureDir")
    if (!picDir.exists()) {
        picDir.mkdir()
    }
    val actualFileName = "$baseDirectory$pictureDir${path.substringAfterLast('/').substringBeforeLast(".")}.png"
    val picFile = File(actualFileName)
    if (picFile.exists()) {
        return picFile.readBytes().decodeToImageBitmap()
    }

    val bitmap = renderPdfPage(path)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    picFile.writeBytes(byteArray)
    return bitmap.asImageBitmap()
}

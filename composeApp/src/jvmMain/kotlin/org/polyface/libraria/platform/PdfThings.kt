package org.polyface.libraria.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skiko.toBitmap
import org.polyface.libraria.baseDirectory
import org.polyface.libraria.pictureDir
import java.io.File




fun renderPdfPage(path: String, page: Int): Bitmap {
    val doc = PDDocument.load(File(path))
    val renderer = PDFRenderer(doc)
    val awtImage = renderer.renderImageWithDPI(page, 150f)
    doc.close()
    val image = awtImage.toBitmap()

    return image
}

actual fun pngBitmapForPdf(path: String) : ImageBitmap {
    val picDir = File("$baseDirectory$pictureDir")
    if (!picDir.exists()) {
        picDir.mkdir()
    }

    val actualFileName = "$baseDirectory$pictureDir${path.substringAfterLast('/').substringBeforeLast(".")}.png"
    println(" baseDirectory = $baseDirectory, pictureDir = $pictureDir")

    println("actualFileName = $actualFileName")
    val picFile = File(actualFileName)
    if (picFile.exists()) {
        return picFile.readBytes().decodeToImageBitmap()
    }
    val pdfPage = renderPdfPage(path,0)

    val pngFileName = renderAndWritePng(pdfPage,path)
    val pngFile = File(pngFileName)
    return pngFile.readBytes().decodeToImageBitmap()
}

fun renderAndWritePng(image: Bitmap, fileName: String): String {
    val picDir = File("$baseDirectory$pictureDir")
    if (!picDir.exists()) {
        picDir.mkdir()
    }
    val actualFileName = "$baseDirectory$pictureDir${fileName.substringAfterLast('/').substringBeforeLast(".")}.png"
    val picFile = File(actualFileName)

    val bitmap = image.makeClone()
    val newImage: Image = Image.makeFromBitmap(bitmap)
    val data: Data? = newImage.encodeToData(EncodedImageFormat.PNG)
    val byteArray: ByteArray = data?.bytes ?: ByteArray(0)
    picFile.writeBytes(byteArray)
    println("yay wrote ${picFile.path}")
    return actualFileName
}

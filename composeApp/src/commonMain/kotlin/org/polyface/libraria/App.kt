package org.polyface.libraria

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.polyface.libraria.components.ConfirmPopup
import org.polyface.libraria.components.TrashButton
import org.polyface.libraria.domain.util.deleteFileFromPath
import org.polyface.libraria.shared.FilePicker
import org.polyface.libraria.shared.getBaseDirectory
import org.polyface.libraria.shared.listFiles
import org.polyface.libraria.shared.openFile
import org.polyface.libraria.shared.pngBitmapForPdf
import java.io.File


val baseDirectory: String by lazy { getBaseDirectory() }
val filesDir = "${baseDirectory}files/"
val pictureDir = "${baseDirectory}picture/"

val SUPPORTED_FORMAT = arrayOf("pdf", "mobi", "epub", "fb2", "cbz", "xps")
val SUPPORTED_MIME_TYPES = arrayOf(
    "application/pdf",                 // .pdf
    "application/epub+zip",            // .epub
    "application/x-fictionbook+xml",   // .fb2
    "application/x-mobipocket-ebook",  // .mobi
    "application/vnd.comicbook+zip",   // .cbz
    "application/oxps",                // .xps
)
@Composable
@Preview
fun App() {
    HideSystemUI()
    MaterialTheme {
        var files by remember { mutableStateOf(listFiles()) }
        var fileToDelete by remember { mutableStateOf<String?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(files.size) { index ->
                        val filePath = files[index]
                        val image = pngBitmapForPdf(filePath)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable(enabled = true, onClick = {
                                    openFile(File(filePath))
                                })
                                .padding(8.dp),

                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                bitmap = image,
                                contentDescription = "PDF preview",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TrashButton(onClick = {
                                    fileToDelete = filePath
                                })

                                Spacer(Modifier.width(1.dp))
                                Text(
                                    text = filePath.substringAfterLast("/"),
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Cyan)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilePicker(
                    lambda = {
                        files = listFiles()
                        
                        for (string in files) {
                            println(string)
                        }

                    }
                )
                Spacer(Modifier.padding(5.dp))
            }

            // show confirmation popup if a file was selected
            if (fileToDelete != null) {
                ConfirmPopup(
                    message = "Delete ${fileToDelete!!.substringAfterLast("/")}?",
                    onConfirm = {
                        deleteFileFromPath(fileToDelete!!)
                        files = files.filter { it != fileToDelete }.toTypedArray()

                        fileToDelete = null
                    },
                    onCancel = {
                        fileToDelete = null
                    }
                )
            }
        }
    }
}

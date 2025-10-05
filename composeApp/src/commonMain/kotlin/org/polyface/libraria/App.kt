package org.polyface.libraria

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.sun.jndi.toolkit.url.Uri
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import libraria.composeapp.generated.resources.Res
import libraria.composeapp.generated.resources.compose_multiplatform
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

expect var targetFiles : Array<String>
@Composable
@Preview
fun App() {

    MaterialTheme {
        var showAddPdf by remember { mutableStateOf(false) }
        val files = targetFiles
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(), // Make the Column take the whole screen,
                verticalArrangement = Arrangement.Center, // Center content vertically
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
                        val image = renderPdfPage(filePath)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f) // 👈 keeps cells square, change as needed
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { /* handle click if needed */ }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (image != null) {
                                Image(
                                    bitmap = image,
                                    contentDescription = "PDF preview",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop // 👈 makes images fill evenly
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Gray.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No preview")
                                }
                            }

                            Spacer(Modifier.height(6.dp))
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Cyan)
                    .padding(16.dp), // optional padding
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showAddPdf = true },
                ) {
                    Text("Add a pdf")
                }
                Spacer(Modifier.padding(5.dp))
                Button(
                    onClick = { /* action here */ }
                ) {
                    Text("Hewo 2")
                }
            }
            if (showAddPdf) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Magenta, shape = RoundedCornerShape(16.dp) )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FilePicker()

                    Button(
                        shape = ButtonDefaults.textShape,
                        onClick = { showAddPdf = false },
                    ) {
                        Text("Close")
                    }
                    }
            }
        }
    }
}

expect fun renderPdfPage(path: String, page: Int = 0): ImageBitmap?


@Composable
fun PdfPreview(path: String) {
    renderPdfPage(path)?.let { img ->
        Image(bitmap = img, contentDescription = "PDF Preview")
    }
}

/*
fun moveFile(file: String) {
    val source = File(file)

    val target = File(targetDir, source.name)

    // copy file contents
    source.copyTo(target, overwrite = true)
}

 */

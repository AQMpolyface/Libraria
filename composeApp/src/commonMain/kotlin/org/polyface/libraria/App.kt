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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.polyface.libraria.domain.util.deleteFileFromPath
import org.polyface.libraria.platform.FilePicker
import org.polyface.libraria.platform.getBaseDirectory
import org.polyface.libraria.platform.listFiles
import org.polyface.libraria.platform.openFile
import org.polyface.libraria.platform.pngBitmapForPdf
import java.io.File

val filesDir = "files/"
val pictureDir = "picture/"
val baseDirectory: String by lazy { getBaseDirectory() }
@OptIn(ExperimentalFoundationApi::class)
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


@Composable
fun TrashButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    }
}

    @Composable
    fun ConfirmPopup(
        message: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { onCancel() },
            title = { Text("Confirm Deletion") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        )
    }



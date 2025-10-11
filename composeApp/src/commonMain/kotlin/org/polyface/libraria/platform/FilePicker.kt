package org.polyface.libraria.platform

import androidx.compose.runtime.Composable
import java.io.File


expect fun openFile(file: File, appIdentifier: String? = null)

@Composable
expect fun FilePicker(lambda : () -> Unit)

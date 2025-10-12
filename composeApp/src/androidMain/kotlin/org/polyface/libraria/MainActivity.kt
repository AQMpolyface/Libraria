package org.polyface.libraria

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.polyface.libraria.platform.initFileListing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        /*window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }        // Initialize targetDir at runtime*/
        initFileListing(applicationContext)


        setContent {
            App()
        }
    }
}

@Composable
actual fun HideSystemUI() {
    val view = LocalView.current

    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as? Activity)?.window ?: return@LaunchedEffect
            val insetsController = WindowCompat.getInsetsController(window, view)

            // Correct: Use the constant from the compatibility library
            insetsController.hide(WindowInsetsCompat.Type.systemBars())

            // Correct: Use the constant from the compatibility library
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}



@Preview
@Composable
fun AppAndroidPreview() {
    val context = LocalContext.current
    initFileListing(context)
    App()

}

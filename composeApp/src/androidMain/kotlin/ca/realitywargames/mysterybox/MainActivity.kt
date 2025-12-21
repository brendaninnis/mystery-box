package ca.realitywargames.mysterybox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import ca.realitywargames.mysterybox.core.data.network.TokenStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Configure edge-to-edge with light status bar content (white icons/text)
        // for our dark theme UI
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = androidx.compose.ui.graphics.Color.Transparent.toArgb()
            )
        )
        super.onCreate(savedInstanceState)

        // Initialize secure token storage
        TokenStorage.initialize(applicationContext)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
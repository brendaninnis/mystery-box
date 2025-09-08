package ca.realitywargames.mysterybox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ca.realitywargames.mysterybox.ui.navigation.NavigationGraph
import ca.realitywargames.mysterybox.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MysteryBoxTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavigationGraph()
        }
    }
}
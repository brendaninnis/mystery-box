package ca.realitywargames.mysterybox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Mystery Box Gradient - the beautiful dark gradient used throughout the app
val MysteryGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F0F23), // Very dark blue
        Color(0xFF16213E), // Darker blue
        Color(0xFF1A1A2E), // Dark blue
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B6B), // Coral red - mystery and danger
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2A2A3E), // Dark blue-gray
    onPrimaryContainer = Color(0xFFFF6B6B),
    secondary = Color(0xFF4ECDC4), // Teal - investigation and logic
    onSecondary = Color(0xFF0F0F23), // Very dark blue
    tertiary = Color(0xFF45B7D1), // Light blue - evidence and inventory
    onTertiary = Color(0xFF0F0F23),
    error = Color(0xFFFF6B6B),
    onError = Color.White,
    background = Color(0xFF0F0F23), // Very dark blue - mysterious atmosphere
    onBackground = Color.White,
    surface = Color(0xFF1A1A2E), // Dark blue-gray - card backgrounds
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A3E), // Slightly lighter dark blue-gray
    onSurfaceVariant = Color(0xFF4ECDC4),
    outline = Color(0xFF4ECDC4).copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F), // Dark red - mystery and danger
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEE), // Light red background
    onPrimaryContainer = Color(0xFFD32F2F),
    secondary = Color(0xFF00897B), // Dark teal - investigation and logic
    onSecondary = Color.White,
    tertiary = Color(0xFF0277BD), // Dark blue - evidence and inventory
    onTertiary = Color.White,
    error = Color(0xFFD32F2F),
    onError = Color.White,
    background = Color(0xFF263238), // Dark blue-gray - still mysterious but lighter
    onBackground = Color.White,
    surface = Color(0xFF37474F), // Medium blue-gray - card backgrounds
    onSurface = Color.White,
    surfaceVariant = Color(0xFF455A64), // Slightly lighter blue-gray
    onSurfaceVariant = Color(0xFF4DB6AC), // Light teal for accents
    outline = Color(0xFF4DB6AC).copy(alpha = 0.7f)
)

@Composable
fun MysteryBoxTheme(
    darkTheme: Boolean = true, // Force dark theme for mysterious atmosphere
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

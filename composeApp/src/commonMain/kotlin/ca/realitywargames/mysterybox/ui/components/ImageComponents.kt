package ca.realitywargames.mysterybox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

/**
 * A cross-platform image component that handles loading states and errors using Coil
 */
@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                placeholder?.invoke() ?: DefaultLoadingPlaceholder()
            }
            is AsyncImagePainter.State.Error -> {
                error?.invoke() ?: DefaultErrorPlaceholder()
            }
            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

/**
 * Default loading placeholder
 */
@Composable
private fun DefaultLoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFFF6B6B),
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Default error placeholder
 */
@Composable
private fun DefaultErrorPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2A2A3E)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFFF6B6B)
            )
            Text(
                text = "Failed to load image",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * Utility function to create a circular network image
 */
@Composable
fun CircularNetworkImage(
    url: String,
    contentDescription: String?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    NetworkImage(
        url = url,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 2)),
        contentScale = ContentScale.Crop
    )
}

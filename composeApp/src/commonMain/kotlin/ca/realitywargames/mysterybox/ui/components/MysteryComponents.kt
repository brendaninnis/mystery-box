package ca.realitywargames.mysterybox.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.MysteryPackage

@Composable
fun MysteryInfoRow(
    mysteryPackage: MysteryPackage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Face,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF4ECDC4) // Teal
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${mysteryPackage.minPlayers}-${mysteryPackage.maxPlayers} players",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4ECDC4)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF45B7D1) // Blue
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${mysteryPackage.durationMinutes} min",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF45B7D1)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = when (mysteryPackage.difficulty) {
                    Difficulty.EASY -> Color(0xFF4CAF50) // Green
                    Difficulty.MEDIUM -> Color(0xFFFF9800) // Orange
                    Difficulty.HARD -> Color(0xFFFF6B6B) // Red
                    Difficulty.EXPERT -> Color(0xFF9C27B0) // Purple
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = mysteryPackage.difficulty.name,
                style = MaterialTheme.typography.bodyLarge,
                color = when (mysteryPackage.difficulty) {
                    Difficulty.EASY -> Color(0xFF4CAF50)
                    Difficulty.MEDIUM -> Color(0xFFFF9800)
                    Difficulty.HARD -> Color(0xFFFF6B6B)
                    Difficulty.EXPERT -> Color(0xFF9C27B0)
                }
            )
        }
    }
}

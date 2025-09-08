package ca.realitywargames.mysterybox.ui.screens.mysteries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.data.models.*
import ca.realitywargames.mysterybox.ui.theme.MysteryGradient
import ca.realitywargames.mysterybox.ui.viewmodel.MysteryViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MysteryDetailScreen(
    mysteryId: String,
    navController: NavHostController,
    viewModel: MysteryViewModel,
    onBackClick: () -> Unit
) {
    val mockMystery = MysteryPackage(
        id = mysteryId,
        title = "The Haunted Mansion Mystery",
        description = "A spine-chilling murder mystery set in a decaying Victorian mansion. When the wealthy patriarch is found dead in his study, guests must unravel a web of family secrets, forbidden romances, and hidden motives before the killer strikes again.",
        imageUrl = "", // Would be populated in real implementation
        price = 29.99,
        currency = "USD",
        durationMinutes = 180,
        minPlayers = 6,
        maxPlayers = 10,
        difficulty = Difficulty.HARD,
        themes = listOf("Haunted House", "Family Secrets", "Forbidden Love", "Inheritance"),
        plotSummary = "The Harrington family gathers for the reading of the will, but when the patriarch is found murdered, dark secrets from the past come to light...",
        characters = listOf(
            CharacterTemplate(
                id = "detective",
                name = "Detective Sarah Miller",
                description = "A sharp-witted private investigator hired to look into the Harrington family affairs",
                avatarUrl = "",
                role = CharacterRole.DETECTIVE,
                background = "Former police detective turned private investigator",
                personality = "Analytical, persistent, and sometimes abrasive",
                objectives = listOf("Solve the murder", "Protect the innocent", "Uncover family secrets"),
                secrets = listOf("Has a personal connection to the victim", "Is investigating the family for fraud")
            ),
            CharacterTemplate(
                id = "widow",
                name = "Victoria Harrington",
                description = "The grieving widow who stands to inherit the entire estate",
                avatarUrl = "",
                role = CharacterRole.SUSPECT,
                background = "Married to the victim for 25 years",
                personality = "Elegant, composed, but with a steely determination",
                objectives = listOf("Secure her inheritance", "Protect her children"),
                secrets = listOf("Was having an affair", "Knows about the victim's illegitimate child")
            ),
            CharacterTemplate(
                id = "son",
                name = "Marcus Harrington",
                description = "The ambitious eldest son who runs the family business",
                avatarUrl = "",
                role = CharacterRole.SUSPECT,
                background = "MBA from Harvard, executive at Harrington Enterprises",
                personality = "Ambitious, charming, but ruthless when crossed",
                objectives = listOf("Take control of the company", "Prove his business acumen"),
                secrets = listOf("Embezzled company funds", "Plans to sell the family business")
            ),
            CharacterTemplate(
                id = "daughter",
                name = "Isabella Harrington",
                description = "The rebellious daughter who left home years ago",
                avatarUrl = "",
                role = CharacterRole.SUSPECT,
                background = "Artist living in Paris, estranged from family",
                personality = "Creative, free-spirited, but harbors deep resentment",
                objectives = listOf("Get revenge on her family", "Secure her share of inheritance"),
                secrets = listOf("Blackmailed her father", "Has a secret child")
            ),
            CharacterTemplate(
                id = "butler",
                name = "Charles Jenkins",
                description = "The loyal butler who has served the family for 30 years",
                avatarUrl = "",
                role = CharacterRole.WITNESS,
                background = "Former military man, impeccable service record",
                personality = "Stoic, reliable, but observant of everything",
                objectives = listOf("Maintain family honor", "Protect the household"),
                secrets = listOf("Knows all family secrets", "Has been blackmailing the victim")
            ),
            CharacterTemplate(
                id = "maid",
                name = "Emma Thompson",
                description = "The young maid who recently joined the household",
                avatarUrl = "",
                role = CharacterRole.SUSPECT,
                background = "Grew up in poverty, working to support her family",
                personality = "Innocent appearance, but cunning and ambitious",
                objectives = listOf("Secure financial stability", "Rise above her station"),
                secrets = listOf("Was the victim's secret lover", "Is pregnant with his child")
            )
        ),
        phases = emptyList(), // Would be populated in real implementation
        isAvailable = true,
        createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
        updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("Mystery Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MysteryGradient),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for hero image - in real app this would be loaded from imageUrl
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = Color(0xFFFF6B6B) // Coral red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🎭 MYSTERY 🎭",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color(0xFFFF6B6B)
                            )
                        }
                    }

                    // Price tag overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(
                                Color(0xFFFF6B6B),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$${mockMystery.price}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title and Basic Info
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = mockMystery.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                text = "${mockMystery.minPlayers}-${mockMystery.maxPlayers} players",
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
                                text = "${mockMystery.durationMinutes} min",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF45B7D1)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = when (mockMystery.difficulty) {
                                    Difficulty.EASY -> Color(0xFF4CAF50) // Green
                                    Difficulty.MEDIUM -> Color(0xFFFF9800) // Orange
                                    Difficulty.HARD -> Color(0xFFFF6B6B) // Red
                                    Difficulty.EXPERT -> Color(0xFF9C27B0) // Purple
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = mockMystery.difficulty.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = when (mockMystery.difficulty) {
                                    Difficulty.EASY -> Color(0xFF4CAF50)
                                    Difficulty.MEDIUM -> Color(0xFFFF9800)
                                    Difficulty.HARD -> Color(0xFFFF6B6B)
                                    Difficulty.EXPERT -> Color(0xFF9C27B0)
                                }
                            )
                        }
                    }
                }
            }

            // Description
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "About This Mystery",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = mockMystery.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        lineHeight = androidx.compose.ui.unit.TextUnit(1.6f, androidx.compose.ui.unit.TextUnitType.Em)
                    )
                }
            }

            // Themes
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Themes",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mockMystery.themes.forEach { theme ->
                            Surface(
                                color = Color(0xFF2A2A3E),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFF4ECDC4))
                            ) {
                                Text(
                                    text = theme,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF4ECDC4),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Characters Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Characters",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Choose your character for this mystery",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            // Characters Horizontal List
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mockMystery.characters) { character ->
                        CharacterCard(character = character)
                    }
                }
            }

            // Plot Summary
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Plot Summary",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = Color(0xFF1E1E2E),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF4ECDC4).copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = mockMystery.plotSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            lineHeight = androidx.compose.ui.unit.TextUnit(1.6f, androidx.compose.ui.unit.TextUnitType.Em),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Action Buttons
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(ca.realitywargames.mysterybox.ui.navigation.NavRoutes.createParty(mysteryId))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B6B)
                        )
                    ) {
                        Text(
                            text = "Create Party",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            // Could navigate to preview or sample game
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(2.dp, Color(0xFF4ECDC4))
                    ) {
                        Text(
                            text = "Preview Mystery",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4ECDC4)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCard(character: CharacterTemplate) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(280.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E2E)
        ),
        border = BorderStroke(1.dp, Color(0xFF4ECDC4).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Character avatar placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = when (character.role) {
                            CharacterRole.DETECTIVE -> Color(0xFF4ECDC4) // Teal for detective
                            CharacterRole.SUSPECT -> Color(0xFFFF6B6B) // Red for suspect
                            CharacterRole.WITNESS -> Color(0xFF45B7D1) // Blue for witness
                            CharacterRole.VICTIM -> Color(0xFF9C27B0) // Purple for victim
                            CharacterRole.HOST -> Color(0xFFFF9800) // Orange for host
                        },
                        shape = RoundedCornerShape(40.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = character.name.first().toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2,
                minLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                color = when (character.role) {
                    CharacterRole.DETECTIVE -> Color(0xFF4ECDC4).copy(alpha = 0.2f)
                    CharacterRole.SUSPECT -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                    CharacterRole.WITNESS -> Color(0xFF45B7D1).copy(alpha = 0.2f)
                    CharacterRole.VICTIM -> Color(0xFF9C27B0).copy(alpha = 0.2f)
                    CharacterRole.HOST -> Color(0xFFFF9800).copy(alpha = 0.2f)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = character.role.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (character.role) {
                        CharacterRole.DETECTIVE -> Color(0xFF4ECDC4)
                        CharacterRole.SUSPECT -> Color(0xFFFF6B6B)
                        CharacterRole.WITNESS -> Color(0xFF45B7D1)
                        CharacterRole.VICTIM -> Color(0xFF9C27B0)
                        CharacterRole.HOST -> Color(0xFFFF9800)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 4,
                minLines = 2
            )
        }
    }
}

package ca.realitywargames.mysterybox.feature.mystery.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.core.ui.screen.BaseScreen
import ca.realitywargames.mysterybox.feature.mystery.ui.component.MysteryInfoRow
import ca.realitywargames.mysterybox.core.ui.component.NetworkImage
import ca.realitywargames.mysterybox.core.navigation.NavRoutes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import ca.realitywargames.mysterybox.core.data.network.serverBaseUrl
import ca.realitywargames.mysterybox.feature.mystery.ui.component.CharacterCard
import ca.realitywargames.mysterybox.preview.MockData
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MysteryDetailScreen(
    mystery: MysteryPackage,
    navController: NavHostController,
    onBackClick: () -> Unit,
) {
    BaseScreen(
        title = "Mystery Details",
        onBackClick = onBackClick
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp))
        {
            // Hero Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    // Load actual mystery image
                    NetworkImage(
                        url = "$serverBaseUrl${mystery.imagePath}",
                        contentDescription = "Hero image for ${mystery.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

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
                            text = "$${mystery.price}",
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
                        text = mystery.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MysteryInfoRow(mysteryPackage = mystery)
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
                        text = mystery.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        lineHeight = TextUnit(1.6f, TextUnitType.Em)
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
                        mystery.themes.forEach { theme ->
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
                    items(mystery.characters) { character ->
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
                            text = mystery.plotSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            lineHeight = TextUnit(1.6f, TextUnitType.Em),
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
                            navController.navigate(NavRoutes.createParty(mystery.id))
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

@Preview
@Composable
private fun MysteryDetailScreenPreview() {
    MysteryBoxTheme {
        MysteryDetailScreen(
            mystery = MockData.sampleMysteryPackage(),
            navController = rememberNavController(),
            onBackClick = { }
        )
    }
}


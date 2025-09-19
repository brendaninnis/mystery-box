package ca.realitywargames.mysterybox.ui.screens.mysteries

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.shared.models.CharacterTemplate
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.ui.components.BaseScreen
import ca.realitywargames.mysterybox.ui.components.CircularNetworkImage
import ca.realitywargames.mysterybox.ui.components.MysteryInfoRow
import ca.realitywargames.mysterybox.ui.components.NetworkImage
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.state.UiState
import ca.realitywargames.mysterybox.ui.viewmodel.MysteryDetailViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import ca.realitywargames.mysterybox.data.network.serverBaseUrl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MysteryDetailScreen(
    mysteryId: String,
    navController: NavHostController,
    viewModel: MysteryDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.mystery.collectAsState()

    BaseScreen(
        title = "Mystery Details",
        onBackClick = onBackClick
    ) {
        when (state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(
                message = (state as UiState.Error).message,
                onRetry = { viewModel.loadMystery() }
            )
            is UiState.Success -> {
                val mystery = (state as UiState.Success<MysteryPackage>).data
                MysteryContent(mystery = mystery, navController = navController, mysteryId = mysteryId)
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = Color(0xFFFF6B6B),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading mystery...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "❌ Oops!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFFF6B6B)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            ) {
                Text(
                    text = "Try Again",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun MysteryContent(
    mystery: MysteryPackage,
    navController: NavHostController,
    mysteryId: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
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
                        navController.navigate(NavRoutes.createParty(mysteryId))
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
            // Character avatar
            CircularNetworkImage(
                url = "$serverBaseUrl${character.avatarPath}",
                contentDescription = "Avatar for ${character.name}",
                size = 80.dp
            )

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


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 4,
                minLines = 2
            )
        }
    }
}

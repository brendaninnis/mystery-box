package ca.realitywargames.mysterybox.ui.screens.parties

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import ca.realitywargames.mysterybox.shared.models.GameStateSection
import ca.realitywargames.mysterybox.shared.models.PartyStatus
import ca.realitywargames.mysterybox.ui.components.BaseScreen
import ca.realitywargames.mysterybox.ui.components.GameActionCard
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.navigation.PartyCharactersRoute
import ca.realitywargames.mysterybox.ui.navigation.PartyEvidenceRoute
import ca.realitywargames.mysterybox.ui.navigation.PartyInventoryRoute
import ca.realitywargames.mysterybox.ui.navigation.PartyInviteRoute
import ca.realitywargames.mysterybox.ui.navigation.PartyObjectivesRoute
import ca.realitywargames.mysterybox.ui.navigation.PartyPhaseInstructionsRoute
import ca.realitywargames.mysterybox.ui.navigation.PartySolutionRoute
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyDetailScreen(
    navController: NavHostController,
    viewModel: PartyViewModel,
    onBackClick: () -> Unit
) {
    // Get real party data from the backend
    val selectedParty by viewModel.selectedParty.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Show loading state
    if (isLoading || selectedParty == null) {
        BaseScreen(
            title = "Party Details",
            onBackClick = onBackClick
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }
    
    val party = selectedParty!!
    val currentPhase = "Phase ${party.currentPhaseIndex + 1}" // Real phase based on current index
    val gameState = party.gameState
    val unlockedSections = gameState?.unlockedSections ?: emptyList()

    BaseScreen(
        title = party.title,
        onBackClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Phase indicator - tappable
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        viewModel.selectParty(party)
                        navController.navigate(PartyPhaseInstructionsRoute(party.id))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Current Phase: $currentPhase",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Game actions grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    GameActionCard(
                        title = "Invite",
                        icon = Icons.Default.Person,
                        isEnabled = party.status == PartyStatus.PLANNED || party.status == PartyStatus.IN_PROGRESS,
                        onClick = {
                            viewModel.selectParty(party)
                            navController.navigate(PartyInviteRoute(party.id))
                        }
                    )
                }

                item {
                GameActionCard(
                    title = "Objectives",
                    icon = Icons.Default.MailOutline,
                    isEnabled = unlockedSections.contains(GameStateSection.OBJECTIVES),
                    onClick = {
                        viewModel.selectParty(party)
                        navController.navigate(PartyObjectivesRoute(party.id))
                    }
                )
                }

                item {
                GameActionCard(
                    title = "Characters",
                    icon = Icons.Default.Face,
                    isEnabled = unlockedSections.contains(GameStateSection.CHARACTER_INFO),
                    onClick = {
                        viewModel.selectParty(party)
                        navController.navigate(PartyCharactersRoute(party.id))
                    }
                )
                }

                item {
                GameActionCard(
                    title = "Inventory",
                    icon = Icons.Default.ShoppingCart,
                    isEnabled = unlockedSections.contains(GameStateSection.INVENTORY),
                    onClick = {
                        viewModel.selectParty(party)
                        navController.navigate(PartyInventoryRoute(party.id))
                    }
                )
                }

                item {
                GameActionCard(
                    title = "Evidence",
                    icon = Icons.Default.Search,
                    isEnabled = unlockedSections.contains(GameStateSection.EVIDENCE),
                    onClick = {
                        viewModel.selectParty(party)
                        navController.navigate(PartyEvidenceRoute(party.id))
                    }
                )
                }

                item {
                GameActionCard(
                    title = "Solution",
                    icon = Icons.Default.CheckCircle,
                    isEnabled = unlockedSections.contains(GameStateSection.SOLUTION),
                    onClick = {
                        viewModel.selectParty(party)
                        navController.navigate(PartySolutionRoute(party.id))
                    }
                )
                }
            }
        }
    }
}

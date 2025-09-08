package ca.realitywargames.mysterybox.ui.screens.parties

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.data.models.Party
import ca.realitywargames.mysterybox.data.models.PartyStatus
import ca.realitywargames.mysterybox.ui.components.BaseScreen
import ca.realitywargames.mysterybox.ui.components.GameActionCard
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyDetailScreen(
    partyId: String,
    navController: NavHostController,
    viewModel: PartyViewModel,
    onBackClick: () -> Unit
) {
    // Mock party data - in real app this would come from the viewModel
    val mockParty = Party(
        id = partyId,
        hostId = "user1",
        mysteryPackageId = "1",
        title = "Saturday Night Mystery",
        description = "Our first murder mystery party!",
        scheduledDate = "2024-01-20T19:00:00Z",
        status = PartyStatus.IN_PROGRESS,
        inviteCode = "ABC123",
        maxGuests = 6,
        guests = emptyList(),
        currentPhaseIndex = 2, // Murder phase
        createdAt = "2024-01-15T10:30:00Z",
        updatedAt = "2024-01-15T10:30:00Z"
    )

    val currentPhase = "Murder Investigation" // Mock phase name

    BaseScreen(
        title = mockParty.title,
        onBackClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Phase indicator
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                        isEnabled = mockParty.status == PartyStatus.PLANNED || mockParty.status == PartyStatus.IN_PROGRESS,
                        onClick = {
                            navController.navigate(NavRoutes.partyInvite(partyId))
                        }
                    )
                }

                item {
                    GameActionCard(
                        title = "Instructions",
                        icon = Icons.Default.Info,
                        isEnabled = true,
                        onClick = {
                            navController.navigate(NavRoutes.partyInstructions(partyId))
                        }
                    )
                }

                item {
                    GameActionCard(
                        title = "Characters",
                        icon = Icons.Default.Face,
                        isEnabled = mockParty.status == PartyStatus.IN_PROGRESS || mockParty.status == PartyStatus.COMPLETED,
                        onClick = {
                            navController.navigate(NavRoutes.partyCharacters(partyId))
                        }
                    )
                }

                item {
                    GameActionCard(
                        title = "Inventory",
                        icon = Icons.Default.ShoppingCart,
                        isEnabled = mockParty.status == PartyStatus.IN_PROGRESS || mockParty.status == PartyStatus.COMPLETED,
                        onClick = {
                            navController.navigate(NavRoutes.partyInventory(partyId))
                        }
                    )
                }

                item {
                    GameActionCard(
                        title = "Evidence",
                        icon = Icons.Default.Search,
                        isEnabled = mockParty.currentPhaseIndex >= 1, // Available after murder phase
                        onClick = {
                            navController.navigate(NavRoutes.partyEvidence(partyId))
                        }
                    )
                }

                item {
                    GameActionCard(
                        title = "Solution",
                        icon = Icons.Default.CheckCircle,
                        isEnabled = mockParty.currentPhaseIndex >= 3, // Available after investigation phase
                        onClick = {
                            navController.navigate(NavRoutes.partySolution(partyId))
                        }
                    )
                }
            }
        }
    }
}

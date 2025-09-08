package ca.realitywargames.mysterybox.ui.screens.parties

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.data.models.*
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.theme.MysteryGradient
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isEnabled)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        ),
        onClick = if (isEnabled) onClick else { {} }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = if (isEnabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isEnabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (!isEnabled) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Locked",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        mockParty.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
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
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MysteryGradient)
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

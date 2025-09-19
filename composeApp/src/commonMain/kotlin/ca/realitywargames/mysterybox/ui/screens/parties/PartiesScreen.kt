package ca.realitywargames.mysterybox.ui.screens.parties

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.data.network.serverBaseUrl
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.Party
import ca.realitywargames.mysterybox.shared.models.PartyStatus
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.ui.components.NetworkImage
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun PartyCard(
    party: Party,
    onClick: () -> Unit,
    onStartGame: () -> Unit
) {
    val localDateTime = Instant.parse(party.scheduledDate).toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedDate = "${localDateTime.month.name} ${localDateTime.day}, ${localDateTime.year}"
    
    // Mock mystery data - in real app this would come from the mystery package
    val mockMystery = MysteryPackage(
        id = party.mysteryPackageId,
        title = "The Haunted Mansion Mystery",
        description = "A spine-chilling murder mystery set in an old mansion",
        imagePath = "/images/murder-and-dragons.png",
        price = 29.99,
        currency = "USD",
        durationMinutes = 180,
        minPlayers = 6,
        maxPlayers = 12,
        difficulty = Difficulty.MEDIUM,
        themes = listOf("Horror", "Victorian"),
        plotSummary = "The Harrington family gathers for the reading of the will...",
        characters = emptyList(),
        phases = emptyList(),
        isAvailable = true
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Mystery image
            NetworkImage(
                url = "$serverBaseUrl${mockMystery.imagePath}",
                contentDescription = mockMystery.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mystery Image",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mystery Image",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Mystery name as title
                        Text(
                            text = mockMystery.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
    
                        Spacer(modifier = Modifier.height(4.dp))
    
                        // Party name as subtitle
                        Text(
                            text = party.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
    
                    // Status badge
                    Surface(
                        color = when (party.status) {
                            PartyStatus.PLANNED -> MaterialTheme.colorScheme.primaryContainer
                            PartyStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondaryContainer
                            PartyStatus.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer
                            PartyStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            PartyStatus.DRAFT -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = party.status.name.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
    
                Spacer(modifier = Modifier.height(12.dp))
    
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${party.guests.size}/${party.maxGuests} guests",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
    
                Text(
                    text = "Scheduled: $formattedDate",
                    style = MaterialTheme.typography.bodySmall
                )
                
                // Show address if available
                party.address?.let { address ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = address,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp),
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
    
                Spacer(modifier = Modifier.height(12.dp))
    
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    when (party.status) {
                        PartyStatus.PLANNED -> {
                            OutlinedButton(onClick = onClick) {
                                Text("View Details")
                            }
                        }
                        PartyStatus.IN_PROGRESS -> {
                            Button(onClick = onStartGame) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Continue Game")
                            }
                        }
                        PartyStatus.COMPLETED -> {
                            OutlinedButton(onClick = onClick) {
                                Text("View Results")
                            }
                        }
                        else -> {
                            OutlinedButton(onClick = onClick) {
                                Text("View Details")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun PartiesScreen(
    navController: NavHostController,
    viewModel: PartyViewModel
) {
    val userParties by viewModel.userParties.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inviteCode by remember { mutableStateOf("") }
    var isJoining by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Parties List - takes up remaining space
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                userParties.isEmpty() -> {
                    item {
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No parties yet",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "Join a party using the invite code below or create your own!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                else -> {
                    items(userParties) { party ->
                        PartyCard(
                            party = party,
                            onClick = {
                                navController.navigate(NavRoutes.partyDetail(party.id))
                            },
                            onStartGame = {
                                navController.navigate(NavRoutes.game(party.id))
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Join Party Section - at bottom
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Join a Party",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enter an invite code to join an existing mystery party",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inviteCode,
                        onValueChange = { inviteCode = it.uppercase() },
                        label = { Text("Invite Code") },
                        placeholder = { Text("ABC123") },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Button(
                        onClick = {
                            if (inviteCode.isNotBlank()) {
                                isJoining = true
                                // TODO: Implement join party logic
                                // For now, just show a success message
                                // viewModel.joinParty(inviteCode)
                            }
                        },
                        enabled = inviteCode.isNotBlank() && !isJoining
                    ) {
                        if (isJoining) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

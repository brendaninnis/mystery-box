package ca.realitywargames.mysterybox.feature.party.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
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
import ca.realitywargames.mysterybox.feature.party.ui.component.PartyCard
import ca.realitywargames.mysterybox.feature.party.navigation.PartyDetailRoute
import ca.realitywargames.mysterybox.feature.party.presentation.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.feature.party.presentation.action.PartyAction
import ca.realitywargames.mysterybox.feature.party.presentation.effect.PartySideEffect
import ca.realitywargames.mysterybox.feature.party.presentation.state.PartyUiState
import ca.realitywargames.mysterybox.core.presentation.state.AsyncState
import ca.realitywargames.mysterybox.preview.MockData
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ca.realitywargames.mysterybox.shared.models.Party
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PartiesScreen(
    navController: NavHostController,
    viewModel: PartyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is PartySideEffect.NavigateToPartyDetail -> {
                    navController.navigate(PartyDetailRoute(effect.partyId))
                }
                is PartySideEffect.ShowErrorMessage -> {
                    // Handle error display (could show snackbar, etc.)
                }
                is PartySideEffect.ShowSuccessMessage -> {
                    // Handle success message
                }
                is PartySideEffect.ShowToast -> {
                    // Handle toast message
                }
                else -> {
                    // Handle other effects if needed
                }
            }
        }
    }

    PartiesScreenContent(
        uiState = uiState,
        onPartyClick = { party ->
            viewModel.onAction(PartyAction.NavigateToPartyDetail(party))
        },
        onJoinParty = { inviteCode ->
            // TODO: Implement join party logic
            // viewModel.onAction(PartyAction.JoinParty(inviteCode))
        }
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun PartiesScreenContent(
    uiState: PartyUiState,
    onPartyClick: (Party) -> Unit,
    onJoinParty: (String) -> Unit
) {
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
                uiState.loadPartiesState.isLoading -> {
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
                uiState.userParties.isEmpty() -> {
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
                    items(uiState.userParties) { party ->
                        PartyCard(
                            party = party,
                            mysteryPackage = uiState.getMysteryPackageForParty(party.mysteryPackageId),
                            onClick = { onPartyClick(party) }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Join Party Section
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
                                onJoinParty(inviteCode)
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

@Preview
@Composable
private fun PartiesScreenLoadingPreview() {
    MysteryBoxTheme {
        PartiesScreenContent(
            uiState = PartyUiState(loadPartiesState = AsyncState(isLoading = true)),
            onPartyClick = { },
            onJoinParty = { }
        )
    }
}

@Preview
@Composable
private fun PartiesScreenWithPartiesPreview() {
    val mockParties = MockData.sampleParties()
    val mockMysteryPackages = mapOf(
        "mystery-1" to MockData.sampleMysteryPackages()[0],
        "mystery-2" to MockData.sampleMysteryPackages()[1],
        "mystery-3" to MockData.sampleMysteryPackages()[2]
    )
    
    MysteryBoxTheme {
        PartiesScreenContent(
            uiState = PartyUiState(
                userParties = mockParties,
                mysteryPackages = mockMysteryPackages,
                loadPartiesState = AsyncState()
            ),
            onPartyClick = { },
            onJoinParty = { }
        )
    }
}

@Preview
@Composable
private fun PartiesScreenEmptyPreview() {
    MysteryBoxTheme {
        PartiesScreenContent(
            uiState = PartyUiState(
                userParties = emptyList(),
                loadPartiesState = AsyncState()
            ),
            onPartyClick = { },
            onJoinParty = { }
        )
    }
}

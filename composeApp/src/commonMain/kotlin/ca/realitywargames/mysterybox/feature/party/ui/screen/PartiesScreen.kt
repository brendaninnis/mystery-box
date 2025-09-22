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
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PartiesScreen(
    navController: NavHostController,
    viewModel: PartyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var inviteCode by remember { mutableStateOf("") }
    
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
                            onClick = {
                                viewModel.onAction(PartyAction.NavigateToPartyDetail(party))
                            },
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

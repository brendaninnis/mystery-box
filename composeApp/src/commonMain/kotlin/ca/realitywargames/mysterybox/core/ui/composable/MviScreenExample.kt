package ca.realitywargames.mysterybox.core.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.realitywargames.mysterybox.feature.party.presentation.action.PartyAction
import ca.realitywargames.mysterybox.feature.party.presentation.effect.PartySideEffect
import ca.realitywargames.mysterybox.feature.party.presentation.state.PartyUiState
import ca.realitywargames.mysterybox.feature.party.presentation.viewmodel.PartyViewModel

/**
 * Example of how to use the MVI pattern in a Composable screen
 * 
 * This shows the recommended pattern for:
 * 1. Collecting state from ViewModel
 * 2. Handling side effects
 * 3. Sending actions to ViewModel
 * 4. Separating stateful and stateless composables
 */
@Composable
fun PartyScreenExample(
    onNavigateToPartyDetail: (String) -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: PartyViewModel = viewModel { PartyViewModel() }
) {
    // 1. Collect state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    
    // 2. Handle side effects
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is PartySideEffect.NavigateToPartyDetail -> {
                    onNavigateToPartyDetail(effect.partyId)
                }
                is PartySideEffect.NavigateToPartyInvite -> {
                    // Handle navigation to invite screen
                }
                is PartySideEffect.NavigateToPartyInstructions -> {
                    // Handle navigation to instructions screen
                }
                is PartySideEffect.NavigateToPartyPhaseInstructions -> {
                    // Handle navigation to phase instructions screen
                }
                is PartySideEffect.NavigateToPartyObjectives -> {
                    // Handle navigation to objectives screen
                }
                is PartySideEffect.NavigateToPartyCharacters -> {
                    // Handle navigation to characters screen
                }
                is PartySideEffect.NavigateToPartyInventory -> {
                    // Handle navigation to inventory screen
                }
                is PartySideEffect.NavigateToPartyEvidence -> {
                    // Handle navigation to evidence screen
                }
                is PartySideEffect.NavigateToPartySolution -> {
                    // Handle navigation to solution screen
                }
                is PartySideEffect.ShowSuccessMessage -> {
                    onShowMessage(effect.message)
                }
                is PartySideEffect.ShowErrorMessage -> {
                    onShowMessage("Error: ${effect.message}")
                }
                is PartySideEffect.ShowToast -> {
                    onShowMessage(effect.message)
                }
                is PartySideEffect.PartyCreatedSuccessfully -> {
                    onShowMessage("Party created successfully!")
                }
                is PartySideEffect.PartyJoinedSuccessfully -> {
                    onShowMessage("Joined party successfully!")
                }
                is PartySideEffect.PartyPhaseAdvanced -> {
                    onShowMessage("Party phase advanced!")
                }
                is PartySideEffect.NavigateBack -> {
                    // Handle navigation back
                }
            }
        }
    }
    
    // 3. Render UI based on state and pass actions
    PartyScreenContent(
        state = uiState,
        onAction = viewModel::onAction
    )
}

/**
 * Stateless composable that renders UI based on state and handles user interactions
 */
@Composable
private fun PartyScreenContent(
    state: PartyUiState,
    onAction: (PartyAction) -> Unit
) {
    when {
        state.loadPartiesState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        state.loadPartiesState.hasError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${state.loadPartiesState.error}")
            }
        }
        
        else -> {
            // Render your main content here
            Text("Parties: ${state.userParties.size}")
            
            // Example of triggering actions
            // Button(onClick = { onAction(PartyAction.RefreshParties) }) {
            //     Text("Refresh")
            // }
        }
    }
}

/**
 * Key benefits of this MVI pattern:
 * 
 * 1. **Unidirectional Data Flow**: 
 *    - Actions flow from UI to ViewModel
 *    - State flows from ViewModel to UI
 *    - Side effects are handled separately
 * 
 * 2. **Testability**: 
 *    - Pure functions for state updates
 *    - Easy to test actions and state transformations
 *    - Side effects are isolated
 * 
 * 3. **Predictability**:
 *    - All state changes are explicit
 *    - Easy to track state mutations
 *    - Clear separation of concerns
 * 
 * 4. **Reusability**:
 *    - Stateless composables can be reused
 *    - Actions are reusable across different UIs
 *    - State is centralized
 */

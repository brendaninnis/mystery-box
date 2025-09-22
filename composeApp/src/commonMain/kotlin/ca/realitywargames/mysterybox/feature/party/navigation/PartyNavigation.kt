package ca.realitywargames.mysterybox.feature.party.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import ca.realitywargames.mysterybox.feature.party.presentation.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyCharactersScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyDetailScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyEvidenceScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyInstructionsScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyInventoryScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyInviteScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyObjectivesScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartyPhaseInstructionsScreen
import ca.realitywargames.mysterybox.feature.party.ui.screen.PartySolutionScreen
import kotlinx.serialization.Serializable

@Serializable
object PartyGraph

@Serializable
data class PartyDetailRoute(val partyId: String)

@Serializable
data class PartyInviteRoute(val partyId: String)

@Serializable
data class PartyInstructionsRoute(val partyId: String)

@Serializable
data class PartyCharactersRoute(val partyId: String)

@Serializable
data class PartyInventoryRoute(val partyId: String)

@Serializable
data class PartyEvidenceRoute(val partyId: String)

@Serializable
data class PartySolutionRoute(val partyId: String)

@Serializable
data class PartyPhaseInstructionsRoute(val partyId: String)

@Serializable
data class PartyObjectivesRoute(val partyId: String)

fun NavGraphBuilder.partyGraph(
    navController: NavHostController,
    partyViewModel: PartyViewModel
) {
    navigation<PartyGraph>(
        startDestination = PartyDetailRoute::class
    ) {
        val onBack: () -> Unit = { navController.navigateUp() }

        composable<PartyDetailRoute> { backStackEntry ->
            PartyDetailScreen(
                navController = navController,
                viewModel = partyViewModel,
                onBackClick = onBack
            )
        }

        composable<PartyInviteRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyInviteScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyInstructionsRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyInstructionsScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyCharactersRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyCharactersScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyInventoryRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyInventoryScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyEvidenceRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyEvidenceScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartySolutionRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartySolutionScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyPhaseInstructionsRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyPhaseInstructionsScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyObjectivesRoute> { backStackEntry ->
            val selectedParty by partyViewModel.uiState.collectAsState()
            val party = selectedParty.selectedParty 
                ?: throw IllegalArgumentException("Party not selected")
            PartyObjectivesScreen(
                party = party,
                onBackClick = onBack
            )
        }
    }
}

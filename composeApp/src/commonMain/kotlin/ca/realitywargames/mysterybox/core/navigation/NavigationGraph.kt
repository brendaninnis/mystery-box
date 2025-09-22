package ca.realitywargames.mysterybox.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.realitywargames.mysterybox.core.ui.screen.MainScreen
import ca.realitywargames.mysterybox.feature.mystery.navigation.mysteryGraph
import ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel.MysteryListViewModel
import ca.realitywargames.mysterybox.feature.party.navigation.partyGraph
import ca.realitywargames.mysterybox.feature.party.presentation.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.feature.profile.navigation.profileGraph
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = viewModel { UserViewModel() },
    partyViewModel: PartyViewModel = viewModel { PartyViewModel() },
    mysteryListViewModel: MysteryListViewModel = viewModel { MysteryListViewModel() },
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        // Main app screen
        composable<HomeRoute> {
            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                mysteryListViewModel = mysteryListViewModel,
                partyViewModel = partyViewModel
            )
        }

        // Feature navigation graphs
        mysteryGraph(
            navController = navController,
            mysteryListViewModel = mysteryListViewModel
        )

        partyGraph(
            navController = navController,
            partyViewModel = partyViewModel
        )

        profileGraph(
            navController = navController,
            userViewModel = userViewModel
        )
    }
}

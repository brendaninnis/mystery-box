package ca.realitywargames.mysterybox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ca.realitywargames.mysterybox.ui.screens.MainScreen
import ca.realitywargames.mysterybox.ui.screens.mysteries.MysteryDetailScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyCharactersScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyDetailScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyEvidenceScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInstructionsScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInventoryScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInviteScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyObjectivesScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyPhaseInstructionsScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartySolutionScreen
import ca.realitywargames.mysterybox.ui.screens.profile.LoginScreen
import ca.realitywargames.mysterybox.ui.screens.profile.RegisterScreen
import ca.realitywargames.mysterybox.ui.screens.profile.SettingsScreen
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.MysteryListViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.UserViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = viewModel { UserViewModel() },
    partyViewModel: PartyViewModel = viewModel { PartyViewModel() },
    mysteryListViewModel: MysteryListViewModel = viewModel { MysteryListViewModel() },
) {
    val selectedParty by partyViewModel.selectedParty.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        val onBack: () -> Unit = { navController.safePopBackStack() }

        // Main app screens
        composable(NavRoutes.HOME) {
            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                mysteryListViewModel = mysteryListViewModel,
                partyViewModel = partyViewModel
            )
        }

        // Authentication screens - ensure only one instance on stack
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    // Clear login from stack and navigate to register
                    navController.popBackStack(NavRoutes.LOGIN, inclusive = true)
                    navController.navigate(NavRoutes.REGISTER)
                },
                onBackClick = onBack
            )
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    // Clear register from stack and navigate to login
                    navController.popBackStack(NavRoutes.REGISTER, inclusive = true)
                    navController.navigate(NavRoutes.LOGIN)
                },
                onBackClick = onBack
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                userViewModel = userViewModel,
                navController = navController,
                onBackClick = onBack
            )
        }

        composable<MysteryDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<MysteryDetailRoute>()
            val mystery = mysteryListViewModel.getMysteryById(args.mysteryId) ?:
                throw IllegalArgumentException("Mystery not found: ${args.mysteryId}")
            MysteryDetailScreen(
                mystery = mystery,
                navController = navController,
                onBackClick = onBack,
            )
        }

        composable<PartyDetailRoute> { backStackEntry ->
            PartyDetailScreen(
                navController = navController,
                viewModel = partyViewModel,
                onBackClick = onBack
            )
        }

        // Party flow screens
        composable<PartyInviteRoute> { backStackEntry ->
            val party = selectedParty ?:
                throw IllegalArgumentException("Party not selected")
            PartyInviteScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyInstructionsRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyInstructionsScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyCharactersRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyCharactersScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyInventoryRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyInventoryScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyEvidenceRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyEvidenceScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartySolutionRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartySolutionScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyPhaseInstructionsRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyPhaseInstructionsScreen(
                party = party,
                onBackClick = onBack
            )
        }

        composable<PartyObjectivesRoute> { backStackEntry ->
            val party = selectedParty ?:
            throw IllegalArgumentException("Party not selected")
            PartyObjectivesScreen(
                party = party,
                onBackClick = onBack
            )
        }
    }
}

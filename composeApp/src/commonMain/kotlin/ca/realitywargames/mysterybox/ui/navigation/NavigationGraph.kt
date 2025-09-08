package ca.realitywargames.mysterybox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ca.realitywargames.mysterybox.ui.screens.MainScreen
import ca.realitywargames.mysterybox.ui.screens.mysteries.MysteryDetailScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyCharactersScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyDetailScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyEvidenceScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInstructionsScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInventoryScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartyInviteScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartySolutionScreen
import ca.realitywargames.mysterybox.ui.screens.profile.LoginScreen
import ca.realitywargames.mysterybox.ui.screens.profile.RegisterScreen
import ca.realitywargames.mysterybox.ui.screens.profile.SettingsScreen
import ca.realitywargames.mysterybox.ui.viewmodel.MysteryViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.UserViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = viewModel { UserViewModel() }
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
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
                onBackClick = {
                    navController.popBackStack()
                }
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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Main app screens
        composable(NavRoutes.HOME) {
            MainScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(
            route = NavRoutes.MYSTERY_DETAIL,
            arguments = listOf(navArgument("mysteryId") { type = NavType.StringType })
        ) { backStackEntry ->
            // For demo purposes, use a default value
            val mysteryId = "1" // Default mystery ID
            MysteryDetailScreen(
                mysteryId = mysteryId,
                navController = navController,
                viewModel = viewModel { MysteryViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_DETAIL,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            // For demo purposes, use a default value
            val partyId = "party1" // Default party ID
            PartyDetailScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                userViewModel = userViewModel,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Party flow screens
        composable(
            route = NavRoutes.PARTY_INVITE,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartyInviteScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_INSTRUCTIONS,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartyInstructionsScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_CHARACTERS,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartyCharactersScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_INVENTORY,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartyInventoryScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_EVIDENCE,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartyEvidenceScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PARTY_SOLUTION,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: "party1"
            PartySolutionScreen(
                partyId = partyId,
                navController = navController,
                viewModel = viewModel { PartyViewModel() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

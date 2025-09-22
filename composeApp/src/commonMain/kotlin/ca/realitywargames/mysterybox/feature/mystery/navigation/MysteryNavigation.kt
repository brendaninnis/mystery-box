package ca.realitywargames.mysterybox.feature.mystery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel.MysteryListViewModel
import ca.realitywargames.mysterybox.feature.mystery.ui.screen.MysteryDetailScreen
import kotlinx.serialization.Serializable

@Serializable
object MysteryGraph

@Serializable
data class MysteryDetailRoute(val mysteryId: String)

fun NavGraphBuilder.mysteryGraph(
    navController: NavHostController,
    mysteryListViewModel: MysteryListViewModel
) {
    navigation<MysteryGraph>(
        startDestination = MysteryDetailRoute::class
    ) {
        composable<MysteryDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<MysteryDetailRoute>()
            val mystery = mysteryListViewModel.getMysteryById(args.mysteryId) 
                ?: throw IllegalArgumentException("Mystery not found: ${args.mysteryId}")
            
            MysteryDetailScreen(
                mystery = mystery,
                navController = navController,
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}

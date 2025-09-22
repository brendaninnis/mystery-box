package ca.realitywargames.mysterybox.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel
import ca.realitywargames.mysterybox.feature.profile.ui.screen.LoginScreen
import ca.realitywargames.mysterybox.feature.profile.ui.screen.RegisterScreen
import ca.realitywargames.mysterybox.feature.profile.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileGraph

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object SettingsRoute

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    navigation<ProfileGraph>(
        startDestination = LoginRoute::class
    ) {
        val onBack: () -> Unit = { navController.navigateUp() }

        composable<LoginRoute> {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    // Clear login from stack and navigate to register
                    navController.popBackStack(LoginRoute, inclusive = true)
                    navController.navigate(RegisterRoute)
                },
                onBackClick = onBack
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    // Clear register from stack and navigate to login
                    navController.popBackStack(RegisterRoute, inclusive = true)
                    navController.navigate(LoginRoute)
                },
                onBackClick = onBack
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                userViewModel = userViewModel,
                navController = navController,
                onBackClick = onBack
            )
        }
    }
}

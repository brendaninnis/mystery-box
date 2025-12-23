package ca.realitywargames.mysterybox.feature.profile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.core.navigation.HomeRoute
import ca.realitywargames.mysterybox.core.ui.component.ConfirmationDialog
import ca.realitywargames.mysterybox.core.ui.screen.BaseScreen
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import ca.realitywargames.mysterybox.core.ui.theme.MysteryGradient
import ca.realitywargames.mysterybox.feature.profile.presentation.action.UserAction
import ca.realitywargames.mysterybox.feature.profile.presentation.effect.UserSideEffect
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userViewModel: UserViewModel,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val uiState by userViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.sideEffect.collect { effect ->
            when (effect) {
                is UserSideEffect.AccountDeleted -> {
                    navController.navigate(HomeRoute) {
                        popUpTo(HomeRoute) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }

    SettingsScreenContent(
        isLoggedIn = uiState.isLoggedIn,
        isDeleting = uiState.deleteAccountState.isLoading,
        deleteError = uiState.deleteAccountState.error,
        onDeleteAccount = {
            userViewModel.onAction(UserAction.DeleteAccount)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isLoggedIn: Boolean = false,
    isDeleting: Boolean = false,
    deleteError: String? = null,
    onDeleteAccount: () -> Unit = {},
    onBackClick: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    BaseScreen(
        title = "Settings",
        onBackClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MysteryGradient)
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (isLoggedIn) {
                Spacer(modifier = Modifier.weight(1f))

                deleteError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = { showDeleteConfirmation = true },
                    enabled = !isDeleting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Delete Account")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            } else {
                Text(
                    text = "Sign in to access account settings.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (showDeleteConfirmation) {
        ConfirmationDialog(
            title = "Delete Account",
            message = "Are you sure you want to delete your account? This action cannot be undone and you will not be able to log in again with these credentials.",
            confirmText = "Delete",
            dismissText = "Cancel",
            isDestructive = true,
            onConfirm = {
                showDeleteConfirmation = false
                onDeleteAccount()
            },
            onDismiss = {
                showDeleteConfirmation = false
            }
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MysteryBoxTheme {
        SettingsScreenContent(
            isLoggedIn = true,
            isDeleting = false,
            deleteError = null,
            onDeleteAccount = {},
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun SettingsScreenNotLoggedInPreview() {
    MysteryBoxTheme {
        SettingsScreenContent(
            isLoggedIn = false,
            isDeleting = false,
            deleteError = null,
            onDeleteAccount = {},
            onBackClick = {}
        )
    }
}

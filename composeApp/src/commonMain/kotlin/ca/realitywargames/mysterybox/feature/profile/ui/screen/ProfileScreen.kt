package ca.realitywargames.mysterybox.feature.profile.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.feature.profile.navigation.LoginRoute
import ca.realitywargames.mysterybox.feature.profile.navigation.RegisterRoute
import ca.realitywargames.mysterybox.feature.profile.navigation.SettingsRoute
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel
import ca.realitywargames.mysterybox.feature.profile.presentation.state.UserUiState
import ca.realitywargames.mysterybox.preview.MockData
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    navController: NavHostController
) {
    val uiState by userViewModel.uiState.collectAsState()

    ProfileScreenContent(
        state = uiState,
        onNavigateToSettings = { navController.navigate(SettingsRoute) },
        onNavigateToRegister = { navController.navigate(RegisterRoute) },
        onNavigateToLogin = { navController.navigate(LoginRoute) }
    )
}

@Composable
fun ProfileScreenContent(
    state: UserUiState,
    onNavigateToSettings: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoggedIn && state.currentUser != null) {
            // Logged in user profile
            val user = state.currentUser // Extract to local variable for smart cast
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (user.isHost) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "Host",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile options
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ListItem(
                    headlineContent = { Text("Settings") },
                    leadingContent = {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        onNavigateToSettings()
                    }
                )
            }
        } else {
            // Not logged in - show account creation suggestions
            Spacer(modifier = Modifier.height(64.dp))

            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sign up to sync your purchased mysteries across devices and keep track of your favorite games.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Create Account")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You can browse mysteries and join parties without an account!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenLoggedInPreview() {
    MysteryBoxTheme {
        ProfileScreenContent(
            state = UserUiState(
                currentUser = MockData.sampleUser(),
                isLoggedIn = true
            ),
            onNavigateToSettings = { },
            onNavigateToRegister = { },
            onNavigateToLogin = { }
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLoggedOutPreview() {
    MysteryBoxTheme {
        ProfileScreenContent(
            state = UserUiState(
                currentUser = null,
                isLoggedIn = false
            ),
            onNavigateToSettings = { },
            onNavigateToRegister = { },
            onNavigateToLogin = { }
        )
    }
}

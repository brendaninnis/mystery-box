package ca.realitywargames.mysterybox.ui.screens.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.realitywargames.mysterybox.ui.components.AuthButton
import ca.realitywargames.mysterybox.ui.components.AuthForm
import ca.realitywargames.mysterybox.ui.components.BaseScreen
import ca.realitywargames.mysterybox.ui.components.EmailTextField
import ca.realitywargames.mysterybox.ui.components.ErrorText
import ca.realitywargames.mysterybox.ui.components.PasswordTextField
import ca.realitywargames.mysterybox.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isAuthenticating by userViewModel.isAuthenticating.collectAsState()
    val error by userViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.clearError()
    }

    BaseScreen(
        title = "Login",
        onBackClick = onBackClick
    ) {
        AuthForm(title = "Welcome to Mystery Box") {
            EmailTextField(
                value = email,
                onValueChange = { email = it }
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it }
            )

            ErrorText(error)

            AuthButton(
                text = "Login",
                onClick = {
                    userViewModel.login(email, password)
                    // For demo purposes, navigate immediately
                    onLoginSuccess()
                },
                enabled = !isAuthenticating && email.isNotBlank() && password.isNotBlank(),
                isLoading = isAuthenticating
            )

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register")
            }
        }
    }
}

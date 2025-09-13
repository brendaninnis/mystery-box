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
import androidx.compose.ui.text.input.TextFieldValue

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
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val error by userViewModel.error.collectAsState()

    // Navigate to main screen when login succeeds
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    // Clear error when screen loads
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
                onValueChange = { email = it },
                isError = error != null && email.isNotBlank()
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                isError = error != null && password.isNotBlank()
            )

            ErrorText(error)

            AuthButton(
                text = "Login",
                onClick = {
                    userViewModel.login(email.trim(), password)
                },
                enabled = !isAuthenticating && isFormValid(email, password),
                isLoading = isAuthenticating
            )

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register")
            }
        }
    }
}

// Form validation functions
private fun isFormValid(email: String, password: String): Boolean {
    return email.isNotBlank() &&
           password.isNotBlank() &&
           isValidEmail(email) &&
           password.length >= 6
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}

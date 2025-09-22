package ca.realitywargames.mysterybox.feature.profile.ui.screen

import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import ca.realitywargames.mysterybox.feature.profile.ui.component.AuthButton
import ca.realitywargames.mysterybox.feature.profile.ui.component.AuthForm
import ca.realitywargames.mysterybox.core.ui.screen.BaseScreen
import ca.realitywargames.mysterybox.feature.profile.ui.component.EmailTextField
import ca.realitywargames.mysterybox.feature.profile.ui.component.ErrorText
import ca.realitywargames.mysterybox.feature.profile.ui.component.NameTextField
import ca.realitywargames.mysterybox.feature.profile.ui.component.PasswordTextField
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    userViewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val isAuthenticating by userViewModel.isAuthenticating.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val error by userViewModel.error.collectAsState()

    // Focus management
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // Navigate to main screen when registration succeeds
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterSuccess()
        }
    }

    // Clear error when screen loads
    LaunchedEffect(Unit) {
        userViewModel.clearError()
    }

    BaseScreen(
        title = "Register",
        onBackClick = onBackClick
    ) {
        AuthForm(title = "Join Mystery Box") {
            NameTextField(
                value = name,
                onValueChange = { name = it },
                isError = error != null && name.isNotBlank(),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = {
                        emailFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier.focusRequester(nameFocusRequester)
            )

            EmailTextField(
                value = email,
                onValueChange = { email = it },
                isError = error != null && email.isNotBlank(),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier.focusRequester(emailFocusRequester)
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                isError = error != null && password.isNotBlank(),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isFormValid(name, email, password)) {
                            userViewModel.register(email.trim(), password, name.trim())
                        }
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.focusRequester(passwordFocusRequester)
            )

            ErrorText(error)

            AuthButton(
                text = "Register",
                onClick = {
                    userViewModel.register(email.trim(), password, name.trim())
                },
                enabled = !isAuthenticating && isFormValid(name, email, password),
                isLoading = isAuthenticating
            )

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}

// Form validation functions
private fun isFormValid(name: String, email: String, password: String): Boolean {
    return name.isNotBlank() &&
           email.isNotBlank() &&
           password.isNotBlank() &&
           isValidEmail(email) &&
           password.length >= 6 &&
           name.length >= 2
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}

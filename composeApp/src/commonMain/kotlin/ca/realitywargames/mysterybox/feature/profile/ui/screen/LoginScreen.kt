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
import ca.realitywargames.mysterybox.feature.profile.ui.component.PasswordTextField
import ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel.UserViewModel
import ca.realitywargames.mysterybox.shared.validation.FormFieldValidator

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

    // Focus management
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

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
                        if (FormFieldValidator.validateLoginForm(email, password)) {
                            userViewModel.login(email.trim(), password)
                        }
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.focusRequester(passwordFocusRequester)
            )

            ErrorText(error)

            AuthButton(
                text = "Login",
                onClick = {
                    userViewModel.login(email.trim(), password)
                },
                enabled = !isAuthenticating && FormFieldValidator.validateLoginForm(email, password),
                isLoading = isAuthenticating
            )

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register")
            }
        }
    }
}

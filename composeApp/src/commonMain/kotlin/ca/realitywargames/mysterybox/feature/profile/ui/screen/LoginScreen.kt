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
import ca.realitywargames.mysterybox.feature.profile.presentation.action.UserAction
import ca.realitywargames.mysterybox.feature.profile.presentation.effect.UserSideEffect
import ca.realitywargames.mysterybox.shared.validation.FormFieldValidator
import ca.realitywargames.mysterybox.preview.MockData
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by userViewModel.uiState.collectAsState()

    // Handle one-time side effects
    LaunchedEffect(Unit) {
        userViewModel.sideEffect.collect { effect ->
            when (effect) {
                is UserSideEffect.LoginSucceeded -> onLoginSuccess()
                is UserSideEffect.ShowError -> { /* no-op, using state-bound error */ }
                is UserSideEffect.ShowToast -> { /* TODO: show toast */ }
                is UserSideEffect.RegistrationSucceeded -> { /* ignore on login */ }
            }
        }
    }

    // Clear error when screen loads
    LaunchedEffect(Unit) {
        userViewModel.onAction(UserAction.ClearError)
    }

    LoginScreenContent(
        isAuthenticating = uiState.authState.isLoading,
        error = uiState.authState.error,
        onLogin = { email, password ->
            userViewModel.onAction(UserAction.Login(email.trim(), password))
        },
        onNavigateToRegister = onNavigateToRegister,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    isAuthenticating: Boolean,
    error: String?,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Focus management
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

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
                            onLogin(email, password)
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
                    onLogin(email, password)
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

@Preview
@Composable
private fun LoginScreenPreview() {
    MysteryBoxTheme {
        LoginScreenContent(
            isAuthenticating = false,
            error = null,
            onLogin = { _, _ -> },
            onNavigateToRegister = { },
            onBackClick = { }
        )
    }
}

@Preview
@Composable
private fun LoginScreenErrorPreview() {
    MysteryBoxTheme {
        LoginScreenContent(
            isAuthenticating = false,
            error = "Invalid email or password",
            onLogin = { _, _ -> },
            onNavigateToRegister = { },
            onBackClick = { }
        )
    }
}

@Preview
@Composable
private fun LoginScreenLoadingPreview() {
    MysteryBoxTheme {
        LoginScreenContent(
            isAuthenticating = true,
            error = null,
            onLogin = { _, _ -> },
            onNavigateToRegister = { },
            onBackClick = { }
        )
    }
}

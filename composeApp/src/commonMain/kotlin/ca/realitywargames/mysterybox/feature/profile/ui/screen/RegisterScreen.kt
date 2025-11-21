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
import ca.realitywargames.mysterybox.feature.profile.presentation.action.UserAction
import ca.realitywargames.mysterybox.feature.profile.presentation.effect.UserSideEffect
import ca.realitywargames.mysterybox.shared.validation.FormFieldValidator
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    userViewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by userViewModel.uiState.collectAsState()

    // Handle side effects
    LaunchedEffect(Unit) {
        userViewModel.sideEffect.collect { effect ->
            when (effect) {
                is UserSideEffect.RegistrationSucceeded -> onRegisterSuccess()
                is UserSideEffect.ShowError -> { /* handled via state */ }
                is UserSideEffect.ShowToast -> { /* TODO */ }
                is UserSideEffect.LoginSucceeded -> { /* ignore on register */ }
            }
        }
    }

    // Clear error when screen loads
    LaunchedEffect(Unit) {
        userViewModel.onAction(UserAction.ClearError)
    }

    RegisterScreenContent(
        isAuthenticating = uiState.authState.isLoading,
        error = uiState.authState.error,
        onRegister = { email, password, name ->
            userViewModel.onAction(UserAction.Register(email.trim(), password, name.trim()))
        },
        onNavigateToLogin = onNavigateToLogin,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    isAuthenticating: Boolean,
    error: String?,
    onRegister: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    // Focus management
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    BaseScreen(
        title = "Register",
        onBackClick = onBackClick
    ) {
        AuthForm(title = "Join Mystery Nights") {
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
                        if (FormFieldValidator.validateRegisterForm(email, password, name)) {
                            onRegister(email, password, name)
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
                    onRegister(email, password, name)
                },
                enabled = !isAuthenticating && FormFieldValidator.validateRegisterForm(email, password, name),
                isLoading = isAuthenticating
            )

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    MysteryBoxTheme {
        RegisterScreenContent(
            isAuthenticating = false,
            error = null,
            onRegister = { _, _, _ -> },
            onNavigateToLogin = { },
            onBackClick = { }
        )
    }
}

@Preview
@Composable
private fun RegisterScreenErrorPreview() {
    MysteryBoxTheme {
        RegisterScreenContent(
            isAuthenticating = false,
            error = "Email already exists",
            onRegister = { _, _, _ -> },
            onNavigateToLogin = { },
            onBackClick = { }
        )
    }
}

@Preview
@Composable
private fun RegisterScreenLoadingPreview() {
    MysteryBoxTheme {
        RegisterScreenContent(
            isAuthenticating = true,
            error = null,
            onRegister = { _, _, _ -> },
            onNavigateToLogin = { },
            onBackClick = { }
        )
    }
}

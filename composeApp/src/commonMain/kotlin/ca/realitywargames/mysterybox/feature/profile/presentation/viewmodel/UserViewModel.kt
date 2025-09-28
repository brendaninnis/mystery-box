package ca.realitywargames.mysterybox.feature.profile.presentation.viewmodel

import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.core.presentation.viewmodel.MviViewModel
import ca.realitywargames.mysterybox.feature.profile.presentation.action.UserAction
import ca.realitywargames.mysterybox.feature.profile.presentation.effect.UserSideEffect
import ca.realitywargames.mysterybox.feature.profile.presentation.state.UserUiState

class UserViewModel : MviViewModel<UserUiState, UserAction, UserSideEffect>() {

    private val repository = AppContainer.userRepository

    override fun initialState(): UserUiState = UserUiState()

    init {
        onAction(UserAction.CheckCurrentUser)
    }

    override fun handleAction(action: UserAction) {
        when (action) {
            is UserAction.Login -> login(action.email, action.password)
            is UserAction.Register -> register(action.email, action.password, action.name)
            is UserAction.Logout -> logout()
            is UserAction.CheckCurrentUser -> checkCurrentUser()
            is UserAction.UpdatePreferences -> updateUserPreferences(action.preferences)
            is UserAction.ClearError -> clearError()
        }
    }

    private fun login(email: String, password: String) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(authState = uiState.value.authState.copy(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(authState = uiState.value.authState.copy(error = error))
                }
            ) {
                val user = repository.login(email, password)
                updateState { copy(currentUser = user, isLoggedIn = true, authState = authState.copy(isLoading = false, error = null)) }
                emitSideEffect(UserSideEffect.LoginSucceeded)
            }
        }
    }

    private fun register(email: String, password: String, name: String) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(authState = uiState.value.authState.copy(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(authState = uiState.value.authState.copy(error = error))
                }
            ) {
                val user = repository.register(email, password, name)
                updateState { copy(currentUser = user, isLoggedIn = true, authState = authState.copy(isLoading = false, error = null)) }
                emitSideEffect(UserSideEffect.RegistrationSucceeded)
            }
        }
    }

    private fun logout() {
        repository.logout()
        updateState { copy(currentUser = null, isLoggedIn = false) }
    }

    private fun updateUserPreferences(preferences: ca.realitywargames.mysterybox.shared.models.UserPreferences) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(updatePrefsState = uiState.value.updatePrefsState.copy(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(updatePrefsState = uiState.value.updatePrefsState.copy(error = error))
                }
            ) {
                val updatedUser = repository.updateUserPreferences(preferences)
                updateState { copy(currentUser = updatedUser, updatePrefsState = updatePrefsState.copy(isLoading = false, error = null)) }
            }
        }
    }

    private fun checkCurrentUser() {
        launchAsync {
            runCatching { repository.getCurrentUser() }
                .onSuccess { user ->
                    updateState { copy(currentUser = user, isLoggedIn = user != null) }
                }
                .onFailure {
                    updateState { copy(isLoggedIn = false) }
                }
        }
    }

    private fun clearError() {
        updateState { copy(authState = authState.copy(error = null)) }
    }
}
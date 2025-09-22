package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {

    private val repository = AppContainer.userRepository

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAuthenticating = MutableStateFlow(false)
    val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()

    init {
        checkCurrentUser()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isAuthenticating.value = true
                setError(null)
                runCatching { repository.login(email, password) }
                    .onSuccess { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                    }
                    .onFailure { exception ->
                        setError(exception.message ?: "Login failed. Please check your credentials.")
                    }
            } finally {
                _isAuthenticating.value = false
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _isAuthenticating.value = true
                setError(null)
                runCatching { repository.register(email, password, name) }
                    .onSuccess { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                    }
                    .onFailure { exception ->
                        setError(exception.message ?: "Registration failed. Please try again.")
                    }
            } finally {
                _isAuthenticating.value = false
            }
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        launchWithLoading {
            runCatching { repository.updateUserPreferences(preferences) }
                .onSuccess { updatedUser ->
                    _currentUser.value = updatedUser
                }
                .onFailure { _ ->
                    // Handle error
                }
        }
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            runCatching { repository.getCurrentUser() }
                .onSuccess { user ->
                    _currentUser.value = user
                    _isLoggedIn.value = user != null
                }
                .onFailure {
                    _isLoggedIn.value = false
                }
        }
    }
}

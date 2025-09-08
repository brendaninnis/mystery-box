package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.data.models.User
import ca.realitywargames.mysterybox.data.models.UserPreferences
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
                repository.login(email, password).collect { result ->
                    result.onSuccess { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                    }.onFailure { exception ->
                        // Handle error
                    }
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
                repository.register(email, password, name).collect { result ->
                    result.onSuccess { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                    }.onFailure { exception ->
                        // Handle error
                    }
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
            repository.updateUserPreferences(preferences).collect { result ->
                result.onSuccess { updatedUser ->
                    _currentUser.value = updatedUser
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            repository.getCurrentUser().collect { result ->
                result.onSuccess { user ->
                    _currentUser.value = user
                    _isLoggedIn.value = user != null
                }.onFailure { exception ->
                    _isLoggedIn.value = false
                }
            }
        }
    }
}

package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    protected fun launchWithLoading(block: suspend () -> Unit) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                clearError()
                block()
            } catch (e: Exception) {
                setError(e.message ?: "An error occurred")
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun clearError() {
        _error.update { null }
    }

    protected fun setError(error: String?) {
        _error.value = error
    }
}

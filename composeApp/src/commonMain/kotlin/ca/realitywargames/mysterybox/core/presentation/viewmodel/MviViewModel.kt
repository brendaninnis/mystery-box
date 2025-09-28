package ca.realitywargames.mysterybox.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing the MVI (Model-View-Intent) pattern.
 * 
 * @param UiState The state that represents the current UI state
 * @param UiAction The actions that can be performed by the user
 * @param SideEffect One-time events that should be handled by the UI
 */
abstract class MviViewModel<UiState, UiAction, SideEffect> : ViewModel() {

    /**
     * The initial state of the UI when the ViewModel is created
     */
    protected abstract fun initialState(): UiState

    /**
     * Handles the user actions and updates the state accordingly
     */
    protected abstract fun handleAction(action: UiAction)

    // State management
    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Side effects (one-time events)
    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect: SharedFlow<SideEffect> = _sideEffect.asSharedFlow()

    /**
     * Public method to handle user actions
     */
    fun onAction(action: UiAction) {
        handleAction(action)
    }

    /**
     * Updates the current UI state
     */
    protected fun updateState(transform: UiState.() -> UiState) {
        _uiState.update(transform)
    }

    /**
     * Emits a side effect to be consumed by the UI
     */
    protected fun emitSideEffect(effect: SideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    /**
     * Launches a coroutine within the viewModelScope for async operations
     */
    protected fun launchAsync(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    /**
     * Executes a block with error handling, automatically updating loading states
     */
    protected suspend fun <T> executeWithErrorHandling(
        updateLoadingState: ((Boolean) -> UiState)? = null,
        onError: ((String) -> UiState)? = null,
        block: suspend () -> T
    ): T? {
        return try {
            updateLoadingState?.let { transform -> updateState { transform(true) } }
            val result = block()
            updateLoadingState?.let { transform -> updateState { transform(false) } }
            result
        } catch (e: Exception) {
            updateLoadingState?.let { transform -> updateState { transform(false) } }
            onError?.let { transform -> updateState { transform(e.message ?: "An error occurred") } }
            null
        }
    }
}

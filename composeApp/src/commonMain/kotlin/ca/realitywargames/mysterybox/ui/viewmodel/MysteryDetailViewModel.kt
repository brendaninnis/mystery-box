package ca.realitywargames.mysterybox.ui.viewmodel

import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MysteryDetailViewModel(private val mysteryId: String) : BaseViewModel() {

    private val repository = AppContainer.mysteryRepository

    private val _mystery = MutableStateFlow<UiState<MysteryPackage>>(UiState.Loading)
    val mystery: StateFlow<UiState<MysteryPackage>> = _mystery.asStateFlow()

    init {
        loadMystery()
    }

    fun loadMystery() {
        launchWithLoading {
            repository.getMysteryPackage(mysteryId).collect { result ->
                result.onSuccess { pkg ->
                    _mystery.value = UiState.Success(pkg)
                }.onFailure { e ->
                    _mystery.value = UiState.Error(e.message ?: "Failed to load mystery")
                }
            }
        }
    }
}



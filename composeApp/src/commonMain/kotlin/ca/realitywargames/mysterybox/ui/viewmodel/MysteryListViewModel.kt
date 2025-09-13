package ca.realitywargames.mysterybox.ui.viewmodel

import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MysteryListViewModel : BaseViewModel() {

    private val repository = AppContainer.mysteryRepository

    private val _mysteries = MutableStateFlow<UiState<List<MysteryPackage>>>(UiState.Loading)
    val mysteries: StateFlow<UiState<List<MysteryPackage>>> = _mysteries.asStateFlow()

    init {
        loadMysteries()
    }

    fun loadMysteries() {
        launchWithLoading {
            repository.getMysteryPackages().collect { result ->
                result.onSuccess { response ->
                    _mysteries.value = UiState.Success(response.items)
                }.onFailure { e ->
                    _mysteries.value = UiState.Error(e.message ?: "Failed to load mysteries")
                }
            }
        }
    }
}



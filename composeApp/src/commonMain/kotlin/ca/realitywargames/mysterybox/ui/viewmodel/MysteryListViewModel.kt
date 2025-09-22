package ca.realitywargames.mysterybox.ui.viewmodel

import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MysteryListViewModel : BaseViewModel() {

    private val repository = AppContainer.mysteryRepository

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _mysteries = MutableStateFlow<UiState<List<MysteryPackage>>>(UiState.Loading)
    val mysteries: StateFlow<UiState<List<MysteryPackage>>> = _mysteries.asStateFlow()

    init {
        loadMysteries()
    }

    fun loadMysteries() {
        launchWithLoading {
            fetchMysteries()
        }
    }

    fun refreshMysteries() {
        _isRefreshing.update { true }
        launchWithLoading {
            delay(300)
            fetchMysteries()
            _isRefreshing.update { false }
        }
    }

    private suspend fun fetchMysteries() {
        runCatching { repository.getMysteryPackages() }
            .onSuccess { response ->
                _mysteries.update { UiState.Success(response.items) }
            }
            .onFailure { e ->
                _mysteries.update { UiState.Error(e.message ?: "Failed to load mysteries") }
            }
    }

    fun getMysteryById(id: String): MysteryPackage? =
        (mysteries.value as? UiState.Success<List<MysteryPackage>>)
            ?.data
            ?.firstOrNull { it.id == id }
}

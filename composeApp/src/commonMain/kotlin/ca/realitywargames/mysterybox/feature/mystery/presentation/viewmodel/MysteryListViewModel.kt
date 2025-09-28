package ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel

import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.core.data.state.UiState
import ca.realitywargames.mysterybox.core.presentation.viewmodel.MviViewModel
import ca.realitywargames.mysterybox.feature.mystery.presentation.action.MysteryListAction
import ca.realitywargames.mysterybox.feature.mystery.presentation.effect.MysteryListSideEffect
import ca.realitywargames.mysterybox.feature.mystery.presentation.state.MysteryListUiState
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import kotlinx.coroutines.delay

class MysteryListViewModel : MviViewModel<MysteryListUiState, MysteryListAction, MysteryListSideEffect>() {

    private val repository = AppContainer.mysteryRepository

    override fun initialState(): MysteryListUiState = MysteryListUiState()

    init {
        onAction(MysteryListAction.LoadMysteries)
    }

    override fun handleAction(action: MysteryListAction) {
        when (action) {
            is MysteryListAction.LoadMysteries -> loadMysteries()
            is MysteryListAction.RefreshMysteries -> refreshMysteries()
        }
    }

    private fun loadMysteries() {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(
                        mysteries = if (loading) UiState.Loading else uiState.value.mysteries
                    )
                },
                onError = { error ->
                    uiState.value.copy(mysteries = UiState.Error(error))
                }
            ) {
                val response = repository.getMysteryPackages()
                updateState { copy(mysteries = UiState.Success(response.items)) }
            }
        }
    }

    private fun refreshMysteries() {
        updateState { copy(isRefreshing = true) }
        launchAsync {
            executeWithErrorHandling(
                onError = { error ->
                    uiState.value.copy(mysteries = UiState.Error(error), isRefreshing = false)
                }
            ) {
                delay(300)
                val response = repository.getMysteryPackages()
                updateState { copy(mysteries = UiState.Success(response.items), isRefreshing = false) }
            }
        }
    }

    fun getMysteryById(id: String): MysteryPackage? =
        (uiState.value.mysteries as? UiState.Success<List<MysteryPackage>>)
            ?.data
            ?.firstOrNull { it.id == id }
}

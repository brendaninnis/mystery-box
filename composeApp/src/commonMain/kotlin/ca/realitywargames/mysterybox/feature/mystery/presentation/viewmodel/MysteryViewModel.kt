package ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel

import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.core.presentation.viewmodel.MviViewModel
import ca.realitywargames.mysterybox.feature.mystery.presentation.action.MysteryDetailAction
import ca.realitywargames.mysterybox.feature.mystery.presentation.effect.MysteryDetailSideEffect
import ca.realitywargames.mysterybox.feature.mystery.presentation.state.MysteryDetailUiState

class MysteryViewModel : MviViewModel<MysteryDetailUiState, MysteryDetailAction, MysteryDetailSideEffect>() {

    private val repository = AppContainer.mysteryRepository

    override fun initialState(): MysteryDetailUiState = MysteryDetailUiState()

    override fun handleAction(action: MysteryDetailAction) {
        when (action) {
            is MysteryDetailAction.LoadPackage -> loadPackage(action.packageId)
            is MysteryDetailAction.PurchasePackage -> purchasePackage(action.packageId)
            is MysteryDetailAction.ClearSelected -> clearSelected()
        }
    }

    private fun loadPackage(packageId: String) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(loadPackageState = uiState.value.loadPackageState.copy(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(loadPackageState = uiState.value.loadPackageState.copy(error = error))
                }
            ) {
                val pkg = repository.getMysteryPackage(packageId)
                updateState { copy(selectedPackage = pkg, loadPackageState = loadPackageState.copy(isLoading = false, error = null)) }
            }
        }
    }

    private fun purchasePackage(packageId: String) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(purchaseState = uiState.value.purchaseState.copy(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(purchaseState = uiState.value.purchaseState.copy(error = error))
                }
            ) {
                repository.purchaseMysteryPackage(packageId)
                emitSideEffect(MysteryDetailSideEffect.PurchaseSucceeded)
                // Optionally reload package or list
            }
        }
    }

    private fun clearSelected() {
        updateState { copy(selectedPackage = null) }
    }
}

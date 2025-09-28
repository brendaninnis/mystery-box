package ca.realitywargames.mysterybox.feature.mystery.presentation.effect

sealed interface MysteryDetailSideEffect {
    data class ShowError(val message: String) : MysteryDetailSideEffect
    data class ShowToast(val message: String) : MysteryDetailSideEffect
    data object PurchaseSucceeded : MysteryDetailSideEffect
}



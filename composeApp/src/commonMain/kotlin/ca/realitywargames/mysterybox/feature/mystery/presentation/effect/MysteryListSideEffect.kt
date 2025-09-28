package ca.realitywargames.mysterybox.feature.mystery.presentation.effect

sealed interface MysteryListSideEffect {
    data class ShowError(val message: String) : MysteryListSideEffect
    data class ShowToast(val message: String) : MysteryListSideEffect
}



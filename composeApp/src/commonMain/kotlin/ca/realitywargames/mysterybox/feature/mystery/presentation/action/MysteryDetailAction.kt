package ca.realitywargames.mysterybox.feature.mystery.presentation.action

sealed interface MysteryDetailAction {
    data class LoadPackage(val packageId: String) : MysteryDetailAction
    data class PurchasePackage(val packageId: String) : MysteryDetailAction
    data object ClearSelected : MysteryDetailAction
}



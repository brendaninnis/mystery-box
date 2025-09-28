package ca.realitywargames.mysterybox.feature.mystery.presentation.action

sealed interface MysteryListAction {
    data object LoadMysteries : MysteryListAction
    data object RefreshMysteries : MysteryListAction
}



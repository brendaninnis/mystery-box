package ca.realitywargames.mysterybox.feature.mystery.presentation.state

import ca.realitywargames.mysterybox.core.presentation.state.AsyncState
import ca.realitywargames.mysterybox.shared.models.MysteryPackage

data class MysteryDetailUiState(
    val selectedPackage: MysteryPackage? = null,
    val loadPackageState: AsyncState = AsyncState(),
    val purchaseState: AsyncState = AsyncState()
)



package ca.realitywargames.mysterybox.feature.mystery.presentation.state

import ca.realitywargames.mysterybox.core.data.state.UiState
import ca.realitywargames.mysterybox.shared.models.MysteryPackage

/**
 * UI State for the Mysteries list feature
 */
data class MysteryListUiState(
    val mysteries: UiState<List<MysteryPackage>> = UiState.Loading,
    val isRefreshing: Boolean = false
)



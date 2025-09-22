package ca.realitywargames.mysterybox.feature.party.presentation.state

import ca.realitywargames.mysterybox.core.presentation.state.AsyncState
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.Party

/**
 * UI State for Party feature
 */
data class PartyUiState(
    val userParties: List<Party> = emptyList(),
    val selectedParty: Party? = null,
    val mysteryPackages: Map<String, MysteryPackage> = emptyMap(),
    val loadPartiesState: AsyncState = AsyncState(),
    val createPartyState: AsyncState = AsyncState(),
    val joinPartyState: AsyncState = AsyncState(),
    val advancePhaseState: AsyncState = AsyncState()
) {
    fun getMysteryPackageForParty(mysteryPackageId: String): MysteryPackage? =
        mysteryPackages[mysteryPackageId]
}

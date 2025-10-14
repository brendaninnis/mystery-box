package ca.realitywargames.mysterybox.feature.party.presentation.effect

/**
 * One-time side effects that should be handled by the UI layer
 */
sealed interface PartySideEffect {
    
    // Navigation effects
    data class NavigateToPartyDetail(val partyId: String) : PartySideEffect
    data class NavigateToPartyInvite(val partyId: String) : PartySideEffect
    data class NavigateToPartyInstructions(val partyId: String) : PartySideEffect
    data class NavigateToPartyPhaseInstructions(val partyId: String) : PartySideEffect
    data class NavigateToPartyObjectives(val partyId: String) : PartySideEffect
    data class NavigateToPartyCharacters(val partyId: String) : PartySideEffect
    data class NavigateToPartyInventory(val partyId: String) : PartySideEffect
    data class NavigateToPartyEvidence(val partyId: String) : PartySideEffect
    data class NavigateToPartySolution(val partyId: String) : PartySideEffect
    data object NavigateBack : PartySideEffect
    
    // UI feedback effects
    data class ShowSuccessMessage(val message: String) : PartySideEffect
    data class ShowErrorMessage(val message: String) : PartySideEffect

    // Party creation effects
    data object PartyCreatedSuccessfully : PartySideEffect
    data object PartyJoinedSuccessfully : PartySideEffect
    data object PartyPhaseAdvanced : PartySideEffect
}

package ca.realitywargames.mysterybox.feature.party.presentation.action

import ca.realitywargames.mysterybox.shared.models.Party
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * All possible actions that can be performed in the Party feature
 */
@OptIn(ExperimentalTime::class)
sealed interface PartyAction {
    
    // Loading actions
    data object LoadUserParties : PartyAction
    data object RefreshParties : PartyAction
    
    // Selection actions
    data class SelectParty(val party: Party) : PartyAction
    data object ClearSelectedParty : PartyAction
    
    // Party management actions
    data class CreateParty(
        val mysteryPackageId: String,
        val title: String,
        val description: String,
        val scheduledDate: Instant,
        val maxGuests: Int
    ) : PartyAction
    
    data class JoinParty(
        val inviteCode: String,
        val name: String,
        val email: String
    ) : PartyAction
    
    data class AdvancePartyPhase(val partyId: String) : PartyAction
    
    // Navigation actions
    data class NavigateToPartyDetail(val party: Party) : PartyAction
    data class NavigateToPartySection(val party: Party, val section: PartySectionType) : PartyAction
    
    // Error handling
    data object ClearError : PartyAction
}

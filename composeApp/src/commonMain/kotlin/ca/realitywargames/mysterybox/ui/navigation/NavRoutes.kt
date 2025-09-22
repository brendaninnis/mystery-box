package ca.realitywargames.mysterybox.ui.navigation

import kotlinx.serialization.Serializable

object NavRoutes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SETTINGS = "settings"

    fun createParty(mysteryId: String) = "create_party/$mysteryId"
}

@Serializable
data class MysteryDetailRoute(val mysteryId: String)

@Serializable
data class PartyDetailRoute(val partyId: String)

@Serializable
data class PartyInviteRoute(val partyId: String)

@Serializable
data class PartyInstructionsRoute(val partyId: String)

@Serializable
data class PartyCharactersRoute(val partyId: String)

@Serializable
data class PartyInventoryRoute(val partyId: String)

@Serializable
data class PartyEvidenceRoute(val partyId: String)

@Serializable
data class PartySolutionRoute(val partyId: String)

@Serializable
data class PartyPhaseInstructionsRoute(val partyId: String)

@Serializable
data class PartyObjectivesRoute(val partyId: String)

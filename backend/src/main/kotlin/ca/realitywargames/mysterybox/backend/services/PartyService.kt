package ca.realitywargames.mysterybox.backend.services

import ca.realitywargames.mysterybox.backend.repositories.PartyRepository
import ca.realitywargames.mysterybox.shared.models.*

class PartyService(private val partyRepository: PartyRepository) {

    suspend fun getPartiesForUser(userId: String): List<Party> {
        return partyRepository.getPartiesForUser(userId)
    }

    suspend fun getParty(partyId: String): Party? {
        return partyRepository.getParty(partyId)
    }

    suspend fun createParty(
        hostId: String,
        mysteryPackageId: String,
        title: String,
        description: String,
        scheduledDate: String,
        maxGuests: Int,
        address: String? = null
    ): Party {
        return partyRepository.createParty(
            hostId = hostId,
            mysteryPackageId = mysteryPackageId,
            title = title,
            description = description,
            scheduledDate = scheduledDate,
            maxGuests = maxGuests,
            address = address
        )
    }

    suspend fun updateParty(party: Party): Party {
        return partyRepository.updateParty(party)
    }

    suspend fun advancePartyPhase(partyId: String): Party? {
        return partyRepository.advancePartyPhase(partyId)
    }

    suspend fun joinParty(userId: String, inviteCode: String): Party? {
        return partyRepository.joinPartyByInviteCode(userId, inviteCode)
    }

    suspend fun isHost(partyId: String, userId: String): Boolean {
        return partyRepository.isHost(partyId, userId)
    }

    suspend fun isUserAuthorized(partyId: String, userId: String): Boolean {
        return partyRepository.isUserAuthorized(partyId, userId)
    }
}

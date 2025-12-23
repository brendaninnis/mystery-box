package ca.realitywargames.mysterybox.backend.services

import ca.realitywargames.mysterybox.backend.repositories.PartyRepository
import ca.realitywargames.mysterybox.shared.models.*

sealed class JoinPartyError(val message: String) {
    object InvalidInviteCode : JoinPartyError("Invalid invite code")
    object PartyFull : JoinPartyError("Party has reached maximum guest capacity")
    object AlreadyJoined : JoinPartyError("You have already joined this party")
    object PartyNotJoinable : JoinPartyError("This party is not accepting new guests")
}

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

    suspend fun joinParty(userId: String, inviteCode: String): Result<Party> {
        // Find the guest record by invite code
        val guestDao = partyRepository.getGuestByInviteCode(inviteCode)
            ?: return Result.failure(IllegalArgumentException(JoinPartyError.InvalidInviteCode.message))

        val partyId = guestDao.partyId.value

        // Get the party to check its status and capacity
        val partyDao = partyRepository.getPartyById(partyId)
            ?: return Result.failure(IllegalArgumentException(JoinPartyError.InvalidInviteCode.message))

        // Check if party status allows joining (only DRAFT or PLANNED)
        val status = PartyStatus.valueOf(partyDao.status)
        if (status == PartyStatus.COMPLETED || status == PartyStatus.CANCELLED) {
            return Result.failure(IllegalArgumentException(JoinPartyError.PartyNotJoinable.message))
        }

        // Check if user is already a guest at this party
        if (partyRepository.isUserAlreadyGuest(partyId, userId)) {
            return Result.failure(IllegalArgumentException(JoinPartyError.AlreadyJoined.message))
        }

        // Check if party is at capacity
        val currentGuestCount = partyRepository.getJoinedGuestCount(partyId)
        if (currentGuestCount >= partyDao.maxGuests) {
            return Result.failure(IllegalArgumentException(JoinPartyError.PartyFull.message))
        }

        // All validations passed, join the party
        val party = partyRepository.joinPartyByInviteCode(userId, inviteCode)
            ?: return Result.failure(IllegalArgumentException(JoinPartyError.InvalidInviteCode.message))

        return Result.success(party)
    }

    suspend fun isHost(partyId: String, userId: String): Boolean {
        return partyRepository.isHost(partyId, userId)
    }

    suspend fun isUserAuthorized(partyId: String, userId: String): Boolean {
        return partyRepository.isUserAuthorized(partyId, userId)
    }
}

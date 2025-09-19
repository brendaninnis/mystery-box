package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.shared.models.CreatePartyRequest
import ca.realitywargames.mysterybox.shared.models.Guest
import ca.realitywargames.mysterybox.shared.models.GuestStatus
import ca.realitywargames.mysterybox.shared.models.JoinPartyRequest
import ca.realitywargames.mysterybox.shared.models.Party
import ca.realitywargames.mysterybox.shared.models.PartyStatus
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class PartyRepository(private val api: MysteryBoxApi) {

    fun getUserParties(): Flow<Result<List<Party>>> = flow {
        try {
            // Mock data for development
            val mockParties = listOf(
                Party(
                    id = "party1",
                    hostId = "user1",
                    mysteryPackageId = "1",
                    title = "Saturday Night Mystery",
                    description = "Our first murder mystery party!",
                    scheduledDate = "2024-01-20T19:00:00Z", // ISO 8601 format
                    status = PartyStatus.PLANNED,
                    maxGuests = 6,
                    guests = listOf(
                        Guest(
                            id = "guest1",
                            userId = "user1",
                            name = "Host",
                            characterId = null,
                            status = GuestStatus.JOINED,
                            inviteCode = "NEW${(1000..9999).random()}",
                            joinedAt = "2024-01-20T19:00:00Z", // ISO 8601 format
                        )
                    ),
                )
            )
            emit(Result.success(mockParties))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getParty(id: String): Flow<Result<Party>> = flow {
        try {
            // Mock data
            val mockParty = Party(
                id = id,
                hostId = "user1",
                mysteryPackageId = "1",
                title = "Saturday Night Mystery",
                description = "Our first murder mystery party!",
                scheduledDate = "2024-01-20T19:00:00Z", // ISO 8601 format
                status = PartyStatus.PLANNED,
                maxGuests = 6,
                guests = listOf(
                    Guest(
                        id = "guest1",
                        userId = "user1",
                        name = "Host",
                        characterId = null,
                        status = GuestStatus.JOINED,
                        inviteCode = "NEW${(1000..9999).random()}",
                        joinedAt = null,
                    )
                ),
            )
            emit(Result.success(mockParty))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun createParty(request: CreatePartyRequest): Flow<Result<Party>> = flow {
        try {
            // Mock successful creation
            val mockParty = Party(
                id = "new_party_12345",
                hostId = "user1",
                mysteryPackageId = request.mysteryPackageId,
                title = request.title,
                description = request.description,
                scheduledDate = request.scheduledDate, // Already in ISO 8601 format
                status = PartyStatus.PLANNED,
                maxGuests = request.maxGuests,
                guests = emptyList(),
            )
            emit(Result.success(mockParty))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun joinParty(request: JoinPartyRequest): Flow<Result<Party>> = flow {
        try {
            // Mock successful join
            val mockParty = Party(
                id = "joined_party",
                hostId = "host_user",
                mysteryPackageId = "1",
                title = "Joined Mystery Party",
                description = "A mystery party I joined",
                scheduledDate = "2024-01-20T19:00:00Z", // ISO 8601 format
                status = PartyStatus.PLANNED,
                maxGuests = 6,
                guests = listOf(
                    Guest(
                        id = "guest_joined",
                        userId = null,
                        name = request.name,
                        characterId = null,
                        status = GuestStatus.JOINED,
                        inviteCode = "NEW${(1000..9999).random()}",
                        )
                ),
            )
            emit(Result.success(mockParty))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun advancePartyPhase(partyId: String): Flow<Result<Party>> = flow {
        try {
            // Mock phase advancement
            emit(Result.success(Party(
                id = partyId,
                hostId = "user1",
                mysteryPackageId = "1",
                title = "Updated Party",
                description = "Phase advanced",
                scheduledDate = "2024-01-20T19:00:00Z", // ISO 8601 format
                status = PartyStatus.IN_PROGRESS,
                maxGuests = 6,
                guests = emptyList(),
                currentPhaseIndex = 1,
            )))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

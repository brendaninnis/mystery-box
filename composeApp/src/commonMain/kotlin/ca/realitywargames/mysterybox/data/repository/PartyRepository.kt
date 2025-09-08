package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.data.models.CreatePartyRequest
import ca.realitywargames.mysterybox.data.models.Guest
import ca.realitywargames.mysterybox.data.models.GuestStatus
import ca.realitywargames.mysterybox.data.models.JoinPartyRequest
import ca.realitywargames.mysterybox.data.models.Party
import ca.realitywargames.mysterybox.data.models.PartyStatus
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
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
                    inviteCode = "ABC123",
                    maxGuests = 6,
                    guests = listOf(
                        Guest(
                            id = "guest1",
                            userId = "user1",
                            email = "host@example.com",
                            name = "Host",
                            characterId = null,
                            status = GuestStatus.JOINED,
                            joinedAt = "2024-01-15T10:30:00Z" // ISO 8601 format
                        )
                    ),
                    createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                    updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
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
                inviteCode = "ABC123",
                maxGuests = 6,
                guests = listOf(
                    Guest(
                        id = "guest1",
                        userId = "user1",
                        email = "host@example.com",
                        name = "Host",
                        characterId = null,
                        status = GuestStatus.JOINED,
                        joinedAt = "2024-01-15T10:30:00Z" // ISO 8601 format
                    )
                ),
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
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
                inviteCode = "NEW${(1000..9999).random()}",
                maxGuests = request.maxGuests,
                guests = emptyList(),
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
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
                inviteCode = request.inviteCode,
                maxGuests = 6,
                guests = listOf(
                    Guest(
                        id = "guest_joined",
                        userId = null,
                        email = request.email,
                        name = request.name,
                        characterId = null,
                        status = GuestStatus.JOINED,
                        joinedAt = "2024-01-15T10:30:00Z" // ISO 8601 format
                    )
                ),
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
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
                inviteCode = "ABC123",
                maxGuests = 6,
                guests = emptyList(),
                currentPhaseIndex = 1,
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            )))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

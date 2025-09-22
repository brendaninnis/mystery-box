package ca.realitywargames.mysterybox.backend.repositories

import ca.realitywargames.mysterybox.shared.models.*

class PartyRepository {

    suspend fun getPartiesForUser(userId: String): List<Party> {
        // For now, return mock data - in real implementation this would query the database
        return listOf(
            createMockParty("party1"),
            createMockParty("party2"),
            createMockParty("party3")
        )
    }

    suspend fun getParty(partyId: String): Party? {
        // For now, return mock data - in real implementation this would query the database
        return createMockParty(partyId)
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
        // For now, return mock data - in real implementation this would insert into database
        return Party(
            id = "party_${System.currentTimeMillis()}",
            hostId = hostId,
            mysteryPackageId = mysteryPackageId,
            title = title,
            description = description,
            scheduledDate = scheduledDate,
            status = PartyStatus.DRAFT,
            maxGuests = maxGuests,
            guests = emptyList(),
            currentPhaseIndex = 0,
            gameState = null,
            address = address
        )
    }

    suspend fun updateParty(party: Party): Party {
        // For now, just return the party - in real implementation this would update the database
        return party
    }

    suspend fun advancePartyPhase(partyId: String): Party? {
        // For now, return mock updated party - in real implementation this would update the database
        val party = getParty(partyId) ?: return null
        return party.copy(currentPhaseIndex = party.currentPhaseIndex + 1)
    }

    private fun createMockParty(partyId: String): Party {
        return Party(
            id = partyId,
            hostId = "user1",
            mysteryPackageId = "550e8400-e29b-41d4-a716-446655440001",
            title = when (partyId) {
                "party1" -> "Saturday Night Mystery"
                "party2" -> "Halloween Haunted Mystery"
                "party3" -> "New Year Murder Mystery"
                else -> "Mystery Party"
            },
            description = when (partyId) {
                "party1" -> "Our first murder mystery party at home!"
                "party2" -> "Spooky Halloween themed mystery party"
                "party3" -> "Ring in the new year with a thrilling mystery"
                else -> "A thrilling murder mystery party"
            },
            scheduledDate = "2024-01-20T19:00:00Z",
            status = when (partyId) {
                "party1" -> PartyStatus.IN_PROGRESS
                "party2" -> PartyStatus.PLANNED
                "party3" -> PartyStatus.COMPLETED
                else -> PartyStatus.PLANNED
            },
            maxGuests = 8,
            guests = createMockGuests(partyId),
            currentPhaseIndex = if (partyId == "party1") 1 else 0,
            gameState = if (partyId == "party1") createMockGameState() else null,
            address = when (partyId) {
                "party1" -> "123 Main Street, Anytown"
                "party2" -> "456 Spooky Lane, Halloween Heights"
                "party3" -> "789 Celebration Ave, New City"
                else -> null
            }
        )
    }

    private fun createMockGuests(partyId: String): List<Guest> {
        return listOf(
            Guest(
                id = "guest1",
                userId = "user2",
                name = "Alice Smith",
                inviteCode = "ABC123",
                characterId = "char1",
                status = GuestStatus.JOINED,
                joinedAt = "2024-01-15T10:00:00Z",
                objectives = listOf(
                    Objective(
                        id = "obj1",
                        description = "Investigate the scene thoroughly",
                        completedAt = "2024-01-15T11:30:00Z"
                    ),
                    Objective(
                        id = "obj2", 
                        description = "Search assigned area for evidence",
                        completedAt = null
                    )
                ),
                inventory = listOf(
                    InventoryItem(
                        id = "inv1",
                        name = "Magnifying Glass",
                        description = "A detective's magnifying glass",
                        imagePath = "/images/magnifying-glass.jpg",
                        quantity = 1
                    )
                )
            ),
            Guest(
                id = "guest2",
                userId = "user3",
                name = "Bob Johnson",
                inviteCode = "DEF456",
                characterId = "char2",
                status = GuestStatus.JOINED,
                joinedAt = "2024-01-15T10:30:00Z",
                objectives = listOf(
                    Objective(
                        id = "obj3",
                        description = "Interview other guests",
                        completedAt = null
                    )
                ),
                inventory = emptyList()
            )
        )
    }

    private fun createMockGameState(): GameState {
        return GameState(
            evidence = listOf(
                Evidence(
                    id = "ev1",
                    name = "Bloody Dagger",
                    description = "A ceremonial dagger with fresh blood",
                    imagePath = "/images/bloody-dagger.jpg",
                    discoveredAt = "2024-01-15T11:00:00Z"
                )
            ),
            accusations = emptyList(),
            phaseStartTime = "2024-01-15T11:00:00Z",
            unlockedSections = listOf(
                GameStateSection.MYSTERY_INFO,
                GameStateSection.CHARACTER_INFO,
                GameStateSection.OBJECTIVES,
            ),
            solution = Solution(
                killer = "char3",
                motive = "Revenge for a past betrayal",
                method = "Poisoned wine",
                location = "The library",
                timeline = "Between 8:00 and 8:30 PM"
            )
        )
    }
}

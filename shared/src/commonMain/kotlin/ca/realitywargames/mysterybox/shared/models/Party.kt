package ca.realitywargames.mysterybox.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class Party(
    val id: String,
    val hostId: String,
    val mysteryPackageId: String,
    val title: String,
    val description: String,
    val scheduledDate: String, // ISO 8601 format
    val status: PartyStatus,
    val maxGuests: Int,
    val guests: List<Guest>,
    val currentPhaseIndex: Int = 0,
    val gameState: GameState? = null,
    val address: String? = null
)

@Serializable
enum class PartyStatus {
    DRAFT, PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}

@Serializable
data class Guest(
    val id: String,
    val userId: String? = null, // null for pending invites
    val name: String,
    val inviteCode: String,
    val characterId: String? = null,
    val status: GuestStatus,
    val joinedAt: String? = null, // ISO 8601 format
    val objectives: List<Objective> = emptyList(),
    val inventory: List<InventoryItem> = emptyList()
)

@Serializable
enum class GuestStatus {
    INVITED, JOINED, DECLINED
}

@Serializable
data class GameState(
    val evidence: List<Evidence>,
    val accusations: List<Accusation>,
    val phaseStartTime: String, // ISO 8601 format
    val phaseEndTime: String? = null, // ISO 8601 format
    val unlockedSections: List<GameStateSection> = listOf(GameStateSection.MYSTERY_INFO, GameStateSection.CHARACTER_INFO),
    val solution: Solution? = null
)

@Serializable
data class Evidence(
    val id: String,
    val name: String,
    val description: String,
    val imagePath: String,
    val discoveredAt: String // ISO 8601 format
)


@Serializable
data class Accusation(
    val id: String,
    val accuserId: String, // character ID
    val accusedId: String, // character ID
    val reason: String,
    val isCorrect: Boolean? = null,
    val madeAt: String // ISO 8601 format
)

@Serializable
data class Objective(
    val id: String,
    val description: String,
    val completedAt: String? = null // ISO 8601 format
)

@Serializable
data class InventoryItem(
    val id: String,
    val name: String,
    val description: String,
    val imagePath: String,
    val quantity: Int = 1
)

@Serializable
data class Solution(
    val killer: String, // character ID
    val motive: String,
    val method: String,
    val location: String,
    val timeline: String,
    val additionalNotes: String? = null
)

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
    val inviteCode: String,
    val maxGuests: Int,
    val guests: List<Guest>,
    val currentPhaseIndex: Int = 0,
    val gameState: GameState? = null,
    val createdAt: String, // ISO 8601 format
    val updatedAt: String  // ISO 8601 format
)

@Serializable
enum class PartyStatus {
    DRAFT, PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}

@Serializable
data class Guest(
    val id: String,
    val userId: String? = null, // null for pending invites
    val email: String,
    val name: String,
    val characterId: String? = null,
    val status: GuestStatus,
    val joinedAt: String? = null // ISO 8601 format
)

@Serializable
enum class GuestStatus {
    INVITED, JOINED, DECLINED
}

@Serializable
data class GameState(
    val evidence: List<Evidence>,
    val clues: List<Clue>,
    val accusations: List<Accusation>,
    val phaseStartTime: String, // ISO 8601 format
    val phaseEndTime: String? = null // ISO 8601 format
)

@Serializable
data class Evidence(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val discoveredAt: String, // ISO 8601 format
    val discoveredBy: String, // character ID
    val isPublic: Boolean = false
)

@Serializable
data class Clue(
    val id: String,
    val title: String,
    val description: String,
    val hint: String? = null,
    val revealedAt: String, // ISO 8601 format
    val revealedTo: List<String> // character IDs
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

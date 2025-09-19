package ca.realitywargames.mysterybox.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class GamePhase(
    val id: String,
    val name: String,
    val order: Int,
    val instructions: List<String> = emptyList(),
    val hostInstructions: List<String> = emptyList(),
    val objectivesToAdd: List<ObjectiveTemplate> = emptyList(),
    val inventoryToAdd: List<InventoryTemplate> = emptyList(),
    val evidenceToAdd: List<EvidenceTemplate> = emptyList(),
    val gameStateToUnlock: List<GameStateSection> = emptyList()
)

@Serializable
data class ObjectiveTemplate(
    val id: String,
    val description: String,
    val targetGuestIds: List<String> = emptyList() // Empty means all guests
)

@Serializable
data class InventoryTemplate(
    val id: String,
    val name: String,
    val description: String,
    val imagePath: String,
    val quantity: Int = 1,
    val targetGuestIds: List<String> = emptyList() // Empty means all guests
)

@Serializable
data class EvidenceTemplate(
    val id: String,
    val name: String,
    val description: String,
    val imagePath: String
)

@Serializable
enum class GameStateSection {
    MYSTERY_INFO,      // Initially visible
    CHARACTER_INFO,    // Initially visible  
    OBJECTIVES,        // Unlocked by phases
    INVENTORY,         // Unlocked by phases
    EVIDENCE,          // Unlocked by phases
    SOLUTION           // Final unlock
}


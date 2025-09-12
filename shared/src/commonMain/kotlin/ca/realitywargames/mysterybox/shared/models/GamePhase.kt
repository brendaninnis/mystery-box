package ca.realitywargames.mysterybox.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class GamePhase(
    val id: String,
    val name: String,
    val description: String,
    val order: Int,
    val durationMinutes: Int? = null,
    val instructions: List<String>,
    val hostInstructions: List<String>,
    val isInteractive: Boolean = false,
    val requiresHostAction: Boolean = false,
    val triggers: List<PhaseTrigger> = emptyList()
)

@Serializable
data class PhaseTrigger(
    val type: TriggerType,
    val condition: String,
    val action: String
)

@Serializable
enum class TriggerType {
    TIME_BASED, EVIDENCE_DISCOVERED, ACCUSATION_MADE, HOST_ACTION
}

@Serializable
data class PhaseProgress(
    val phaseId: String,
    val startedAt: String, // ISO 8601 format
    val completedAt: String? = null, // ISO 8601 format
    val isActive: Boolean = false,
    val hostActionsCompleted: List<String> = emptyList(),
    val playerActionsCompleted: List<String> = emptyList()
)

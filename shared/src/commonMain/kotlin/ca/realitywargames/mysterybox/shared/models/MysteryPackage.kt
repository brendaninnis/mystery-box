package ca.realitywargames.mysterybox.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class MysteryPackage(
    val id: String,
    val title: String,
    val description: String,
    val imagePath: String,
    val price: Double,
    val currency: String = "USD",
    val durationMinutes: Int,
    val minPlayers: Int,
    val maxPlayers: Int,
    val difficulty: Difficulty,
    val themes: List<String>,
    val plotSummary: String,
    val characters: List<CharacterTemplate>,
    val phases: List<GamePhase>,
    val isAvailable: Boolean = true
)

@Serializable
enum class Difficulty {
    EASY, MEDIUM, HARD, EXPERT
}

@Serializable
data class CharacterTemplate(
    val id: String,
    val name: String,
    val description: String,
    val avatarPath: String,
    val background: String
)


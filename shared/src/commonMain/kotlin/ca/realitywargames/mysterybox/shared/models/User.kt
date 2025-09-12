package ca.realitywargames.mysterybox.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String? = null,
    val isHost: Boolean = false,
    val preferences: UserPreferences = UserPreferences(),
    val createdAt: String, // ISO 8601 format
    val updatedAt: String  // ISO 8601 format
)

@Serializable
data class UserPreferences(
    val notificationsEnabled: Boolean = true,
    val theme: Theme = Theme.SYSTEM,
    val language: String = "en"
)

@Serializable
enum class Theme {
    LIGHT, DARK, SYSTEM
}

@Serializable
data class AssignedCharacter(
    val characterTemplate: CharacterTemplate,
    val playerId: String,
    val playerName: String,
    val isCurrentUser: Boolean = false
)

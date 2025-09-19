package ca.realitywargames.mysterybox.backend.models

import ca.realitywargames.mysterybox.shared.models.CharacterTemplate
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.GamePhase
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlinx.serialization.json.Json
import java.util.UUID

class UserDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(Users) {
        fun findByEmail(email: String) = UserDAO.find { Users.email eq email }.singleOrNull()
    }

    var email by Users.email
    var passwordHash by Users.passwordHash
    var name by Users.name
    var avatarPath by Users.avatarPath
    var isHost by Users.isHost
    var preferences by Users.preferences
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt

    fun toUser(): User = User(
        id = id.value.toString(),
        email = email,
        name = name,
        avatarPath = avatarPath ?: "",
        isHost = isHost,
        preferences = Json.decodeFromString(UserPreferences.serializer(), preferences),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

class MysteryPackageDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MysteryPackageDAO>(MysteryPackages)

    var title by MysteryPackages.title
    var description by MysteryPackages.description
    var imagePath by MysteryPackages.imagePath
    var price by MysteryPackages.price
    var currency by MysteryPackages.currency
    var durationMinutes by MysteryPackages.durationMinutes
    var minPlayers by MysteryPackages.minPlayers
    var maxPlayers by MysteryPackages.maxPlayers
    var difficulty by MysteryPackages.difficulty
    var themes by MysteryPackages.themes
    var plotSummary by MysteryPackages.plotSummary
    var isAvailable by MysteryPackages.isAvailable

    val characters by CharacterTemplateDAO referrersOn CharacterTemplates.mysteryPackageId
    val phases by GamePhaseDAO referrersOn GamePhases.mysteryPackageId

    fun toMysteryPackage(): MysteryPackage = MysteryPackage(
        id = id.value.toString(),
        title = title,
        description = description ?: "",
        imagePath = imagePath ?: "",
        price = price.toDouble(),
        currency = currency,
        durationMinutes = durationMinutes,
        minPlayers = minPlayers,
        maxPlayers = maxPlayers,
        difficulty = Difficulty.valueOf(difficulty),
        themes = themes?.let { Json.decodeFromString<List<String>>(it) } ?: emptyList(),
        plotSummary = plotSummary ?: "",
        characters = characters.map { it.toCharacterTemplate() },
        phases = phases.map { it.toGamePhase() },
        isAvailable = isAvailable
    )
}

class CharacterTemplateDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CharacterTemplateDAO>(CharacterTemplates)

    var mysteryPackageId by CharacterTemplates.mysteryPackageId
    var name by CharacterTemplates.name
    var description by CharacterTemplates.description
    var avatarPath by CharacterTemplates.avatarPath
    var background by CharacterTemplates.background

    fun toCharacterTemplate(): CharacterTemplate = CharacterTemplate(
        id = id.value.toString(),
        name = name,
        description = description ?: "",
        avatarPath = avatarPath ?: "",
        background = background ?: ""
    )
}

class GamePhaseDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GamePhaseDAO>(GamePhases)

    var mysteryPackageId by GamePhases.mysteryPackageId
    var name by GamePhases.name
    var description by GamePhases.description
    var orderIndex by GamePhases.orderIndex
    var durationMinutes by GamePhases.durationMinutes
    var instructions by GamePhases.instructions
    var hostInstructions by GamePhases.hostInstructions
    var isInteractive by GamePhases.isInteractive
    var requiresHostAction by GamePhases.requiresHostAction

    fun toGamePhase(): GamePhase = GamePhase(
        id = id.value.toString(),
        name = name,
        description = description ?: "",
        order = orderIndex,
        durationMinutes = durationMinutes,
        instructions = instructions?.let { Json.decodeFromString<List<String>>(it) } ?: emptyList(),
        hostInstructions = hostInstructions?.let { Json.decodeFromString<List<String>>(it) } ?: emptyList(),
        isInteractive = isInteractive,
        requiresHostAction = requiresHostAction,
        triggers = emptyList() // TODO: Implement phase triggers
    )
}

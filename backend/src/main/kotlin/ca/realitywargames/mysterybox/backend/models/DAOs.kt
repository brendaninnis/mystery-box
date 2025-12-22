package ca.realitywargames.mysterybox.backend.models

import ca.realitywargames.mysterybox.shared.models.CharacterTemplate
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.GamePhase
import ca.realitywargames.mysterybox.shared.models.GameStateSection
import ca.realitywargames.mysterybox.shared.models.Guest
import ca.realitywargames.mysterybox.shared.models.GuestStatus
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.ObjectiveTemplate
import ca.realitywargames.mysterybox.shared.models.InventoryTemplate
import ca.realitywargames.mysterybox.shared.models.EvidenceTemplate
import ca.realitywargames.mysterybox.shared.models.Party
import ca.realitywargames.mysterybox.shared.models.PartyStatus
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

    var productId by MysteryPackages.productId
    var title by MysteryPackages.title
    var description by MysteryPackages.description
    var imagePath by MysteryPackages.imagePath
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
        productId = productId,
        title = title,
        description = description ?: "",
        imagePath = imagePath ?: "",
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
    var orderIndex by GamePhases.orderIndex
    var instructions by GamePhases.instructions
    var hostInstructions by GamePhases.hostInstructions
    var objectivesToAdd by GamePhases.objectivesToAdd
    var inventoryToAdd by GamePhases.inventoryToAdd
    var evidenceToAdd by GamePhases.evidenceToAdd
    var gameStateToUnlock by GamePhases.gameStateToUnlock

    fun toGamePhase(): GamePhase = GamePhase(
        id = id.value.toString(),
        name = name,
        order = orderIndex,
        instructions = instructions?.let { Json.decodeFromString<List<String>>(it) } ?: emptyList(),
        hostInstructions = hostInstructions?.let { Json.decodeFromString<List<String>>(it) } ?: emptyList(),
        objectivesToAdd = objectivesToAdd?.let { Json.decodeFromString<List<ObjectiveTemplate>>(it) } ?: emptyList(),
        inventoryToAdd = inventoryToAdd?.let { Json.decodeFromString<List<InventoryTemplate>>(it) } ?: emptyList(),
        evidenceToAdd = evidenceToAdd?.let { Json.decodeFromString<List<EvidenceTemplate>>(it) } ?: emptyList(),
        gameStateToUnlock = gameStateToUnlock?.let { Json.decodeFromString<List<String>>(it).map { sectionName -> GameStateSection.valueOf(sectionName) } } ?: emptyList()
    )
}

class PartyDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PartyDAO>(Parties)

    var hostId by Parties.hostId
    var mysteryPackageId by Parties.mysteryPackageId
    var title by Parties.title
    var description by Parties.description
    var scheduledDate by Parties.scheduledDate
    var status by Parties.status
    var maxGuests by Parties.maxGuests
    var currentPhaseIndex by Parties.currentPhaseIndex
    var address by Parties.address
    var createdAt by Parties.createdAt
    var updatedAt by Parties.updatedAt

    val guests by GuestDAO referrersOn Guests.partyId

    fun toParty(): Party = Party(
        id = id.value.toString(),
        hostId = hostId.value.toString(),
        mysteryPackageId = mysteryPackageId.value.toString(),
        title = title,
        description = description ?: "",
        scheduledDate = scheduledDate.toString(),
        status = PartyStatus.valueOf(status),
        maxGuests = maxGuests,
        guests = guests.map { it.toGuest() },
        currentPhaseIndex = currentPhaseIndex,
        gameState = null,
        address = address
    )
}

class GuestDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GuestDAO>(Guests)

    var partyId by Guests.partyId
    var userId by Guests.userId
    var name by Guests.name
    var inviteCode by Guests.inviteCode
    var characterId by Guests.characterId
    var status by Guests.status
    var joinedAt by Guests.joinedAt

    fun toGuest(): Guest = Guest(
        id = id.value.toString(),
        userId = userId?.value?.toString(),
        name = name,
        inviteCode = inviteCode,
        characterId = characterId?.toString(),
        status = GuestStatus.valueOf(status),
        joinedAt = joinedAt?.toString(),
        objectives = emptyList(),
        inventory = emptyList()
    )
}

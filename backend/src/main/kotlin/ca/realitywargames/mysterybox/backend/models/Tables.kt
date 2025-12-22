package ca.realitywargames.mysterybox.backend.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.ReferenceOption

object Users : UUIDTable("users") {
    val email = varchar("email", 255)
    val passwordHash = varchar("password_hash", 255)
    val name = varchar("name", 255)
    val avatarPath = text("avatar_path").nullable()
    val isHost = bool("is_host").default(false)
    val preferences = text("preferences").default("{}")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

object MysteryPackages : UUIDTable("mystery_packages") {
    val productId = varchar("product_id", 100)
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val imagePath = text("image_path").nullable()
    val durationMinutes = integer("duration_minutes")
    val minPlayers = integer("min_players")
    val maxPlayers = integer("max_players")
    val difficulty = varchar("difficulty", 20)
    val themes = text("themes").nullable() // JSON array
    val plotSummary = text("plot_summary").nullable()
    val isAvailable = bool("is_available").default(true)
}

object CharacterTemplates : UUIDTable("character_templates") {
    val mysteryPackageId = reference("mystery_package_id", MysteryPackages, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val avatarPath = text("avatar_path").nullable()
    val background = text("background").nullable()
}

object GamePhases : UUIDTable("game_phases") {
    val mysteryPackageId = reference("mystery_package_id", MysteryPackages, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val orderIndex = integer("order_index")
    val instructions = text("instructions").nullable() // JSON array
    val hostInstructions = text("host_instructions").nullable() // JSON array
    val objectivesToAdd = text("objectives_to_add").nullable() // JSON array
    val inventoryToAdd = text("inventory_to_add").nullable() // JSON array
    val evidenceToAdd = text("evidence_to_add").nullable() // JSON array
    val gameStateToUnlock = text("game_state_to_unlock").nullable() // JSON array
}

object Parties : UUIDTable("parties") {
    val hostId = reference("host_id", Users, onDelete = ReferenceOption.CASCADE)
    val mysteryPackageId = reference("mystery_package_id", MysteryPackages)
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val scheduledDate = timestamp("scheduled_date")
    val status = varchar("status", 50).default("DRAFT")
    val maxGuests = integer("max_guests")
    val currentPhaseIndex = integer("current_phase_index").default(0)
    val address = text("address").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

object Guests : UUIDTable("guests") {
    val partyId = reference("party_id", Parties, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", Users, onDelete = ReferenceOption.SET_NULL).nullable()
    val name = varchar("name", 255)
    val inviteCode = varchar("invite_code", 50)
    val characterId = uuid("character_id").nullable()
    val status = varchar("status", 50).default("INVITED")
    val joinedAt = timestamp("joined_at").nullable()
}

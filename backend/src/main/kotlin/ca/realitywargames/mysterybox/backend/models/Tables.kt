package ca.realitywargames.mysterybox.backend.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.ReferenceOption
import java.math.BigDecimal

object Users : UUIDTable("users") {
    val email = varchar("email", 255)
    val passwordHash = varchar("password_hash", 255)
    val name = varchar("name", 255)
    val avatarUrl = text("avatar_url").nullable()
    val isHost = bool("is_host").default(false)
    val preferences = text("preferences").default("{}")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

object MysteryPackages : UUIDTable("mystery_packages") {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val imageUrl = text("image_url").nullable()
    val price = decimal("price", 10, 2).default(BigDecimal("0.00"))
    val currency = varchar("currency", 3).default("USD")
    val durationMinutes = integer("duration_minutes")
    val minPlayers = integer("min_players")
    val maxPlayers = integer("max_players")
    val difficulty = varchar("difficulty", 20)
    val themes = text("themes").nullable() // JSON array
    val plotSummary = text("plot_summary").nullable()
    val isAvailable = bool("is_available").default(true)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

object CharacterTemplates : UUIDTable("character_templates") {
    val mysteryPackageId = uuid("mystery_package_id").references(MysteryPackages.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val avatarUrl = text("avatar_url").nullable()
    val role = varchar("role", 20)
    val background = text("background").nullable()
    val personality = text("personality").nullable()
    val objectives = text("objectives").nullable() // JSON array
    val secrets = text("secrets").nullable() // JSON array
}

object GamePhases : UUIDTable("game_phases") {
    val mysteryPackageId = uuid("mystery_package_id").references(MysteryPackages.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val orderIndex = integer("order_index")
    val durationMinutes = integer("duration_minutes").nullable()
    val instructions = text("instructions").nullable() // JSON array
    val hostInstructions = text("host_instructions").nullable() // JSON array
    val isInteractive = bool("is_interactive").default(false)
    val requiresHostAction = bool("requires_host_action").default(false)
}

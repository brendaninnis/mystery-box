package ca.realitywargames.mysterybox.backend.repositories

import ca.realitywargames.mysterybox.backend.models.UserDAO
import ca.realitywargames.mysterybox.backend.models.Users
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository {

    suspend fun findByEmail(email: String): User? {
        return transaction {
            Users.selectAll()
                .andWhere { Users.email eq email }
                .singleOrNull()
                ?.let { UserDAO.wrapRow(it).toUser() }
        }
    }

    suspend fun findById(id: String): User? {
        return try {
            transaction {
                UserDAO.findById(UUID.fromString(id))?.toUser()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun createUser(
        email: String,
        passwordHash: String,
        name: String,
        isHost: Boolean = false
    ): User {
        val now = Clock.System.now()
        return transaction {
            UserDAO.new {
                this.email = email
                this.passwordHash = passwordHash
                this.name = name
                this.isHost = isHost
                this.preferences = Json.encodeToString(UserPreferences.serializer(), UserPreferences())
                this.createdAt = now
                this.updatedAt = now
            }.toUser()
        }
    }

    suspend fun updateUserPreferences(userId: String, preferences: UserPreferences): User? {
        return try {
            transaction {
                val dao = UserDAO.findById(UUID.fromString(userId))
                dao?.apply {
                    this.preferences = Json.encodeToString(UserPreferences.serializer(), preferences)
                    this.updatedAt = Clock.System.now()
                }?.toUser()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun verifyCredentials(email: String, password: String): User? {
        val dao = transaction {
            Users.selectAll()
                .andWhere { Users.email eq email }
                .singleOrNull()
                ?.let { UserDAO.wrapRow(it) }
        }

        if (dao != null) {
            val result = at.favre.lib.crypto.bcrypt.BCrypt.verifyer().verify(password.toCharArray(), dao.passwordHash)
            if (result.verified) {
                return dao.toUser()
            }
        }
        return null
    }
}

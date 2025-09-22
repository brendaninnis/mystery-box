package ca.realitywargames.mysterybox.feature.profile.data

import ca.realitywargames.mysterybox.core.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class UserRepository(private val api: MysteryBoxApi) {

    private var currentUser: User? = null

    suspend fun login(email: String, password: String): User {
        val response = api.login(LoginRequest(email, password))
        if (response.success && response.data != null) {
            currentUser = response.data!!.user
            return response.data!!.user
        }
        throw Exception(response.error?.message ?: "Login failed")
    }

    suspend fun register(email: String, password: String, name: String): User {
        val response = api.register(
            RegisterRequest(
                email = email,
                password = password,
                name = name
            )
        )
        if (response.success && response.data != null) {
            currentUser = response.data!!
            return response.data!!
        }
        throw Exception(response.error?.message ?: "Registration failed")
    }

    suspend fun getCurrentUser(): User? {
        val response = api.getCurrentUser()
        if (response.success) {
            currentUser = response.data
            return response.data
        }
        return null
    }

    fun logout() {
        currentUser = null
    }

    suspend fun updateUserPreferences(preferences: UserPreferences): User {
        val user = currentUser ?: throw Exception("No user logged in")
        val updatedUser = user.copy(
            preferences = preferences,
            updatedAt = "2024-01-15T10:30:00Z"
        )
        currentUser = updatedUser
        return updatedUser
    }

    fun isLoggedIn(): Boolean = currentUser != null
}
package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.data.models.*
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class UserRepository(private val api: MysteryBoxApi) {

    private var currentUser: User? = null

    fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            // Mock login for development
            val mockUser = User(
                id = "user1",
                email = email,
                name = "John Doe",
                avatarUrl = null,
                isHost = true,
                preferences = UserPreferences(),
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            )
            currentUser = mockUser
            emit(Result.success(mockUser))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun register(email: String, password: String, name: String): Flow<Result<User>> = flow {
        try {
            // Mock registration
            val mockUser = User(
                id = "user_12345",
                email = email,
                name = name,
                avatarUrl = null,
                isHost = false,
                preferences = UserPreferences(),
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            )
            currentUser = mockUser
            emit(Result.success(mockUser))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCurrentUser(): Flow<Result<User?>> = flow {
        try {
            emit(Result.success(currentUser))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun logout() {
        currentUser = null
    }

    fun updateUserPreferences(preferences: UserPreferences): Flow<Result<User>> = flow {
        try {
            currentUser?.let { user ->
                val updatedUser = user.copy(
                    preferences = preferences,
                    updatedAt = "2024-01-15T10:30:00Z" // ISO 8601 format
                )
                currentUser = updatedUser
                emit(Result.success(updatedUser))
            } ?: emit(Result.failure(Exception("No user logged in")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun isLoggedIn(): Boolean = currentUser != null
}

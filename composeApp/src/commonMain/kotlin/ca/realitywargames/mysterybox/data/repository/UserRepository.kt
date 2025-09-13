package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class UserRepository(private val api: MysteryBoxApi) {

    private var currentUser: User? = null

    fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val response = api.login(LoginRequest(email, password))
            if (response.success && response.data != null) {
                currentUser = response.data!!.user
                emit(Result.success(response.data!!.user))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Login failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun register(email: String, password: String, name: String): Flow<Result<User>> = flow {
        try {
            val response = api.register(RegisterRequest(email = email, password = password, name = name))
            if (response.success && response.data != null) {
                currentUser = response.data!!
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Registration failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCurrentUser(): Flow<Result<User?>> = flow {
        try {
            val response = api.getCurrentUser()
            if (response.success) {
                currentUser = response.data
                emit(Result.success(response.data))
            } else {
                emit(Result.success(null))
            }
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

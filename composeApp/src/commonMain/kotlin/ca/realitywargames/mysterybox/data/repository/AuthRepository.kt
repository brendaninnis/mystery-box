package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi.LoginResponse
import ca.realitywargames.mysterybox.shared.models.ApiResponse
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import ca.realitywargames.mysterybox.shared.models.User

class AuthRepository(private val api: MysteryBoxApi) {

    suspend fun register(request: RegisterRequest): Result<User> {
        return try {
            val resp: ApiResponse<User> = api.register(request)
            if (resp.success && resp.data != null) {
                Result.success(resp.data!!)
            } else {
                Result.failure(Exception(resp.error?.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<User> {
        return try {
            val resp: ApiResponse<LoginResponse> = api.login(request)
            if (resp.success && resp.data != null) {
                api.setAuthToken(resp.data!!.token)
                Result.success(resp.data!!.user)
            } else {
                Result.failure(Exception(resp.error?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        return try {
            val resp: ApiResponse<User> = api.getCurrentUser()
            if (resp.success && resp.data != null) {
                Result.success(resp.data!!)
            } else {
                Result.failure(Exception(resp.error?.message ?: "Unauthorized"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

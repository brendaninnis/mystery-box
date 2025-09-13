package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.PaginatedResponse
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MysteryRepository(private val api: MysteryBoxApi) {

    fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): Flow<Result<PaginatedResponse<MysteryPackage>>> = flow {
        try {
            val response = api.getMysteryPackages(page, pageSize, difficulty, minPlayers, maxPlayers)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Unknown error")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getMysteryPackage(id: String): Flow<Result<MysteryPackage>> = flow {
        try {
            val response = api.getMysteryPackage(id)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Mystery package not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun purchaseMysteryPackage(packageId: String): Flow<Result<String>> = flow {
        try {
            // Placeholder until backend purchase is implemented
            emit(Result.success("Purchase successful"))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

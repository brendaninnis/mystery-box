package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.data.models.Difficulty
import ca.realitywargames.mysterybox.data.models.MysteryPackage
import ca.realitywargames.mysterybox.data.models.PaginatedResponse
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
            // For now, return mock data since backend doesn't exist
            val mockResponse = PaginatedResponse(
                items = api.getMockMysteryPackages(),
                total = 1,
                page = 1,
                pageSize = 20,
                hasNext = false,
                hasPrevious = false
            )
            emit(Result.success(mockResponse))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getMysteryPackage(id: String): Flow<Result<MysteryPackage>> = flow {
        try {
            // For now, return mock data
            val mockPackage = api.getMockMysteryPackages().firstOrNull { it.id == id }
            if (mockPackage != null) {
                emit(Result.success(mockPackage))
            } else {
                emit(Result.failure(Exception("Mystery package not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun purchaseMysteryPackage(packageId: String): Flow<Result<String>> = flow {
        try {
            // Mock purchase success
            emit(Result.success("Purchase successful"))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

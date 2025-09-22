package ca.realitywargames.mysterybox.feature.mystery.data

import ca.realitywargames.mysterybox.core.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.PaginatedResponse

class MysteryRepository(private val api: MysteryBoxApi) {

    suspend fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): PaginatedResponse<MysteryPackage> {
        val response = api.getMysteryPackages(page, pageSize, difficulty, minPlayers, maxPlayers)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Unknown error")
    }

    suspend fun getMysteryPackage(id: String): MysteryPackage {
        val response = api.getMysteryPackage(id)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Mystery package not found")
    }

    suspend fun purchaseMysteryPackage(packageId: String): String {
        // Placeholder until backend purchase is implemented
        return "Purchase successful"
    }
}
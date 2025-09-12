package ca.realitywargames.mysterybox.backend.services

import ca.realitywargames.mysterybox.backend.repositories.MysteryRepository
import ca.realitywargames.mysterybox.shared.models.*

class MysteryService(private val mysteryRepository: MysteryRepository) {

    suspend fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): PaginatedResponse<MysteryPackage> {
        return mysteryRepository.getMysteryPackages(page, pageSize, difficulty, minPlayers, maxPlayers)
    }

    suspend fun getMysteryPackage(id: String): MysteryPackage? {
        return mysteryRepository.getMysteryPackage(id)
    }
}

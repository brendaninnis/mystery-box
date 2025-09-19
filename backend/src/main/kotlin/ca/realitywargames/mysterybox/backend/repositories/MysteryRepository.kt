package ca.realitywargames.mysterybox.backend.repositories

import ca.realitywargames.mysterybox.backend.models.MysteryPackageDAO
import ca.realitywargames.mysterybox.backend.models.MysteryPackages
import ca.realitywargames.mysterybox.shared.models.Difficulty
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.PaginatedResponse
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MysteryRepository {

    suspend fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): PaginatedResponse<MysteryPackage> {
        return transaction {
            val offset = (page - 1) * pageSize

            val query = MysteryPackages.selectAll()
                .andWhere { MysteryPackages.isAvailable eq true }

            difficulty?.let {
                query.andWhere { MysteryPackages.difficulty eq it.name }
            }

            minPlayers?.let {
                query.andWhere { MysteryPackages.minPlayers lessEq it }
            }

            maxPlayers?.let {
                query.andWhere { MysteryPackages.maxPlayers greaterEq it }
            }

            val totalCount = query.count()

            val packages = query
                .limit(pageSize)
                .offset(offset.toLong())
                .map { MysteryPackageDAO.wrapRow(it).toMysteryPackage() }

            PaginatedResponse(
                items = packages,
                total = totalCount.toInt(),
                page = page,
                pageSize = pageSize,
                hasNext = (page * pageSize) < totalCount.toInt(),
                hasPrevious = page > 1
            )
        }
    }

    suspend fun getMysteryPackage(id: String): MysteryPackage? {
        return try {
            transaction {
                MysteryPackageDAO.findById(UUID.fromString(id))?.toMysteryPackage()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun createMysteryPackage(packageData: MysteryPackage): MysteryPackage {
        val now = Clock.System.now()
        return transaction {
            MysteryPackageDAO.new {
                title = packageData.title
                description = packageData.description
                imagePath = packageData.imagePath
                price = packageData.price.toBigDecimal()
                currency = packageData.currency
                durationMinutes = packageData.durationMinutes
                minPlayers = packageData.minPlayers
                maxPlayers = packageData.maxPlayers
                difficulty = packageData.difficulty.name
                themes = Json.encodeToString(packageData.themes)
                plotSummary = packageData.plotSummary
                isAvailable = packageData.isAvailable
            }.toMysteryPackage()
        }
    }
}

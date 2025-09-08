package ca.realitywargames.mysterybox.data.network

import ca.realitywargames.mysterybox.data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.Json
import kotlin.time.Clock

@OptIn(ExperimentalTime::class)
class MysteryBoxApi(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "https://api.mysterybox.com/v1" // Replace with actual API URL

        fun createHttpClient(): HttpClient {
            return HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
    }

    // Auth endpoints
    suspend fun login(request: LoginRequest): ApiResponse<User> {
        return httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequest): ApiResponse<User> {
        return httpClient.post("$BASE_URL/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getCurrentUser(): ApiResponse<User> {
        return httpClient.get("$BASE_URL/auth/me").body()
    }

    // Mystery packages endpoints
    suspend fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): ApiResponse<PaginatedResponse<MysteryPackage>> {
        return httpClient.get("$BASE_URL/mystery-packages") {
            parameter("page", page)
            parameter("pageSize", pageSize)
            difficulty?.let { parameter("difficulty", it.name) }
            minPlayers?.let { parameter("minPlayers", it) }
            maxPlayers?.let { parameter("maxPlayers", it) }
        }.body()
    }

    suspend fun getMysteryPackage(id: String): ApiResponse<MysteryPackage> {
        return httpClient.get("$BASE_URL/mystery-packages/$id").body()
    }

    suspend fun purchaseMysteryPackage(request: PurchaseRequest): ApiResponse<String> {
        return httpClient.post("$BASE_URL/mystery-packages/purchase") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // Party endpoints
    suspend fun getUserParties(): ApiResponse<List<Party>> {
        return httpClient.get("$BASE_URL/parties").body()
    }

    suspend fun getParty(id: String): ApiResponse<Party> {
        return httpClient.get("$BASE_URL/parties/$id").body()
    }

    suspend fun createParty(request: CreatePartyRequest): ApiResponse<Party> {
        return httpClient.post("$BASE_URL/parties") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun joinParty(request: JoinPartyRequest): ApiResponse<Party> {
        return httpClient.post("$BASE_URL/parties/join") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updatePartyStatus(partyId: String, status: PartyStatus): ApiResponse<Party> {
        return httpClient.patch("$BASE_URL/parties/$partyId/status") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("status" to status.name))
        }.body()
    }

    suspend fun advancePartyPhase(partyId: String): ApiResponse<Party> {
        return httpClient.post("$BASE_URL/parties/$partyId/advance-phase").body()
    }

    // Game state endpoints
    suspend fun getGameState(partyId: String): ApiResponse<GameState> {
        return httpClient.get("$BASE_URL/parties/$partyId/game-state").body()
    }

    suspend fun addEvidence(partyId: String, evidence: Evidence): ApiResponse<GameState> {
        return httpClient.post("$BASE_URL/parties/$partyId/evidence") {
            contentType(ContentType.Application.Json)
            setBody(evidence)
        }.body()
    }

    suspend fun makeAccusation(partyId: String, accusation: Accusation): ApiResponse<GameState> {
        return httpClient.post("$BASE_URL/parties/$partyId/accusation") {
            contentType(ContentType.Application.Json)
            setBody(accusation)
        }.body()
    }

    // Mock data for development (since backend doesn't exist yet)
    fun getMockMysteryPackages(): List<MysteryPackage> {
        // This will be removed once the backend is implemented
        return listOf(
            MysteryPackage(
                id = "1",
                title = "The Haunted Mansion Mystery",
                description = "A classic whodunit set in a mysterious mansion",
                imageUrl = "https://example.com/mansion.jpg",
                price = 29.99,
                durationMinutes = 120,
                minPlayers = 4,
                maxPlayers = 8,
                difficulty = Difficulty.MEDIUM,
                themes = listOf("Haunted House", "Inheritance", "Family Secrets"),
                plotSummary = "When wealthy entrepreneur Reginald Harrington dies suddenly...",
                characters = emptyList(), // Would be populated in real implementation
                phases = emptyList(), // Would be populated in real implementation
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            )
        )
    }
}

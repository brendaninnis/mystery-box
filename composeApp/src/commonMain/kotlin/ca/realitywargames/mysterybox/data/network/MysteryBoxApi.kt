package ca.realitywargames.mysterybox.data.network

import ca.realitywargames.mysterybox.shared.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.Json

@OptIn(ExperimentalTime::class)
class MysteryBoxApi(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "http://localhost:8080/api/v1" // Local development URL

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
    suspend fun login(request: LoginRequest): ApiResponse<Map<String, kotlinx.serialization.json.JsonElement>> {
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
                title = "Murder and Dragons",
                description = "",
                imageUrl = "https://example.com/castle.jpg",
                price = 29.99,
                durationMinutes = 120,
                minPlayers = 6,
                maxPlayers = 20,
                difficulty = Difficulty.MEDIUM,
                themes = listOf("Fantasy", "Political Intrigue"),
                plotSummary = "",
                characters = emptyList(), // Would be populated in real implementation
                phases = emptyList(), // Would be populated in real implementation
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            ),
            MysteryPackage(
                id = "2",
                title = "[Murder at the school of Witchcraft and Wizardry]",
                description = "",
                imageUrl = "https://example.com/wizard-duel.jpg",
                price = 19.99,
                durationMinutes = 120,
                minPlayers = 6,
                maxPlayers = 16,
                difficulty = Difficulty.EASY,
                themes = listOf("Fantasy", "Pop Culture"),
                plotSummary = "",
                characters = emptyList(), // Would be populated in real implementation
                phases = emptyList(), // Would be populated in real implementation
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            ),
            MysteryPackage(
                id = "3",
                title = "[Murder at a board game night]",
                description = "",
                imageUrl = "https://example.com/mansion.jpg",
                price = 19.99,
                durationMinutes = 120,
                minPlayers = 10,
                maxPlayers = 24,
                difficulty = Difficulty.MEDIUM,
                themes = listOf("Humorous", "Meta"),
                plotSummary = "",
                characters = emptyList(), // Would be populated in real implementation
                phases = emptyList(), // Would be populated in real implementation
                createdAt = "2024-01-15T10:30:00Z", // ISO 8601 format
                updatedAt = "2024-01-15T10:30:00Z"  // ISO 8601 format
            ),
        )
    }
}

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
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import ca.realitywargames.mysterybox.data.network.apiBaseUrl

@OptIn(ExperimentalTime::class)
class MysteryBoxApi(private val httpClient: HttpClient) {

    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }

    companion object {
        private val BASE_URL get() = apiBaseUrl

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
    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
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
        return httpClient.get("$BASE_URL/auth/me") {
            headers {
                authToken?.let { append(HttpHeaders.Authorization, "Bearer $it") }
            }
        }.body()
    }

    // Mystery packages endpoints
    suspend fun getMysteryPackages(
        page: Int = 1,
        pageSize: Int = 20,
        difficulty: Difficulty? = null,
        minPlayers: Int? = null,
        maxPlayers: Int? = null
    ): ApiResponse<PaginatedResponse<MysteryPackage>> {
        return httpClient.get("$BASE_URL/mysteries") {
            parameter("page", page)
            parameter("pageSize", pageSize)
            difficulty?.let { parameter("difficulty", it.name) }
            minPlayers?.let { parameter("minPlayers", it) }
            maxPlayers?.let { parameter("maxPlayers", it) }
        }.body()
    }

    suspend fun getMysteryPackage(id: String): ApiResponse<MysteryPackage> {
        return httpClient.get("$BASE_URL/mysteries/$id").body()
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
}

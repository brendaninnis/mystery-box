package ca.realitywargames.mysterybox.backend.routes

import ca.realitywargames.mysterybox.backend.utils.DependencyInjection
import ca.realitywargames.mysterybox.shared.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.partyRoutes() {
    val partyService = DependencyInjection.partyService

    route("/parties") {
        authenticate("auth-jwt") {
            // Get parties for current user
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<List<Party>>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@get
                }

                val parties = partyService.getPartiesForUser(userId)
                call.respond(ApiResponse(success = true, data = parties))
            }

            // Get specific party
            get("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@get
                }

                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "MISSING_ID",
                                message = "Party ID is required"
                            )
                        )
                    )
                    return@get
                }

                val party = partyService.getParty(id)
                if (party == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "PARTY_NOT_FOUND",
                                message = "Party not found"
                            )
                        )
                    )
                    return@get
                }

                // Check if user is authorized to view this party
                if (!partyService.isUserAuthorized(id, userId)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "FORBIDDEN",
                                message = "You do not have access to this party"
                            )
                        )
                    )
                    return@get
                }

                call.respond(ApiResponse(success = true, data = party))
            }

            // Create new party
            post {
                val principal = call.principal<JWTPrincipal>()
                val hostId = principal?.payload?.getClaim("userId")?.asString()

                if (hostId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@post
                }

                try {
                    val request = call.receive<CreatePartyRequest>()

                    val party = partyService.createParty(
                        hostId = hostId,
                        mysteryPackageId = request.mysteryPackageId,
                        title = request.title,
                        description = request.description,
                        scheduledDate = request.scheduledDate,
                        maxGuests = request.maxGuests,
                        address = request.address
                    )

                    call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = party))
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_REQUEST",
                                message = "Invalid request data: ${e.message}"
                            )
                        )
                    )
                }
            }

            // Update party
            put("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@put
                }

                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "MISSING_ID",
                                message = "Party ID is required"
                            )
                        )
                    )
                    return@put
                }

                // Only host can update party
                if (!partyService.isHost(id, userId)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "FORBIDDEN",
                                message = "Only the host can update this party"
                            )
                        )
                    )
                    return@put
                }

                try {
                    val updatedParty = call.receive<Party>()
                    val party = partyService.updateParty(updatedParty.copy(id = id))
                    call.respond(ApiResponse(success = true, data = party))
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_REQUEST",
                                message = "Invalid request data: ${e.message}"
                            )
                        )
                    )
                }
            }

            // Advance party phase
            post("/{id}/advance") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@post
                }

                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "MISSING_ID",
                                message = "Party ID is required"
                            )
                        )
                    )
                    return@post
                }

                // Only host can advance party phase
                if (!partyService.isHost(id, userId)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "FORBIDDEN",
                                message = "Only the host can advance the party phase"
                            )
                        )
                    )
                    return@post
                }

                val party = partyService.advancePartyPhase(id)
                if (party != null) {
                    call.respond(ApiResponse(success = true, data = party))
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "PARTY_NOT_FOUND",
                                message = "Party not found"
                            )
                        )
                    )
                }
            }

            // Join party with invite code
            post("/join") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                    return@post
                }

                try {
                    val request = call.receive<JoinPartyRequest>()

                    partyService.joinParty(userId, request.inviteCode)
                        .onSuccess { party ->
                            call.respond(ApiResponse(success = true, data = party))
                        }
                        .onFailure { error ->
                            val errorCode = when (error.message) {
                                "Party has reached maximum guest capacity" -> "PARTY_FULL"
                                "You have already joined this party" -> "ALREADY_JOINED"
                                "This party is not accepting new guests" -> "PARTY_NOT_JOINABLE"
                                else -> "INVALID_INVITE_CODE"
                            }
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                ApiResponse<Party>(
                                    success = false,
                                    error = ErrorResponse(
                                        code = errorCode,
                                        message = error.message ?: "Failed to join party"
                                    )
                                )
                            )
                        }
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_REQUEST",
                                message = "Invalid request data: ${e.message}"
                            )
                        )
                    )
                }
            }
        }
    }
}

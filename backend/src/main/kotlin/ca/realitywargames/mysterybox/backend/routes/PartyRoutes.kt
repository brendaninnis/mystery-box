package ca.realitywargames.mysterybox.backend.routes

import ca.realitywargames.mysterybox.backend.utils.DependencyInjection
import ca.realitywargames.mysterybox.shared.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.partyRoutes() {
    val partyService = DependencyInjection.partyService

    route("/parties") {
        // Get parties for current user
        get {
            val userId = call.request.queryParameters["userId"] ?: "user1" // TODO: Get from authentication
            val parties = partyService.getPartiesForUser(userId)
            call.respond(ApiResponse(success = true, data = parties))
        }

        // Get specific party
        get("/{id}") {
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

        // Create new party
        post {
            try {
                val request = call.receive<CreatePartyRequest>()
                val hostId = "user1" // TODO: Get from authentication
                
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
            try {
                val request = call.receive<JoinPartyRequest>()
                val userId = "user1" // TODO: Get from authentication
                
                val party = partyService.joinParty(userId, request.inviteCode)
                if (party != null) {
                    call.respond(ApiResponse(success = true, data = party))
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        ApiResponse<Party>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_INVITE_CODE",
                                message = "Invalid invite code"
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

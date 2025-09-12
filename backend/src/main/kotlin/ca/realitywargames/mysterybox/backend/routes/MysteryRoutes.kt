package ca.realitywargames.mysterybox.backend.routes

import ca.realitywargames.mysterybox.backend.utils.DependencyInjection
import ca.realitywargames.mysterybox.shared.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mysteryRoutes() {
    val mysteryService = DependencyInjection.mysteryService

    route("/mysteries") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
            val difficulty = call.request.queryParameters["difficulty"]?.let { Difficulty.valueOf(it) }
            val minPlayers = call.request.queryParameters["minPlayers"]?.toIntOrNull()
            val maxPlayers = call.request.queryParameters["maxPlayers"]?.toIntOrNull()

            val result = mysteryService.getMysteryPackages(page, pageSize, difficulty, minPlayers, maxPlayers)
            call.respond(ApiResponse(success = true, data = result))
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    ApiResponse<MysteryPackage>(
                        success = false,
                        error = ErrorResponse(
                            code = "MISSING_ID",
                            message = "Mystery package ID is required"
                        )
                    )
                )
                return@get
            }

            val mysteryPackage = mysteryService.getMysteryPackage(id)
            if (mysteryPackage != null) {
                call.respond(ApiResponse(success = true, data = mysteryPackage))
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    ApiResponse<MysteryPackage>(
                        success = false,
                        error = ErrorResponse(
                            code = "MYSTERY_NOT_FOUND",
                            message = "Mystery package not found"
                        )
                    )
                )
            }
        }
    }
}

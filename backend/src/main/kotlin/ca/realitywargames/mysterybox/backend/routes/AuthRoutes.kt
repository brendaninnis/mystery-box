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

fun Route.authRoutes() {
    val authService = DependencyInjection.authService

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val result = authService.register(request)

            result.fold(
                onSuccess = { user ->
                    call.respond(ApiResponse(success = true, data = user))
                },
                onFailure = { error ->
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        ApiResponse<User>(
                            success = false,
                            error = ErrorResponse(
                                code = "REGISTRATION_FAILED",
                                message = error.message ?: "Registration failed"
                            )
                        )
                    )
                }
            )
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val result = authService.login(request)

            result.fold(
                onSuccess = { (user, token) ->
                    call.respond(
                        ApiResponse(
                            success = true,
                            data = LoginResponse(user = user, token = token)
                        )
                    )
                },
                onFailure = { error ->
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<LoginResponse>(
                            success = false,
                            error = ErrorResponse(
                                code = "LOGIN_FAILED",
                                message = error.message ?: "Login failed"
                            )
                        )
                    )
                }
            )
        }

        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId != null) {
                    val user = authService.getCurrentUser(userId)
                    if (user != null) {
                        call.respond(ApiResponse(success = true, data = user))
                    } else {
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            ApiResponse<User>(
                                success = false,
                                error = ErrorResponse(
                                    code = "USER_NOT_FOUND",
                                    message = "User not found"
                                )
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        ApiResponse<User>(
                            success = false,
                            error = ErrorResponse(
                                code = "INVALID_TOKEN",
                                message = "Invalid authentication token"
                            )
                        )
                    )
                }
            }
        }
    }
}

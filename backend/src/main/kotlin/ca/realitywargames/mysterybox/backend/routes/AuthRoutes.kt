package ca.realitywargames.mysterybox.backend.routes

import ca.realitywargames.mysterybox.backend.utils.DependencyInjection
import ca.realitywargames.mysterybox.shared.models.ApiResponse
import ca.realitywargames.mysterybox.shared.models.ErrorResponse
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.LoginResponse
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import ca.realitywargames.mysterybox.shared.models.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

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

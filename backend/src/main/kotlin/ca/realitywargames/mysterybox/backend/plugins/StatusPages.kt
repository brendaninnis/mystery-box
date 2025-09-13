package ca.realitywargames.mysterybox.backend.plugins

import ca.realitywargames.mysterybox.shared.models.ApiResponse
import ca.realitywargames.mysterybox.shared.models.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import java.io.PrintWriter
import java.io.StringWriter

fun Application.configureStatusPages() {
    install(StatusPages) {
        // Handle serialization/deserialization errors
        exception<SerializationException> { call, cause ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

            call.respond(
                status = HttpStatusCode.BadRequest,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "SERIALIZATION_ERROR",
                        message = if (isDevelopment) "${cause.message}\n${getStackTrace(cause)}" else "Invalid request format"
                    )
                )
            )
        }

        // Handle database errors
        exception<ExposedSQLException> { call, cause ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

            call.respond(
                status = HttpStatusCode.InternalServerError,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "DATABASE_ERROR",
                        message = if (isDevelopment) "${cause.message}\n${getStackTrace(cause)}" else "Database error occurred"
                    )
                )
            )
        }

        // Handle PostgreSQL specific errors
        exception<PSQLException> { call, cause ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

            call.respond(
                status = HttpStatusCode.InternalServerError,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "DATABASE_ERROR",
                        message = if (isDevelopment) "${cause.message}\n${getStackTrace(cause)}" else "Database error occurred"
                    )
                )
            )
        }

        // Handle IllegalArgumentException (like invalid enum values)
        exception<IllegalArgumentException> { call, cause ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

            call.respond(
                status = HttpStatusCode.BadRequest,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "INVALID_ARGUMENT",
                        message = if (isDevelopment) "${cause.message}\n${getStackTrace(cause)}" else "Invalid parameter"
                    )
                )
            )
        }

        // Handle all other exceptions
        exception<Exception> { call, cause ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

            // Log the error for debugging
            call.application.log.error("Unhandled exception: ${cause.message}", cause)

            call.respond(
                status = HttpStatusCode.InternalServerError,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "INTERNAL_ERROR",
                        message = if (isDevelopment) "${cause.message}\n${getStackTrace(cause)}" else "An unexpected error occurred"
                    )
                )
            )
        }

        // Handle 404s
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                status = HttpStatusCode.NotFound,
                ApiResponse<Nothing>(
                    success = false,
                    error = ErrorResponse(
                        code = "NOT_FOUND",
                        message = "Endpoint not found: ${call.request.uri}"
                    )
                )
            )
        }
    }
}

private fun getStackTrace(throwable: Throwable): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    throwable.printStackTrace(pw)
    return sw.toString()
}

package ca.realitywargames.mysterybox.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.origin
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val uri = call.request.uri
            val userAgent = call.request.headers["User-Agent"] ?: "Unknown"
            val remoteHost = call.request.origin.remoteHost

            "[$remoteHost] $httpMethod $uri - $status - User-Agent: $userAgent"
        }

        // Log request body for debugging (be careful with sensitive data)
        filter { call ->
            val isDevelopment = call.application.environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false
            isDevelopment && call.request.uri.startsWith("/api/")
        }
    }
}

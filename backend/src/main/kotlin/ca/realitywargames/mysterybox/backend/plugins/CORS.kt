package ca.realitywargames.mysterybox.backend.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)

        // Allow credentials for authentication
        allowCredentials = true

        // Use environment-based allowed hosts
        // In production, set CORS_ALLOWED_HOSTS=mysterynights.app,www.mysterynights.app
        val allowedHosts = System.getenv("CORS_ALLOWED_HOSTS")?.split(",")
            ?: listOf("localhost", "10.0.2.2", "127.0.0.1")

        allowedHosts.forEach { host ->
            allowHost(host, schemes = listOf("http", "https"))
        }
    }
}

package ca.realitywargames.mysterybox.backend.plugins

import ca.realitywargames.mysterybox.backend.routes.authRoutes
import ca.realitywargames.mysterybox.backend.routes.mysteryRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            authRoutes()
            mysteryRoutes()
        }
    }
}

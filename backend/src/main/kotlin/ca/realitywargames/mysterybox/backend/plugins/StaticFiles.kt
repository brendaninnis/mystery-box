package ca.realitywargames.mysterybox.backend.plugins

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.configureStaticFiles() {
    routing {
        staticResources("/images", "images")
    }
}

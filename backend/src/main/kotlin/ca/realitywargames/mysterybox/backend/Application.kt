package ca.realitywargames.mysterybox.backend

import ca.realitywargames.mysterybox.backend.database.DatabaseFactory
import ca.realitywargames.mysterybox.backend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 9090, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    // Install debugging and monitoring plugins
    configureStatusPages()
    configureCallLogging()

    // Install standard plugins
    configureCORS()
    configureContentNegotiation()
    configureAuthentication()
    configureStaticFiles()
    configureRouting()
}

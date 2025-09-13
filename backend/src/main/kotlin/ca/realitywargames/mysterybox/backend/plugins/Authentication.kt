package ca.realitywargames.mysterybox.backend.plugins

import ca.realitywargames.mysterybox.backend.utils.DependencyInjection
import com.auth0.jwt.JWT
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureAuthentication() {
    val authService = DependencyInjection.authService

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authService.jwtIssuer
            verifier(
                JWT.require(authService.algorithm)
                    .withAudience(authService.jwtAudience)
                    .withIssuer(authService.jwtIssuer)
                    .build()
            )
            validate { credential ->
                val userId = authService.validateToken(credential.payload.getClaim("userId").asString())
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

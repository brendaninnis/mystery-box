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
                // The JWT is already verified by the verifier above
                // Just check that the userId claim exists
                val userId = credential.payload.getClaim("userId")?.asString()
                if (!userId.isNullOrBlank()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

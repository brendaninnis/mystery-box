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
    val userRepository = DependencyInjection.userRepository

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
                // Check that the userId claim exists and user still exists in database
                val userId = credential.payload.getClaim("userId")?.asString()
                if (!userId.isNullOrBlank()) {
                    // Verify user still exists in database (handles deleted users)
                    val userExists = userRepository.findById(userId) != null
                    if (userExists) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null // User was deleted, reject token
                    }
                } else {
                    null
                }
            }
        }
    }
}

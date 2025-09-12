package ca.realitywargames.mysterybox.backend.utils

import ca.realitywargames.mysterybox.backend.repositories.MysteryRepository
import ca.realitywargames.mysterybox.backend.repositories.UserRepository
import ca.realitywargames.mysterybox.backend.services.AuthService
import ca.realitywargames.mysterybox.backend.services.MysteryService
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object DependencyInjection {
    private val config = HoconApplicationConfig(ConfigFactory.load())

    // Repositories
    val userRepository = UserRepository()
    val mysteryRepository = MysteryRepository()

    // Services
    val authService = AuthService(userRepository, config)
    val mysteryService = MysteryService(mysteryRepository)

    // JWT Algorithm for auth plugin
    val algorithm = Algorithm.HMAC256(config.property("jwt.secret").getString())
    val jwtIssuer = config.property("jwt.issuer").getString()
}

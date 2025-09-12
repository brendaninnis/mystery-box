package ca.realitywargames.mysterybox.backend.services

import ca.realitywargames.mysterybox.backend.repositories.UserRepository
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import ca.realitywargames.mysterybox.shared.models.User
import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

class AuthService(
    private val userRepository: UserRepository,
    private val config: ApplicationConfig
) {

    val jwtSecret = config.property("jwt.secret").getString()
    val jwtIssuer = config.property("jwt.issuer").getString()
    val jwtAudience = config.property("jwt.audience").getString()
    val algorithm = Algorithm.HMAC256(jwtSecret)

    suspend fun register(request: RegisterRequest): Result<User> {
        // Check if user already exists
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) {
            return Result.failure(IllegalArgumentException("User with this email already exists"))
        }

        // Hash password
        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())

        // Create user
        val user = userRepository.createUser(
            email = request.email,
            passwordHash = passwordHash,
            name = request.name
        )

        return Result.success(user)
    }

    suspend fun login(request: LoginRequest): Result<Pair<User, String>> {
        val user = userRepository.verifyCredentials(request.email, request.password)
            ?: return Result.failure(IllegalArgumentException("Invalid credentials"))

        // Generate JWT token
        val token = generateToken(user)
        return Result.success(Pair(user, token))
    }

    suspend fun getCurrentUser(userId: String): User? {
        return userRepository.findById(userId)
    }

    private fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000)) // 24 hours
            .sign(algorithm)
    }

    fun validateToken(token: String): String? {
        return try {
            val verifier = JWT.require(algorithm)
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .build()

            val decoded = verifier.verify(token)
            decoded.getClaim("userId").asString()
        } catch (e: Exception) {
            null
        }
    }
}

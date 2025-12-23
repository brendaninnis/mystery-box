package ca.realitywargames.mysterybox.backend.services

import ca.realitywargames.mysterybox.backend.repositories.UserRepository
import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.validation.LoginValidator
import ca.realitywargames.mysterybox.shared.validation.RegisterValidator
import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

class AuthService(
    private val userRepository: UserRepository,
    private val config: ApplicationConfig
) {
    private val isDevelopment = config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

    val jwtSecret = config.propertyOrNull("jwt.secret")?.getString()
        ?: if (isDevelopment) {
            config.property("jwt.default.secret").getString()
        } else {
            throw IllegalStateException("JWT_SECRET must be set in production")
        }

    val jwtIssuer = config.propertyOrNull("jwt.issuer")?.getString()
        ?: if (isDevelopment) {
            config.property("jwt.default.issuer").getString()
        } else {
            throw IllegalStateException("JWT_ISSUER must be set in production")
        }

    val jwtAudience = config.propertyOrNull("jwt.audience")?.getString()
        ?: if (isDevelopment) {
            config.property("jwt.default.audience").getString()
        } else {
            throw IllegalStateException("JWT_AUDIENCE must be set in production")
        }

    val algorithm = Algorithm.HMAC256(jwtSecret)

    suspend fun register(request: RegisterRequest): Result<Pair<User, String>> {
        // Validate request
        val validationResults = RegisterValidator.validate(request)
        if (!validationResults.isValid()) {
            val firstError = validationResults.getFirstError() ?: "Validation failed"
            return Result.failure(IllegalArgumentException(firstError))
        }

        // Check if user already exists
        val existingUser = userRepository.findByEmail(request.email.trim())
        if (existingUser != null) {
            return Result.failure(IllegalArgumentException("User with this email already exists"))
        }

        // Hash password
        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())

        // Create user
        val user = userRepository.createUser(
            email = request.email.trim(),
            passwordHash = passwordHash,
            name = request.name.trim()
        )

        // Generate JWT token so user is logged in after registration
        val token = generateToken(user)
        return Result.success(Pair(user, token))
    }

    suspend fun login(request: LoginRequest): Result<Pair<User, String>> {
        // Validate request
        val validationResults = LoginValidator.validate(request)
        if (!validationResults.isValid()) {
            val firstError = validationResults.getFirstError() ?: "Validation failed"
            return Result.failure(IllegalArgumentException(firstError))
        }

        val user = userRepository.verifyCredentials(request.email.trim(), request.password)
            ?: return Result.failure(IllegalArgumentException("Invalid credentials"))

        // Generate JWT token
        val token = generateToken(user)
        return Result.success(Pair(user, token))
    }

    suspend fun getCurrentUser(userId: String): User? {
        return userRepository.findById(userId)
    }

    suspend fun deleteAccount(userId: String): Boolean {
        return userRepository.deleteUser(userId)
    }

    private fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + 2592000000L)) // 30 days
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

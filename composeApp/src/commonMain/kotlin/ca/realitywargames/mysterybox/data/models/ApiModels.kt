package ca.realitywargames.mysterybox.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null,
    val message: String? = null
)

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

@Serializable
data class JoinPartyRequest(
    val inviteCode: String,
    val name: String,
    val email: String
)

@Serializable
data class CreatePartyRequest(
    val mysteryPackageId: String,
    val title: String,
    val description: String,
    val scheduledDate: String, // ISO 8601 format
    val maxGuests: Int
)

@Serializable
data class PurchaseRequest(
    val mysteryPackageId: String,
    val paymentMethodId: String? = null // For future payment integration
)

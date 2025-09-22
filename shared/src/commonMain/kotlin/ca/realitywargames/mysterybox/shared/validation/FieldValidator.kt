package ca.realitywargames.mysterybox.shared.validation

/**
 * Interface for validating individual fields
 */
interface FieldValidator<T> {
    fun validate(value: T): ValidationResult
}

/**
 * Validates email addresses
 */
object EmailValidator : FieldValidator<String> {
    // More robust email regex that matches most common email formats
    private val emailRegex = Regex(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    )
    
    override fun validate(value: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Invalid("Email is required")
            value.length > 254 -> ValidationResult.Invalid("Email is too long")
            !emailRegex.matches(value) -> ValidationResult.Invalid("Please enter a valid email address")
            else -> ValidationResult.Valid
        }
    }
}

/**
 * Validates passwords
 */
object PasswordValidator : FieldValidator<String> {
    override fun validate(value: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Invalid("Password is required")
            value.length < 6 -> ValidationResult.Invalid("Password must be at least 6 characters")
            value.length > 128 -> ValidationResult.Invalid("Password is too long")
            else -> ValidationResult.Valid
        }
    }
}

/**
 * Validates user names
 */
object NameValidator : FieldValidator<String> {
    override fun validate(value: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Invalid("Name is required")
            value.length < 2 -> ValidationResult.Invalid("Name must be at least 2 characters")
            value.length > 50 -> ValidationResult.Invalid("Name is too long")
            !value.matches(Regex("^[a-zA-Z\\s'-]+$")) -> 
                ValidationResult.Invalid("Name can only contain letters, spaces, hyphens, and apostrophes")
            else -> ValidationResult.Valid
        }
    }
}

/**
 * Generic validator that combines multiple conditions
 */
class CompositeValidator<T>(
    private val validators: List<FieldValidator<T>>
) : FieldValidator<T> {
    override fun validate(value: T): ValidationResult {
        for (validator in validators) {
            val result = validator.validate(value)
            if (!result.isValid()) {
                return result
            }
        }
        return ValidationResult.Valid
    }
}

package ca.realitywargames.mysterybox.shared.validation

/**
 * Represents the result of a validation operation
 */
sealed class ValidationResult {
    /**
     * Validation passed successfully
     */
    object Valid : ValidationResult()
    
    /**
     * Validation failed with an error message
     */
    data class Invalid(val message: String) : ValidationResult()
    
    /**
     * Check if the validation result is valid
     */
    fun isValid(): Boolean = this is Valid
    
    /**
     * Get the error message if invalid, null if valid
     */
    fun getErrorMessage(): String? = when (this) {
        is Valid -> null
        is Invalid -> message
    }
}

/**
 * Represents the result of validating multiple fields
 */
data class ValidationResults(
    val results: Map<String, ValidationResult>
) {
    /**
     * Check if all validations passed
     */
    fun isValid(): Boolean = results.values.all { it.isValid() }
    
    /**
     * Get the first error message, if any
     */
    fun getFirstError(): String? = results.values.firstNotNullOfOrNull { it.getErrorMessage() }
    
    /**
     * Get all error messages mapped by field name
     */
    fun getErrors(): Map<String, String> = results
        .filter { !it.value.isValid() }
        .mapValues { it.value.getErrorMessage()!! }
    
    /**
     * Get error for specific field
     */
    fun getFieldError(fieldName: String): String? = results[fieldName]?.getErrorMessage()
}

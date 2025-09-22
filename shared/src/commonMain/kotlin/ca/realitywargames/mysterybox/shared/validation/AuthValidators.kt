package ca.realitywargames.mysterybox.shared.validation

import ca.realitywargames.mysterybox.shared.models.LoginRequest
import ca.realitywargames.mysterybox.shared.models.RegisterRequest

/**
 * Validates login requests
 */
object LoginValidator {
    fun validate(request: LoginRequest): ValidationResults {
        val results = mutableMapOf<String, ValidationResult>()
        
        results["email"] = EmailValidator.validate(request.email.trim())
        results["password"] = PasswordValidator.validate(request.password)
        
        return ValidationResults(results)
    }
    
    /**
     * Convenience method for quick validation
     */
    fun isValid(request: LoginRequest): Boolean = validate(request).isValid()
}

/**
 * Validates registration requests
 */
object RegisterValidator {
    fun validate(request: RegisterRequest): ValidationResults {
        val results = mutableMapOf<String, ValidationResult>()
        
        results["email"] = EmailValidator.validate(request.email.trim())
        results["password"] = PasswordValidator.validate(request.password)
        results["name"] = NameValidator.validate(request.name.trim())
        
        return ValidationResults(results)
    }
    
    /**
     * Convenience method for quick validation
     */
    fun isValid(request: RegisterRequest): Boolean = validate(request).isValid()
}

/**
 * Validates individual form fields for UI validation
 */
object FormFieldValidator {
    /**
     * Validates an email field with trimming
     */
    fun validateEmail(email: String): ValidationResult {
        return EmailValidator.validate(email.trim())
    }
    
    /**
     * Validates a password field
     */
    fun validatePassword(password: String): ValidationResult {
        return PasswordValidator.validate(password)
    }
    
    /**
     * Validates a name field with trimming
     */
    fun validateName(name: String): ValidationResult {
        return NameValidator.validate(name.trim())
    }
    
    /**
     * Validates login form fields
     */
    fun validateLoginForm(email: String, password: String): Boolean {
        return validateEmail(email).isValid() && validatePassword(password).isValid()
    }
    
    /**
     * Validates registration form fields
     */
    fun validateRegisterForm(email: String, password: String, name: String): Boolean {
        return validateEmail(email).isValid() && 
               validatePassword(password).isValid() && 
               validateName(name).isValid()
    }
}

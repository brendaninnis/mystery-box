package ca.realitywargames.mysterybox.feature.profile.presentation.action

import ca.realitywargames.mysterybox.shared.models.UserPreferences

sealed interface UserAction {
    data class Login(val email: String, val password: String) : UserAction
    data class Register(val email: String, val password: String, val name: String) : UserAction
    data object Logout : UserAction
    data object CheckCurrentUser : UserAction
    data class UpdatePreferences(val preferences: UserPreferences) : UserAction
    data object ClearError : UserAction
    data object DeleteAccount : UserAction
}



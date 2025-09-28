package ca.realitywargames.mysterybox.feature.profile.presentation.effect

sealed interface UserSideEffect {
    data class ShowError(val message: String) : UserSideEffect
    data class ShowToast(val message: String) : UserSideEffect
    data object LoginSucceeded : UserSideEffect
    data object RegistrationSucceeded : UserSideEffect
}



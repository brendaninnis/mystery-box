package ca.realitywargames.mysterybox.feature.profile.presentation.state

import ca.realitywargames.mysterybox.core.presentation.state.AsyncState
import ca.realitywargames.mysterybox.shared.models.User
import ca.realitywargames.mysterybox.shared.models.UserPreferences

data class UserUiState(
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false,
    val authState: AsyncState = AsyncState(),
    val updatePrefsState: AsyncState = AsyncState()
)



package ca.realitywargames.mysterybox.core.data.network

import kotlinx.browser.localStorage

actual object TokenStorage {
    private const val TOKEN_KEY = "auth_token"

    actual fun getToken(): String? {
        return try {
            localStorage.getItem(TOKEN_KEY)
        } catch (e: Exception) {
            null
        }
    }

    actual fun setToken(token: String?) {
        try {
            if (token != null) {
                localStorage.setItem(TOKEN_KEY, token)
            } else {
                localStorage.removeItem(TOKEN_KEY)
            }
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }

    actual fun clearToken() {
        try {
            localStorage.removeItem(TOKEN_KEY)
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }
}

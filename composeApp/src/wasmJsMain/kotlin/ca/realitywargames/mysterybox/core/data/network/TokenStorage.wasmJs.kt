package ca.realitywargames.mysterybox.core.data.network

import kotlinx.browser.sessionStorage

actual object TokenStorage {
    private const val TOKEN_KEY = "auth_token"

    actual fun getToken(): String? {
        return try {
            sessionStorage.getItem(TOKEN_KEY)
        } catch (e: Exception) {
            null
        }
    }

    actual fun setToken(token: String?) {
        try {
            if (token != null) {
                sessionStorage.setItem(TOKEN_KEY, token)
            } else {
                sessionStorage.removeItem(TOKEN_KEY)
            }
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }

    actual fun clearToken() {
        try {
            sessionStorage.removeItem(TOKEN_KEY)
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }
}

package ca.realitywargames.mysterybox.core.data.network

import android.content.Context
import com.liftric.kvault.KVault

actual object TokenStorage {
    private const val TOKEN_KEY = "auth_token"
    private var kvault: KVault? = null

    fun initialize(context: Context) {
        kvault = KVault(context, "mystery_box_auth")
    }

    actual fun getToken(): String? {
        return try {
            kvault?.string(TOKEN_KEY)
        } catch (e: Exception) {
            null
        }
    }

    actual fun setToken(token: String?) {
        try {
            if (token != null) {
                kvault?.set(TOKEN_KEY, token)
            } else {
                kvault?.deleteObject(TOKEN_KEY)
            }
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }

    actual fun clearToken() {
        try {
            kvault?.deleteObject(TOKEN_KEY)
        } catch (e: Exception) {
            // Ignore storage errors
        }
    }
}

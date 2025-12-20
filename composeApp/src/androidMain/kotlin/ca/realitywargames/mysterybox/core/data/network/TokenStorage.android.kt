package ca.realitywargames.mysterybox.core.data.network

actual object TokenStorage {
    private var token: String? = null

    actual fun getToken(): String? = token

    actual fun setToken(token: String?) {
        this.token = token
    }

    actual fun clearToken() {
        token = null
    }
}

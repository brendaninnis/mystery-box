package ca.realitywargames.mysterybox.core.data.network

expect object TokenStorage {
    fun getToken(): String?
    fun setToken(token: String?)
    fun clearToken()
}

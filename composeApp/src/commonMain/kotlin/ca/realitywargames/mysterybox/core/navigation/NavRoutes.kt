package ca.realitywargames.mysterybox.core.navigation

import kotlinx.serialization.Serializable

// Main app routes
@Serializable
object HomeRoute

@Serializable  
object MainRoute

object NavRoutes {
    const val HOME = "home"
    
    fun createParty(mysteryId: String) = "create_party/$mysteryId"
}

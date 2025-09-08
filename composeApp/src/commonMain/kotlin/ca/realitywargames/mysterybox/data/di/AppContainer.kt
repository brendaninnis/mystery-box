package ca.realitywargames.mysterybox.data.di

import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.data.repository.MysteryRepository
import ca.realitywargames.mysterybox.data.repository.PartyRepository
import ca.realitywargames.mysterybox.data.repository.UserRepository

object AppContainer {
    private val httpClient = MysteryBoxApi.createHttpClient()
    private val api = MysteryBoxApi(httpClient)

    val userRepository: UserRepository by lazy {
        UserRepository(api)
    }

    val mysteryRepository: MysteryRepository by lazy {
        MysteryRepository(api)
    }

    val partyRepository: PartyRepository by lazy {
        PartyRepository(api)
    }
}

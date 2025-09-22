package ca.realitywargames.mysterybox.core.data.di

import ca.realitywargames.mysterybox.core.data.network.MysteryBoxApi
import ca.realitywargames.mysterybox.feature.mystery.data.MysteryRepository
import ca.realitywargames.mysterybox.feature.party.data.PartyRepository
import ca.realitywargames.mysterybox.feature.profile.data.UserRepository

object AppContainer {
    private val httpClient = MysteryBoxApi.Companion.createHttpClient()
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
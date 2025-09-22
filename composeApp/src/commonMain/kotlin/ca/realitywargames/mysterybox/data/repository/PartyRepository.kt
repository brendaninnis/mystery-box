package ca.realitywargames.mysterybox.data.repository

import ca.realitywargames.mysterybox.shared.models.CreatePartyRequest
import ca.realitywargames.mysterybox.shared.models.JoinPartyRequest
import ca.realitywargames.mysterybox.shared.models.Party
import ca.realitywargames.mysterybox.data.network.MysteryBoxApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class PartyRepository(private val api: MysteryBoxApi) {

    suspend fun getUserParties(): List<Party> {
        val response = api.getUserParties()
        if (response.success) return response.data ?: emptyList()
        throw Exception(response.error?.message ?: "Failed to fetch parties")
    }

    suspend fun getParty(id: String): Party {
        val response = api.getParty(id)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Failed to fetch party")
    }

    suspend fun createParty(request: CreatePartyRequest): Party {
        val response = api.createParty(request)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Failed to create party")
    }

    suspend fun joinParty(request: JoinPartyRequest): Party {
        val response = api.joinParty(request)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Failed to join party")
    }

    suspend fun advancePartyPhase(partyId: String): Party {
        val response = api.advancePartyPhase(partyId)
        if (response.success && response.data != null) return response.data!!
        throw Exception(response.error?.message ?: "Failed to advance party phase")
    }
}
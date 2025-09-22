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

    fun getUserParties(): Flow<Result<List<Party>>> = flow {
        try {
            // Call backend API
            val response = api.getUserParties()
            if (response.success) {
                emit(Result.success(response.data ?: emptyList()))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Failed to fetch parties")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getParty(id: String): Flow<Result<Party>> = flow {
        try {
            // Call backend API
            val response = api.getParty(id)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Failed to fetch party")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun createParty(request: CreatePartyRequest): Flow<Result<Party>> = flow {
        try {
            // Call backend API
            val response = api.createParty(request)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Failed to create party")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun joinParty(request: JoinPartyRequest): Flow<Result<Party>> = flow {
        try {
            // Call backend API
            val response = api.joinParty(request)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Failed to join party")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun advancePartyPhase(partyId: String): Flow<Result<Party>> = flow {
        try {
            // Call backend API
            val response = api.advancePartyPhase(partyId)
            if (response.success && response.data != null) {
                emit(Result.success(response.data!!))
            } else {
                emit(Result.failure(Exception(response.error?.message ?: "Failed to advance party phase")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
package ca.realitywargames.mysterybox.feature.party.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.core.presentation.viewmodel.BaseViewModel
import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.CreatePartyRequest
import ca.realitywargames.mysterybox.shared.models.JoinPartyRequest
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.Party
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class PartyViewModel : BaseViewModel() {

    private val partyRepository = AppContainer.partyRepository
    private val mysteryRepository = AppContainer.mysteryRepository

    private val _userParties = MutableStateFlow<List<Party>>(emptyList())
    val userParties: StateFlow<List<Party>> = _userParties.asStateFlow()

    private val _selectedParty = MutableStateFlow<Party?>(null)
    val selectedParty: StateFlow<Party?> = _selectedParty.asStateFlow()

    private val _isCreatingParty = MutableStateFlow(false)
    val isCreatingParty: StateFlow<Boolean> = _isCreatingParty.asStateFlow()

    private val _isJoiningParty = MutableStateFlow(false)
    val isJoiningParty: StateFlow<Boolean> = _isJoiningParty.asStateFlow()

    private val mysteryPackages: MutableMap<String, MysteryPackage> = mutableMapOf()

    init {
        loadUserParties()
    }

    fun loadUserParties() = launchWithLoading {
        runCatching { partyRepository.getUserParties() }
            .onSuccess { parties ->
                _userParties.value = parties
                // Load mystery packages for each party
                loadMysteryPackages(parties.map { it.mysteryPackageId }.distinct())
            }
            .onFailure { _ ->
                // Handle error
            }
    }

    private suspend fun loadMysteryPackages(mysteryPackageIds: List<String>) =
        mysteryPackageIds.forEach { mysteryId ->
            if (!mysteryPackages.containsKey(mysteryId)) {
                runCatching { mysteryRepository.getMysteryPackage(mysteryId) }
                    .onSuccess { pkg -> mysteryPackages[mysteryId] = pkg }
                    .onFailure { _ -> /* ignore load failure for cache */ }
            }
        }

    fun getMysteryPackageForParty(mysteryPackageId: String): MysteryPackage? =
        mysteryPackages[mysteryPackageId]

    fun selectParty(party: Party) {
        _selectedParty.update { party }
    }

    fun createParty(
        mysteryPackageId: String,
        title: String,
        description: String,
        scheduledDate: Instant,
        maxGuests: Int
    ) = viewModelScope.launch {
        try {
            _isCreatingParty.value = true
            val request = CreatePartyRequest(
                mysteryPackageId = mysteryPackageId,
                title = title,
                description = description,
                scheduledDate = scheduledDate.toString(),
                maxGuests = maxGuests
            )

            runCatching { partyRepository.createParty(request) }
                .onSuccess { party ->
                    loadUserParties()
                    _selectedParty.value = party
                }
                .onFailure { _ ->
                    // Handle error
                }
        } finally {
            _isCreatingParty.value = false
        }
    }

    fun joinParty(inviteCode: String, name: String, email: String) = viewModelScope.launch {
        try {
            _isJoiningParty.value = true
            val request = JoinPartyRequest(
                inviteCode = inviteCode,
            )

            runCatching { partyRepository.joinParty(request) }
                .onSuccess { party ->
                    loadUserParties()
                    _selectedParty.value = party
                }
                .onFailure { _ ->
                    // Handle error
                }
        } finally {
            _isJoiningParty.value = false
        }
    }

    fun advancePartyPhase(partyId: String) = launchWithLoading {
        runCatching { partyRepository.advancePartyPhase(partyId) }
            .onSuccess { updatedParty ->
                _selectedParty.value = updatedParty
                loadUserParties()
            }
            .onFailure { _ ->
                // Handle error
            }
    }

    fun clearSelectedParty() {
        _selectedParty.value = null
    }
}
package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.CreatePartyRequest
import ca.realitywargames.mysterybox.shared.models.JoinPartyRequest
import ca.realitywargames.mysterybox.shared.models.Party
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class PartyViewModel : BaseViewModel() {

    private val repository = AppContainer.partyRepository

    private val _userParties = MutableStateFlow<List<Party>>(emptyList())
    val userParties: StateFlow<List<Party>> = _userParties.asStateFlow()

    private val _selectedParty = MutableStateFlow<Party?>(null)
    val selectedParty: StateFlow<Party?> = _selectedParty.asStateFlow()

    private val _isCreatingParty = MutableStateFlow(false)
    val isCreatingParty: StateFlow<Boolean> = _isCreatingParty.asStateFlow()

    private val _isJoiningParty = MutableStateFlow(false)
    val isJoiningParty: StateFlow<Boolean> = _isJoiningParty.asStateFlow()

    init {
        loadUserParties()
    }

    fun loadUserParties() {
        launchWithLoading {
            repository.getUserParties().collect { result ->
                result.onSuccess { parties ->
                    _userParties.value = parties
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    fun selectParty(partyId: String) {
        launchWithLoading {
            repository.getParty(partyId).collect { result ->
                result.onSuccess { party ->
                    _selectedParty.value = party
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    fun createParty(
        mysteryPackageId: String,
        title: String,
        description: String,
        scheduledDate: Instant,
        maxGuests: Int
    ) {
        viewModelScope.launch {
            try {
                _isCreatingParty.value = true
                val request = CreatePartyRequest(
                    mysteryPackageId = mysteryPackageId,
                    title = title,
                    description = description,
                    scheduledDate = scheduledDate.toString(),
                    maxGuests = maxGuests
                )

                repository.createParty(request).collect { result ->
                    result.onSuccess { party ->
                        loadUserParties() // Refresh the list
                        _selectedParty.value = party
                    }.onFailure { exception ->
                        // Handle error
                    }
                }
            } finally {
                _isCreatingParty.value = false
            }
        }
    }

    fun joinParty(inviteCode: String, name: String, email: String) {
        viewModelScope.launch {
            try {
                _isJoiningParty.value = true
                val request = JoinPartyRequest(
                    inviteCode = inviteCode,
                    name = name,
                    email = email
                )

                repository.joinParty(request).collect { result ->
                    result.onSuccess { party ->
                        loadUserParties() // Refresh the list
                        _selectedParty.value = party
                    }.onFailure { exception ->
                        // Handle error
                    }
                }
            } finally {
                _isJoiningParty.value = false
            }
        }
    }

    fun advancePartyPhase(partyId: String) {
        launchWithLoading {
            repository.advancePartyPhase(partyId).collect { result ->
                result.onSuccess { updatedParty ->
                    _selectedParty.value = updatedParty
                    loadUserParties() // Refresh the list
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    fun clearSelectedParty() {
        _selectedParty.value = null
    }
}

package ca.realitywargames.mysterybox.feature.party.presentation.viewmodel

import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.core.presentation.state.AsyncState
import ca.realitywargames.mysterybox.core.presentation.viewmodel.MviViewModel
import ca.realitywargames.mysterybox.feature.party.presentation.action.PartyAction
import ca.realitywargames.mysterybox.feature.party.presentation.action.PartySectionType
import ca.realitywargames.mysterybox.feature.party.presentation.effect.PartySideEffect
import ca.realitywargames.mysterybox.feature.party.presentation.state.PartyUiState
import ca.realitywargames.mysterybox.shared.models.CreatePartyRequest
import ca.realitywargames.mysterybox.shared.models.JoinPartyRequest
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.shared.models.Party
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class PartyViewModel : MviViewModel<PartyUiState, PartyAction, PartySideEffect>() {

    private val partyRepository = AppContainer.partyRepository
    private val mysteryRepository = AppContainer.mysteryRepository

    override fun initialState(): PartyUiState = PartyUiState()

    init {
        onAction(PartyAction.LoadUserParties)
    }

    override fun handleAction(action: PartyAction) {
        when (action) {
            is PartyAction.LoadUserParties -> loadUserParties()
            is PartyAction.RefreshParties -> loadUserParties()
            is PartyAction.SelectParty -> selectParty(action.party)
            is PartyAction.ClearSelectedParty -> clearSelectedParty()
            is PartyAction.CreateParty -> createParty(action)
            is PartyAction.JoinParty -> joinParty(action)
            is PartyAction.AdvancePartyPhase -> advancePartyPhase(action.partyId)
            is PartyAction.NavigateToPartyDetail -> navigateToPartyDetail(action.party)
            is PartyAction.NavigateToPartySection -> navigateToPartySection(action.party, action.section)
            is PartyAction.ClearError -> clearErrors()
        }
    }

    private fun loadUserParties() {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(loadPartiesState = AsyncState(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(
                        loadPartiesState = AsyncState(error = error)
                    ).also {
                        emitSideEffect(PartySideEffect.ShowErrorMessage("Failed to load parties: $error"))
                    }
                }
            ) {
                val parties = partyRepository.getUserParties()
                
                // Load mystery packages for each party
                val mysteryPackageIds = parties.map { it.mysteryPackageId }.distinct()
                val mysteryPackages = loadMysteryPackages(mysteryPackageIds)
                
                updateState {
                    copy(
                        userParties = parties,
                        mysteryPackages = mysteryPackages,
                        loadPartiesState = AsyncState()
                    )
                }
            }
        }
    }

    private suspend fun loadMysteryPackages(mysteryPackageIds: List<String>): Map<String, MysteryPackage> {
        val packages = mutableMapOf<String, MysteryPackage>()
        
        mysteryPackageIds.forEach { mysteryId ->
            runCatching { 
                mysteryRepository.getMysteryPackage(mysteryId) 
            }.onSuccess { pkg -> 
                packages[mysteryId] = pkg 
            }
        }
        
        return packages
    }

    private fun selectParty(party: Party) {
        updateState { 
            copy(selectedParty = party) 
        }
    }

    private fun clearSelectedParty() {
        updateState { 
            copy(selectedParty = null) 
        }
    }

    private fun createParty(action: PartyAction.CreateParty) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(createPartyState = AsyncState(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(
                        createPartyState = AsyncState(error = error)
                    ).also {
                        emitSideEffect(PartySideEffect.ShowErrorMessage("Failed to create party: $error"))
                    }
                }
            ) {
                val request = CreatePartyRequest(
                    mysteryPackageId = action.mysteryPackageId,
                    title = action.title,
                    description = action.description,
                    scheduledDate = action.scheduledDate.toString(),
                    maxGuests = action.maxGuests
                )

                val party = partyRepository.createParty(request)
                
                updateState {
                    copy(
                        selectedParty = party,
                        createPartyState = AsyncState()
                    )
                }
                
                // Reload parties to get the updated list
                onAction(PartyAction.LoadUserParties)
                
                emitSideEffect(PartySideEffect.PartyCreatedSuccessfully)
                emitSideEffect(PartySideEffect.NavigateToPartyDetail(party.id))
            }
        }
    }

    private fun joinParty(action: PartyAction.JoinParty) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(joinPartyState = AsyncState(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(
                        joinPartyState = AsyncState(error = error)
                    ).also {
                        emitSideEffect(PartySideEffect.ShowErrorMessage("Failed to join party: $error"))
                    }
                }
            ) {
                val request = JoinPartyRequest(inviteCode = action.inviteCode)
                val party = partyRepository.joinParty(request)
                
                updateState {
                    copy(
                        selectedParty = party,
                        joinPartyState = AsyncState()
                    )
                }
                
                // Reload parties to get the updated list
                onAction(PartyAction.LoadUserParties)
                
                emitSideEffect(PartySideEffect.PartyJoinedSuccessfully)
                emitSideEffect(PartySideEffect.NavigateToPartyDetail(party.id))
            }
        }
    }

    private fun advancePartyPhase(partyId: String) {
        launchAsync {
            executeWithErrorHandling(
                updateLoadingState = { loading ->
                    uiState.value.copy(advancePhaseState = AsyncState(isLoading = loading))
                },
                onError = { error ->
                    uiState.value.copy(
                        advancePhaseState = AsyncState(error = error)
                    ).also {
                        emitSideEffect(PartySideEffect.ShowErrorMessage("Failed to advance party phase: $error"))
                    }
                }
            ) {
                val updatedParty = partyRepository.advancePartyPhase(partyId)
                
                updateState {
                    copy(
                        selectedParty = updatedParty,
                        advancePhaseState = AsyncState()
                    )
                }
                
                // Reload parties to get the updated list
                onAction(PartyAction.LoadUserParties)
                
                emitSideEffect(PartySideEffect.PartyPhaseAdvanced)
                emitSideEffect(PartySideEffect.ShowSuccessMessage("Party phase advanced successfully"))
            }
        }
    }

    private fun navigateToPartyDetail(party: Party) {
        selectParty(party)
        emitSideEffect(PartySideEffect.NavigateToPartyDetail(party.id))
    }
    
    private fun navigateToPartySection(party: Party, section: PartySectionType) {
        selectParty(party)
        when (section) {
            PartySectionType.INVITE -> 
                emitSideEffect(PartySideEffect.NavigateToPartyInvite(party.id))
            PartySectionType.INSTRUCTIONS -> 
                emitSideEffect(PartySideEffect.NavigateToPartyInstructions(party.id))
            PartySectionType.PHASE_INSTRUCTIONS -> 
                emitSideEffect(PartySideEffect.NavigateToPartyPhaseInstructions(party.id))
            PartySectionType.OBJECTIVES -> 
                emitSideEffect(PartySideEffect.NavigateToPartyObjectives(party.id))
            PartySectionType.CHARACTERS -> 
                emitSideEffect(PartySideEffect.NavigateToPartyCharacters(party.id))
            PartySectionType.INVENTORY -> 
                emitSideEffect(PartySideEffect.NavigateToPartyInventory(party.id))
            PartySectionType.EVIDENCE -> 
                emitSideEffect(PartySideEffect.NavigateToPartyEvidence(party.id))
            PartySectionType.SOLUTION -> 
                emitSideEffect(PartySideEffect.NavigateToPartySolution(party.id))
            PartySectionType.PARTY_DETAIL -> 
                emitSideEffect(PartySideEffect.NavigateToPartyDetail(party.id))
        }
    }

    private fun clearErrors() {
        updateState {
            copy(
                loadPartiesState = loadPartiesState.copy(error = null),
                createPartyState = createPartyState.copy(error = null),
                joinPartyState = joinPartyState.copy(error = null),
                advancePhaseState = advancePhaseState.copy(error = null)
            )
        }
    }

}
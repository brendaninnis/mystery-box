package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.Accusation
import ca.realitywargames.mysterybox.shared.models.AssignedCharacter
import ca.realitywargames.mysterybox.shared.models.CharacterTemplate
import ca.realitywargames.mysterybox.shared.models.Evidence
import ca.realitywargames.mysterybox.shared.models.GamePhase
import ca.realitywargames.mysterybox.shared.models.GameState
import ca.realitywargames.mysterybox.shared.models.GameStateSection
import ca.realitywargames.mysterybox.shared.models.Party
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class GameViewModel : BaseViewModel() {

    private val partyRepository = AppContainer.partyRepository

    private val _currentParty = MutableStateFlow<Party?>(null)
    val currentParty: StateFlow<Party?> = _currentParty.asStateFlow()

    private val _currentPhase = MutableStateFlow<GamePhase?>(null)
    val currentPhase: StateFlow<GamePhase?> = _currentPhase.asStateFlow()

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _userCharacter = MutableStateFlow<AssignedCharacter?>(null)
    val userCharacter: StateFlow<AssignedCharacter?> = _userCharacter.asStateFlow()

    private val _availableEvidence = MutableStateFlow<List<Evidence>>(emptyList())
    val availableEvidence: StateFlow<List<Evidence>> = _availableEvidence.asStateFlow()


    private val _accusations = MutableStateFlow<List<Accusation>>(emptyList())
    val accusations: StateFlow<List<Accusation>> = _accusations.asStateFlow()

    fun loadGameState(partyId: String) {
        launchWithLoading {
            partyRepository.getParty(partyId).collect { result ->
                result.onSuccess { party ->
                    _currentParty.value = party

                    // Mock game state data
                    val mockGameState = GameState(
                        evidence = listOf(
                            Evidence(
                                id = "evidence1",
                                name = "Bloody Knife",
                                description = "A knife found in the study with blood stains",
                                imagePath = "/images/bloody-knife.jpg",
                                discoveredAt = "2024-01-15T10:30:00Z" // ISO 8601 format
                            )
                        ),
                        accusations = emptyList(),
                        phaseStartTime = "2024-01-15T10:30:00Z", // ISO 8601 format
                        unlockedSections = listOf(GameStateSection.MYSTERY_INFO, GameStateSection.CHARACTER_INFO, GameStateSection.EVIDENCE)
                    )

                    _gameState.value = mockGameState
                    _availableEvidence.value = mockGameState.evidence
                    _accusations.value = mockGameState.accusations

                    // Mock current phase
                    val mockPhase = GamePhase(
                        id = "phase1",
                        name = "The Murder",
                        order = 1,
                        instructions = listOf(
                            "Gather in the main hall",
                            "Listen to the host's announcement", 
                            "Begin asking initial questions"
                        ),
                        hostInstructions = listOf(
                            "Describe the scene dramatically",
                            "Distribute initial clues",
                            "Answer questions about the academy"
                        )
                    )

                    _currentPhase.value = mockPhase

                    // Mock user character
                    val mockCharacter = AssignedCharacter(
                        characterTemplate = CharacterTemplate(
                            id = "detective",
                            name = "Detective Sarah Miller",
                            description = "A sharp-witted detective known for solving impossible cases",
                            avatarPath = "https://example.com/detective.jpg",
                            background = "Former police detective turned private investigator"
                        ),
                        playerId = "user1",
                        playerName = "John Doe",
                        isCurrentUser = true
                    )

                    _userCharacter.value = mockCharacter
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    fun advanceToNextPhase() {
        _currentParty.value?.let { party ->
            viewModelScope.launch {
                partyRepository.advancePartyPhase(party.id).collect { result ->
                    result.onSuccess { updatedParty ->
                        _currentParty.value = updatedParty
                        // Update phase based on new currentPhaseIndex
                        updateCurrentPhase(updatedParty.currentPhaseIndex)
                    }.onFailure { exception ->
                        // Handle error
                    }
                }
            }
        }
    }

    private fun updateCurrentPhase(phaseIndex: Int) {
        // Mock phase update - in real implementation, this would come from the mystery package
        val mockPhases = listOf(
            GamePhase(
                id = "phase1",
                name = "The Murder",
                order = 1,
                instructions = listOf("Examine the crime scene"),
                hostInstructions = listOf("Read the murder announcement")
            ),
            GamePhase(
                id = "phase2",
                name = "Investigation",
                order = 2,
                instructions = listOf("Search for clues", "Interview witnesses"),
                hostInstructions = listOf("Facilitate player interactions")
            ),
            GamePhase(
                id = "phase3",
                name = "The Resolution",
                order = 3,
                instructions = listOf("Make your final accusation"),
                hostInstructions = listOf("Reveal the solution")
            )
        )

        _currentPhase.value = mockPhases.getOrNull(phaseIndex)
    }

    fun makeAccusation(accusedId: String, reason: String) {
        val accusation = Accusation(
            id = "accusation_12345",
            accuserId = _userCharacter.value?.characterTemplate?.id ?: "unknown",
            accusedId = accusedId,
            reason = reason,
            madeAt = "2024-01-15T10:30:00Z" // ISO 8601 format
        )

        _accusations.value = _accusations.value + accusation
    }

    fun clearGameState() {
        _currentParty.value = null
        _currentPhase.value = null
        _gameState.value = null
        _userCharacter.value = null
        _availableEvidence.value = emptyList()
        _accusations.value = emptyList()
    }
}

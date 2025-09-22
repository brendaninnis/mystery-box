package ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.core.presentation.viewmodel.BaseViewModel
import ca.realitywargames.mysterybox.core.data.di.AppContainer
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MysteryViewModel : BaseViewModel() {

    private val repository = AppContainer.mysteryRepository

    private val _mysteryPackages = MutableStateFlow<List<MysteryPackage>>(emptyList())
    val mysteryPackages: StateFlow<List<MysteryPackage>> = _mysteryPackages.asStateFlow()

    private val _selectedPackage = MutableStateFlow<MysteryPackage?>(null)
    val selectedPackage: StateFlow<MysteryPackage?> = _selectedPackage.asStateFlow()

    private val _isPurchasing = MutableStateFlow(false)
    val isPurchasing: StateFlow<Boolean> = _isPurchasing.asStateFlow()

    init {
        loadMysteryPackages()
    }

    fun loadMysteryPackages() {
        launchWithLoading {
            runCatching { repository.getMysteryPackages() }
                .onSuccess { response ->
                    _mysteryPackages.value = response.items
                }
                .onFailure { _ ->
                    // Handle error - could emit to error state
                }
        }
    }

    fun selectMysteryPackage(packageId: String) {
        launchWithLoading {
            runCatching { repository.getMysteryPackage(packageId) }
                .onSuccess { mysteryPackage ->
                    _selectedPackage.value = mysteryPackage
                }
                .onFailure { _ ->
                    // Handle error
                }
        }
    }

    fun purchaseMysteryPackage(packageId: String) {
        viewModelScope.launch {
            try {
                _isPurchasing.value = true
                runCatching { repository.purchaseMysteryPackage(packageId) }
                    .onSuccess {
                        // Handle success - maybe refresh packages or show success message
                        loadMysteryPackages()
                    }
                    .onFailure { _ ->
                        // Handle error
                    }
            } finally {
                _isPurchasing.value = false
            }
        }
    }

    fun clearSelectedPackage() {
        _selectedPackage.value = null
    }
}

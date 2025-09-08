package ca.realitywargames.mysterybox.ui.viewmodel

import androidx.lifecycle.viewModelScope
import ca.realitywargames.mysterybox.data.di.AppContainer
import ca.realitywargames.mysterybox.data.models.MysteryPackage
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
            repository.getMysteryPackages().collect { result ->
                result.onSuccess { response ->
                    _mysteryPackages.value = response.items
                }.onFailure { exception ->
                    // Handle error - could emit to error state
                }
            }
        }
    }

    fun selectMysteryPackage(packageId: String) {
        launchWithLoading {
            repository.getMysteryPackage(packageId).collect { result ->
                result.onSuccess { mysteryPackage ->
                    _selectedPackage.value = mysteryPackage
                }.onFailure { exception ->
                    // Handle error
                }
            }
        }
    }

    fun purchaseMysteryPackage(packageId: String) {
        viewModelScope.launch {
            try {
                _isPurchasing.value = true
                repository.purchaseMysteryPackage(packageId).collect { result ->
                    result.onSuccess { message ->
                        // Handle success - maybe refresh packages or show success message
                        loadMysteryPackages()
                    }.onFailure { exception ->
                        // Handle error
                    }
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

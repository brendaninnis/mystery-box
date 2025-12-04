package ca.brendaninnis.kmpiap

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object IAPRepository {
    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var platformIAP: PlatformIAP

    private val _products: MutableStateFlow<Map<String, IAPProduct>> = MutableStateFlow(emptyMap())
    val products: StateFlow<Map<String, IAPProduct>> = _products

    internal fun initialize(platformIAP: PlatformIAP) {
        this.platformIAP = platformIAP
    }

    fun fetchProducts(identifiers: List<String>) {
        if (!::platformIAP.isInitialized) {
            throw IllegalStateException("IAPRepository not initialized. Call IAPRepository.initialize() first.")
        }

        scope.launch {
            val fetchedProducts = platformIAP.getIAPProducts(identifiers)
            _products.update { fetchedProducts }
        }
    }
}

internal interface PlatformIAP {
    suspend fun getIAPProducts(productIdentifiers: List<String>): Map<String, IAPProduct>
}

data class IAPProduct(
    val id: String,
    val title: String,
    val description: String,
    val formattedPrice: String,
)
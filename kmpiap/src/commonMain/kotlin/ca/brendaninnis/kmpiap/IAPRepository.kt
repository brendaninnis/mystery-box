package ca.brendaninnis.kmpiap

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object IAPRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var platformIAP: PlatformIAP

    private val _products: MutableStateFlow<List<IAPProduct>> = MutableStateFlow(emptyList())
    val products: MutableStateFlow<List<IAPProduct>> = _products

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
            println("Fetched products: $fetchedProducts")
        }
    }
}

internal interface PlatformIAP {
    suspend fun getIAPProducts(productIdentifiers: List<String>): List<IAPProduct>
}

data class IAPProduct(
    val id: String,
    val title: String,
    val description: String,
    val formattedPrice: String,
)
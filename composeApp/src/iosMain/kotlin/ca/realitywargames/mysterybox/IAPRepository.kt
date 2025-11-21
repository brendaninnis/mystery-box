package ca.realitywargames.mysterybox

import ca.brendaninnis.kmpiap.IAPRepository
import ca.brendaninnis.kmpiap.initialize

fun initializeIAP() {
    IAPRepository.initialize().also {
        IAPRepository.fetchProducts(listOf("testmysterypackage"))
    }
}
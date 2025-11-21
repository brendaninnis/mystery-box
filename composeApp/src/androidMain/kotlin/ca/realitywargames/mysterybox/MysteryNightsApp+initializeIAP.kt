package ca.realitywargames.mysterybox

import ca.brendaninnis.kmpiap.IAPRepository
import ca.brendaninnis.kmpiap.initialize

fun MysteryNightsApp.initializeIAP() {
    IAPRepository.initialize(this).also {
        IAPRepository.fetchProducts(
            listOf(
                "murderanddragons",
                "witchcraftschool",
                "boardgamemurder",
            )
        )
    }
}
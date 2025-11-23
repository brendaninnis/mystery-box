package ca.brendaninnis.kmpiap

class FakePlatformIAP: PlatformIAP {
    override suspend fun getIAPProducts(productIdentifiers: List<String>): Map<String, IAPProduct> =
        mapOf(
            "murderanddragons" to IAPProduct(
                id = "murderanddragons",
                title = "Murder and Dragons",
                description = "Unlock the Murder and Dragons mystery package",
                formattedPrice = "29.99",
            ),
            "witchcraftschool" to IAPProduct(
                id = "witchcraftschool",
                title = "Murder at te School of Witchcraft and Wizardry",
                description = "Unlock the Mystery Box mystery package",
                formattedPrice = "19.99",
            ),
            "boardgamemurder" to IAPProduct(
                id = "boardgamemurder",
                title = "Murder at a Board Game Night",
                description = "Unlock the Murder at a Board Game Night mystery package",
                formattedPrice = "19.99",
            ),
        )
}
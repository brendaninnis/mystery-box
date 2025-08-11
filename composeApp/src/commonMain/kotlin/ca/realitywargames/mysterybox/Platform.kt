package ca.realitywargames.mysterybox

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
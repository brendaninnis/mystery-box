package ca.realitywargames.mysterybox.preview

import ca.realitywargames.mysterybox.shared.models.*

/**
 * Mock data providers for Compose previews
 */
object MockData {
    
    fun sampleMysteryPackages(): List<MysteryPackage> = listOf(
        MysteryPackage(
            id = "mystery-1",
            title = "Murder at Moonlight Manor",
            description = "A classic whodunit set in a Victorian mansion during a stormy night. Someone has been murdered, and everyone is a suspect.",
            imagePath = "/images/moonlight-manor.jpg",
            price = 29.99,
            currency = "USD",
            durationMinutes = 180,
            minPlayers = 6,
            maxPlayers = 8,
            difficulty = Difficulty.MEDIUM,
            themes = listOf("Victorian", "Classic Mystery", "Mansion"),
            plotSummary = "Lord Blackwood has been found dead in his study during a dinner party. The doors were locked from the inside, but the guests all have motives and opportunity.",
            characters = listOf(
                CharacterTemplate(
                    id = "char-1",
                    name = "Lady Victoria Blackwood",
                    description = "The victim's estranged wife",
                    avatarPath = "/avatars/lady-victoria.jpg",
                    background = "Married to Lord Blackwood for his money, but their relationship has been rocky."
                ),
                CharacterTemplate(
                    id = "char-2", 
                    name = "Dr. Harrison Webb",
                    description = "The family physician",
                    avatarPath = "/avatars/dr-webb.jpg",
                    background = "Has been treating the family for years and knows all their secrets."
                )
            ),
            phases = listOf(
                GamePhase(
                    id = "phase-1",
                    name = "Discovery",
                    order = 1,
                    instructions = listOf("Examine the crime scene", "Interview other guests"),
                    hostInstructions = listOf("Set up the study", "Brief players on their characters")
                )
            ),
            isAvailable = true
        ),
        MysteryPackage(
            id = "mystery-2",
            title = "Death on the Orient Express",
            description = "A murder mystery aboard a luxurious train journey. When a passenger is found dead, everyone becomes a suspect.",
            imagePath = "/images/orient-express.jpg", 
            price = 34.99,
            currency = "USD",
            durationMinutes = 210,
            minPlayers = 8,
            maxPlayers = 10,
            difficulty = Difficulty.HARD,
            themes = listOf("Train", "1930s", "Classic Literature"),
            plotSummary = "A wealthy businessman is murdered in his compartment during the night. The train is snowed in, and the killer must be among the passengers.",
            characters = listOf(
                CharacterTemplate(
                    id = "char-3",
                    name = "Detective Hercule Poirot",
                    description = "Famous Belgian detective",
                    avatarPath = "/avatars/poirot.jpg",
                    background = "World-renowned detective with an eye for detail."
                )
            ),
            phases = listOf(),
            isAvailable = true
        ),
        MysteryPackage(
            id = "mystery-3", 
            title = "Hollywood Homicide",
            description = "A glamorous murder mystery set in 1940s Hollywood. A famous actress has been found dead on set.",
            imagePath = "/images/hollywood-homicide.jpg",
            price = 27.99,
            currency = "USD", 
            durationMinutes = 150,
            minPlayers = 5,
            maxPlayers = 7,
            difficulty = Difficulty.EASY,
            themes = listOf("Hollywood", "1940s", "Glamour"),
            plotSummary = "Star actress Marilyn Starlight is found dead in her dressing room. Was it jealousy, revenge, or something more sinister?",
            characters = listOf(),
            phases = listOf(),
            isAvailable = true
        )
    )
    
    fun sampleParties(): List<Party> = listOf(
        Party(
            id = "party-1",
            hostId = "user-1", 
            mysteryPackageId = "mystery-1",
            title = "Saturday Night Mystery",
            description = "Our monthly murder mystery party! Come dressed for the Victorian era.",
            scheduledDate = "2024-02-10T19:00:00Z",
            status = PartyStatus.PLANNED,
            maxGuests = 8,
            guests = listOf(
                Guest(
                    id = "guest-1",
                    userId = "user-2",
                    name = "Sarah Johnson",
                    inviteCode = "ABC123",
                    characterId = "char-1",
                    status = GuestStatus.JOINED,
                    joinedAt = "2024-01-15T10:30:00Z"
                ),
                Guest(
                    id = "guest-2", 
                    userId = "user-3",
                    name = "Mike Chen",
                    inviteCode = "DEF456", 
                    characterId = "char-2",
                    status = GuestStatus.JOINED,
                    joinedAt = "2024-01-16T14:22:00Z"
                ),
                Guest(
                    id = "guest-3",
                    userId = null,
                    name = "Emma Wilson",
                    inviteCode = "GHI789",
                    characterId = null,
                    status = GuestStatus.INVITED
                )
            ),
            address = "123 Oak Street, Portland, OR"
        ),
        Party(
            id = "party-2",
            hostId = "user-1",
            mysteryPackageId = "mystery-2", 
            title = "New Year's Murder Mystery",
            description = "Ring in the new year with a thrilling whodunit aboard the Orient Express!",
            scheduledDate = "2024-01-31T20:00:00Z",
            status = PartyStatus.IN_PROGRESS,
            maxGuests = 10,
            guests = listOf(
                Guest(
                    id = "guest-4",
                    userId = "user-4",
                    name = "David Rodriguez",
                    inviteCode = "JKL012",
                    characterId = "char-3", 
                    status = GuestStatus.JOINED,
                    joinedAt = "2024-01-10T09:15:00Z"
                )
            ),
            currentPhaseIndex = 1,
            gameState = GameState(
                evidence = listOf(
                    Evidence(
                        id = "evidence-1",
                        name = "Bloody Knife", 
                        description = "A knife with blood stains found in the victim's compartment",
                        imagePath = "/evidence/knife.jpg",
                        discoveredAt = "2024-01-31T20:45:00Z"
                    )
                ),
                accusations = listOf(),
                phaseStartTime = "2024-01-31T20:30:00Z"
            ),
            address = "Downtown Event Center"
        ),
        Party(
            id = "party-3",
            hostId = "user-1",
            mysteryPackageId = "mystery-3",
            title = "Hollywood Glamour Night", 
            description = "Step into the golden age of Hollywood for a night of mystery and intrigue.",
            scheduledDate = "2024-01-05T18:30:00Z",
            status = PartyStatus.COMPLETED,
            maxGuests = 7,
            guests = listOf(
                Guest(
                    id = "guest-5",
                    userId = "user-5",
                    name = "Lisa Thompson",
                    inviteCode = "MNO345",
                    characterId = "char-4",
                    status = GuestStatus.JOINED,
                    joinedAt = "2023-12-20T11:00:00Z"
                )
            ),
            address = "The Roosevelt Hotel"
        )
    )
    
    fun sampleMysteryPackage(): MysteryPackage = sampleMysteryPackages().first()
    
    fun sampleParty(): Party = sampleParties().first()
    
    fun sampleUser(): User = User(
        id = "user-1",
        email = "demo@mysterybox.com", 
        name = "Demo User",
        avatarPath = "/avatars/demo-user.jpg",
        isHost = true,
        preferences = UserPreferences(
            notificationsEnabled = true,
            theme = Theme.SYSTEM,
            language = "en"
        ),
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-01-15T10:30:00Z"
    )
}

package ca.realitywargames.mysterybox.backend.repositories

import ca.realitywargames.mysterybox.backend.models.GuestDAO
import ca.realitywargames.mysterybox.backend.models.Guests
import ca.realitywargames.mysterybox.backend.models.PartyDAO
import ca.realitywargames.mysterybox.backend.models.Parties
import ca.realitywargames.mysterybox.shared.models.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PartyRepository {

    suspend fun getPartiesForUser(userId: String): List<Party> {
        return try {
            val userUuid = UUID.fromString(userId)
            transaction {
                // Get parties where user is host OR user is a joined guest
                val hostedParties = PartyDAO.find { Parties.hostId eq userUuid }.toList()

                val guestPartyIds = Guests.selectAll()
                    .where { (Guests.userId eq userUuid) and (Guests.status eq GuestStatus.JOINED.name) }
                    .map { it[Guests.partyId].value }
                    .toSet()

                val guestParties = if (guestPartyIds.isNotEmpty()) {
                    PartyDAO.find { Parties.id inList guestPartyIds }.toList()
                } else {
                    emptyList()
                }

                (hostedParties + guestParties).distinctBy { it.id.value }.map { it.toParty() }
            }
        } catch (e: IllegalArgumentException) {
            emptyList() // Invalid UUID format
        }
    }

    suspend fun getParty(partyId: String): Party? {
        return try {
            transaction {
                PartyDAO.findById(UUID.fromString(partyId))?.toParty()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun createParty(
        hostId: String,
        mysteryPackageId: String,
        title: String,
        description: String,
        scheduledDate: String,
        maxGuests: Int,
        address: String? = null
    ): Party {
        val now = Clock.System.now()
        val scheduledInstant = Instant.parse(scheduledDate)
        return transaction {
            PartyDAO.new {
                this.hostId = org.jetbrains.exposed.dao.id.EntityID(UUID.fromString(hostId), ca.realitywargames.mysterybox.backend.models.Users)
                this.mysteryPackageId = org.jetbrains.exposed.dao.id.EntityID(UUID.fromString(mysteryPackageId), ca.realitywargames.mysterybox.backend.models.MysteryPackages)
                this.title = title
                this.description = description
                this.scheduledDate = scheduledInstant
                this.status = PartyStatus.DRAFT.name
                this.maxGuests = maxGuests
                this.currentPhaseIndex = 0
                this.address = address
                this.createdAt = now
                this.updatedAt = now
            }.toParty()
        }
    }

    suspend fun updateParty(party: Party): Party {
        return transaction {
            val dao = PartyDAO.findById(UUID.fromString(party.id))
                ?: throw IllegalArgumentException("Party not found")

            dao.apply {
                title = party.title
                description = party.description
                scheduledDate = Instant.parse(party.scheduledDate)
                status = party.status.name
                maxGuests = party.maxGuests
                currentPhaseIndex = party.currentPhaseIndex
                address = party.address
                updatedAt = Clock.System.now()
            }.toParty()
        }
    }

    suspend fun advancePartyPhase(partyId: String): Party? {
        return try {
            transaction {
                val dao = PartyDAO.findById(UUID.fromString(partyId)) ?: return@transaction null
                dao.apply {
                    currentPhaseIndex = currentPhaseIndex + 1
                    updatedAt = Clock.System.now()
                }.toParty()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun joinPartyByInviteCode(userId: String, inviteCode: String): Party? {
        return try {
            val userUuid = UUID.fromString(userId)
            transaction {
                // Find guest by invite code
                val guestDao = GuestDAO.find { Guests.inviteCode eq inviteCode }.singleOrNull()
                    ?: return@transaction null

                // Update guest with user info
                guestDao.apply {
                    this.userId = org.jetbrains.exposed.dao.id.EntityID(userUuid, ca.realitywargames.mysterybox.backend.models.Users)
                    this.status = GuestStatus.JOINED.name
                    this.joinedAt = Clock.System.now()
                }

                // Return the party
                PartyDAO.findById(guestDao.partyId)?.toParty()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    suspend fun addGuest(partyId: String, name: String, inviteCode: String): Guest? {
        return try {
            transaction {
                val partyUuid = UUID.fromString(partyId)
                GuestDAO.new {
                    this.partyId = org.jetbrains.exposed.dao.id.EntityID(partyUuid, Parties)
                    this.name = name
                    this.inviteCode = inviteCode
                    this.status = GuestStatus.INVITED.name
                }.toGuest()
            }
        } catch (e: IllegalArgumentException) {
            null // Invalid UUID format
        }
    }

    // Authorization helper methods

    suspend fun isHost(partyId: String, userId: String): Boolean {
        return try {
            val partyUuid = UUID.fromString(partyId)
            val userUuid = UUID.fromString(userId)
            transaction {
                val party = PartyDAO.findById(partyUuid) ?: return@transaction false
                party.hostId.value == userUuid
            }
        } catch (e: IllegalArgumentException) {
            false // Invalid UUID format
        }
    }

    suspend fun isUserAuthorized(partyId: String, userId: String): Boolean {
        return try {
            val partyUuid = UUID.fromString(partyId)
            val userUuid = UUID.fromString(userId)
            transaction {
                // Check if user is host
                val party = PartyDAO.findById(partyUuid) ?: return@transaction false
                if (party.hostId.value == userUuid) {
                    return@transaction true
                }

                // Check if user is a joined guest
                val isGuest = Guests.selectAll()
                    .where {
                        (Guests.partyId eq partyUuid) and
                        (Guests.userId eq userUuid) and
                        (Guests.status eq GuestStatus.JOINED.name)
                    }
                    .count() > 0

                isGuest
            }
        } catch (e: IllegalArgumentException) {
            false // Invalid UUID format
        }
    }
}

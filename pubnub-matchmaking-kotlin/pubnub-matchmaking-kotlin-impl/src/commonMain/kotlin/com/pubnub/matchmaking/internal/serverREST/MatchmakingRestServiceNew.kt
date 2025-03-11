package com.pubnub.matchmaking.internal.serverREST

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.asFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.internal.UserImpl
import com.pubnub.matchmaking.internal.serverREST.entities.MatchGroup
import com.pubnub.matchmaking.internal.serverREST.entities.MatchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

// this class represents server-side REST API
class MatchmakingRestServiceNew(
    private val matchmaking: Matchmaking,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    // Instead of a queue, we maintain a list of open match groups.
    private val openMatchGroups = mutableListOf<OpenMatchGroup>()
    private val groupsMutex = Mutex()

    @Throws(MatchMakingException::class)
    fun findMatch(userId: String): PNFuture<MatchResult> =
        if (!isValidId(userId)) {
            PubNubException("Id is required").asFuture()
        } else {
            PNFuture { callback ->
                scope.launch {
                    try {
                        // Validate user exists
                        val userMeta = getUserMetadata(userId)
                        val user = UserImpl.fromDTO(matchmaking = matchmaking, user = userMeta.data)
                        // Attempt to join an existing group or create a new one.
                        val result: MatchResult = findOrCreateMatchGroup(user)
                        callback.accept(Result.success(result))
                    } catch (e: Exception) {
                        callback.accept(Result.failure(e))
                    }
                }
            }
        }

    // Tries to find a compatible open group based on a simple Elo check.
    // If one is found and becomes full, a MatchResult is constructed and sent to all waiting callers.
    private suspend fun findOrCreateMatchGroup(user: User): MatchResult {
        // Create a channel for the current user’s match notification.
        val userChannel = Channel<MatchResult>(Channel.RENDEZVOUS)
        var groupToJoin: OpenMatchGroup?

        groupsMutex.withLock {
            // Find an open group where the user's skill is compatible.
            groupToJoin = openMatchGroups.firstOrNull { group ->
                isSkillCompatible(user, group) && group.users.size < group.requiredSize
            }
            if (groupToJoin != null) {
                // Join the found group.
                groupToJoin!!.users.add(user)
                groupToJoin!!.waitingChannels.add(userChannel)
                // When the group is full, prepare the final MatchGroup and notify all waiting channels.
                if (groupToJoin!!.users.size == groupToJoin!!.requiredSize) {
                    val finalGroup = MatchGroup(users = groupToJoin!!.users.toSet())
                    // Optionally update status for all group members.
                    finalGroup.users.forEach { groupUser ->
                        println("Found match for user: $groupUser")
                    }
                    // Create matchData. This can be extended as needed.
                    val matchData = mapOf(
                        "status" to "matchFound",
                        "groupSize" to groupToJoin!!.requiredSize.toString()
                    )
                    val matchResult = MatchResult(match = finalGroup, matchData = matchData)
                    // Notify every waiting channel.
                    groupToJoin!!.waitingChannels.forEach { channel ->
                        scope.launch {
                            channel.send(matchResult)
                        }
                    }
                    // Remove the group now that it is complete.
                    openMatchGroups.remove(groupToJoin)
                } else { // if must have both main and 'else' branches if used as an expression
                    Unit
                }
            } else {
                // No suitable group found; create a new one.
                groupToJoin = OpenMatchGroup(requiredSize = 2)
                groupToJoin!!.users.add(user)
                groupToJoin!!.waitingChannels.add(userChannel)
                openMatchGroups.add(groupToJoin!!)
            }
        }
        // Wait until the group becomes complete and a MatchResult is sent.
        return userChannel.receive()
    }

    // Simple compatibility check based on Elo difference.
    private fun isSkillCompatible(user: User, group: OpenMatchGroup): Boolean {
        // If the group is empty, any user is compatible.
        if (group.users.isEmpty()) {
            return true
        }
        val userElo = (user.custom?.get("elo") as? Int) ?: 0
        val groupAverageElo = group.users.map { (it.custom?.get("elo") as? Int ?: 0) }.average()
        // Example: user is compatible if the difference is 50 or less.
        return abs(userElo - groupAverageElo) <= 50 // todo get this value from Illuminate
    }

    private suspend fun getUserMetadata(userId: String): PNUUIDMetadataResult {
        val pnUuidMetadataResult: PNUUIDMetadataResult
        try {
            pnUuidMetadataResult = matchmaking.pubNub.getUUIDMetadata(uuid = userId, includeCustom = true).await()
        } catch (e: PubNubException) {
            if (e.statusCode == 404) {
                // Log.error
                println("User does not exist in AppContext")
                throw PubNubException("getUsersByIds: User does not exist")
            } else {
                throw PubNubException(e.message)
            }
        }
        return pnUuidMetadataResult
    }

    private fun isValidId(id: String): Boolean {
        return id.isNotEmpty()
    }

    private suspend fun <T> PNFuture<T>.await(): T =
        suspendCancellableCoroutine { cont ->
            async { result ->
                result.onSuccess {
                    cont.resume(it)
                }.onFailure {
                    cont.resumeWithException(it)
                }
            }
        }
}

class MatchMakingException : Exception() // todo implement

private data class OpenMatchGroup(
    val requiredSize: Int = 2, // For pairing, group size is 2 (can be configurable)
    val users: MutableList<User> = mutableListOf(),
    // Each waiting user gets a channel to receive the match result.
    val waitingChannels: MutableList<Channel<MatchResult>> = mutableListOf()
)

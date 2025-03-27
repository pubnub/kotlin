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
import com.pubnub.matchmaking.server.Constraints
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

    fun findMatch(userId: String, requiredMatchSize: Int = 2): PNFuture<MatchResult> =
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
                        val result: MatchResult = findOrCreateMatchGroup(user, requiredMatchSize)
                        callback.accept(Result.success(result))
                    } catch (e: Exception) {
                        callback.accept(Result.failure(e))
                    }
                }
            }
        }

    // Attempts to find a compatible group or creates a new one.
    private suspend fun findOrCreateMatchGroup(user: User, requiredSize: Int): MatchResult {
        // Create a channel for the current user’s match notification.
        val userChannel = Channel<MatchResult>(Channel.RENDEZVOUS)
        var groupToJoin: OpenMatchGroup?

        groupsMutex.withLock {
            // Look for an existing group with the same requiredSize that is compatible.
            groupToJoin = openMatchGroups.firstOrNull { group ->
                group.requiredSize == requiredSize && isSkillCompatible(user, group) && group.users.size < group.requiredSize
            }
            if (groupToJoin != null) {
                // Join the found group.
                groupToJoin!!.users.add(user)
                groupToJoin!!.waitingChannels.add(userChannel)
                // If the group is now full, create the final match result and notify all waiting users.
                if (groupToJoin!!.users.size == groupToJoin!!.requiredSize) {
                    val finalGroup = MatchGroup(users = groupToJoin!!.users.toSet())
                    finalGroup.users.forEach { groupUser ->
                        println("Found match for user: $groupUser")
                    }
                    val matchData = mapOf(
                        "status" to "matchFound",
                        "groupSize" to groupToJoin!!.requiredSize.toString()
                    )
                    val matchResult = MatchResult(match = finalGroup, matchData = matchData)
                    groupToJoin!!.waitingChannels.forEach { channel ->
                        scope.launch {
                            channel.send(matchResult)
                        }
                    }
                    openMatchGroups.remove(groupToJoin)
                } else { // 'if' must have both main and 'else' branches if used as an expression
                    Unit
                }
            } else {
                // No suitable group found; create a new one with the specified requiredSize.
                groupToJoin = OpenMatchGroup(requiredSize = requiredSize)
                groupToJoin!!.users.add(user)
                groupToJoin!!.waitingChannels.add(userChannel)
                openMatchGroups.add(groupToJoin!!)
            }
        }
        // Wait until a match result is received.
        return userChannel.receive()
    }

    // factors MAX_ELO_GAP, SKILL_GAP_WEIGHT, REGIONAL_PRIORITY
    private fun isSkillCompatible(user: User, group: OpenMatchGroup): Boolean {
        // If the group is empty, any user is compatible.
        if (group.users.isEmpty()) {
            return true
        }

        // Retrieve configuration constraints.
        val constraints = Constraints.getConstraints()
        val maxEloGap = constraints["MAX_ELO_GAP"] as? Int ?: 100
        val skillGapWeight = constraints["SKILL_GAP_WEIGHT"] as? Double ?: 1.0
        val regionalPriority = constraints["REGIONAL_PRIORITY"] as? Double ?: 10.0

        // Retrieve additional dynamic parameters for threshold calculation.
        val baseThreshold = constraints["BASE_THRESHOLD"] as? Double ?: 20.0
        val thresholdIncrement = constraints["THRESHOLD_INCREMENT"] as? Double ?: 5.0

        // Get the new user's Elo.
        val userElo = (user.custom?.get("elo") as? Int) ?: 0

        // Compute the group's average Elo.
        val groupElos: List<Int> = group.users.map { (it.custom?.get("elo") as? Int ?: 0) }
        val groupAverageElo = groupElos.average()

        // Automatically reject if the difference exceeds the maximum allowed gap.
        if (abs(userElo - groupAverageElo) > maxEloGap) {
            return false
        }

        // Calculate the weighted Elo difference.
        val weightedDifference = skillGapWeight * abs(userElo - groupAverageElo)

        // Determine the region for the new user.
        val userRegion = (user.custom?.get("server") as? String) ?: "global"
        // Calculate the majority region in the group.
        val regionCounts = group.users.groupingBy { (it.custom?.get("server") as? String ?: "global") }.eachCount()
        val majorityRegion = regionCounts.maxByOrNull { it.value }?.key ?: "global"
        val regionMismatchPenalty = if (userRegion == majorityRegion){
            0.0
        } else {
            regionalPriority
        }

        // Overall compatibility score.
        val compatibilityScore = weightedDifference + regionMismatchPenalty

        // Calculate a dynamic threshold that scales with the number of users already in the group.
        // For example, for a 2-player match the threshold might be baseThreshold,
        // and for each additional user the tolerance increases by thresholdIncrement.
        val dynamicThreshold = baseThreshold + (group.users.size - 1) * thresholdIncrement

        // todo consider
        // val waitTime = System.currentTimeMillis() - group.creationTime
        // val timeFactor = waitTime / SOME_TIME_UNIT  // e.g., seconds or minutes
        // val dynamicThreshold = baseThreshold + (group.users.size - 1) * thresholdIncrement + timeFactor

        return compatibilityScore <= dynamicThreshold
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

// todo ad creation time, as time passes the algorithm an gradually incrase the acceptable difference in skill or latency
private data class OpenMatchGroup(
    val requiredSize: Int = 2, // For pairing, group size is 2 (can be configurable)
    val users: MutableList<User> = mutableListOf(),
    // Each waiting user gets a channel to receive the match result.
    val waitingChannels: MutableList<Channel<MatchResult>> = mutableListOf()
)

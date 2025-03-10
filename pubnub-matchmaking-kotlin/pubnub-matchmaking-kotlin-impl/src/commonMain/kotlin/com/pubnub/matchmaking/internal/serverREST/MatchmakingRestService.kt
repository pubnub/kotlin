package com.pubnub.matchmaking.server

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.asFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.entities.MatchmakingStatus
import com.pubnub.matchmaking.internal.USER_STATUS_CHANNEL_PREFIX
import com.pubnub.matchmaking.internal.UserImpl
import com.pubnub.matchmaking.internal.serverREST.entities.MatchGroup
import com.pubnub.matchmaking.internal.serverREST.entities.MatchmakingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

// this class represents server-side REST API
class MatchmakingRestService(
    private val matchmaking: Matchmaking,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    // Use a mutable map guarded by a Mutex for thread safety
    private val matchmakingQueue = mutableMapOf<String, MatchmakingStatus>()
    private val queueMutex = Mutex()
    private var processingQueueInProgress = false

    // todo get Set<UserId> instead of userId
    internal fun findMatch(userId: String): PNFuture<String> =
        if (isValidId(userId)) {
            PubNubException("Id is required").asFuture()
        } else {
            PNFuture { callback ->
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        // check if user exists, if not then throw
                        getUserMetadata(userId)
                        val result = findMatchInternal(userId)
                        callback.accept(Result.success(result))
                    } catch (e: Exception) {
                        callback.accept(Result.failure(e))
                    }
                }
            }
        }

    // Registers a user for matchmaking.
    private suspend fun findMatchInternal(userId: String): String {
        queueMutex.withLock {
            if (matchmakingQueue.containsKey(userId)) {
                return "User already registered for matchmaking. Duplication not permitted."
            }
            matchmakingQueue[userId] = MatchmakingStatus.IN_QUEUE
            // stworz kanał zawierający nazwę usera i wyślij do niego wiadomość ze statusem
            // w SDK będzie metoda getStatus, która będzie odczytywała ostatnią wiadomość z tego kanału
            // w SDK będzie metoda getStatus(callback), która będzie się subskrybowała na ten kanał i zwracała aktualny status

            // matchmaking status for user will be stored in channel
            matchmaking.pubNub.publish(
                channel = USER_STATUS_CHANNEL_PREFIX + userId,
                message = MatchmakingStatus.IN_QUEUE
            ).await()
        }
        ensureMatchmakingIsRunning()
        return "accepted"
    }

    // Starts a coroutine loop to process the matchmaking queue when enough users are available.
    private suspend fun ensureMatchmakingIsRunning() {
        queueMutex.withLock {
            if (matchmakingQueue.size >= 2 && !processingQueueInProgress) { // todo 2 should depend on number of users in match
                processingQueueInProgress = true
                scope.launch {
                    try {
                        while (true) {
                            processMatchmakingQueue()
                            delay(5000L) // Wait 5 seconds between processing
                        }
                    } finally {
                        processingQueueInProgress = false
                    }
                }
            }
        }
    }

    // Processes the matchmaking queue by removing all user IDs for pairing.
    private suspend fun processMatchmakingQueue() {
        val userIds: Set<String> = queueMutex.withLock {
            if (matchmakingQueue.size < 2) {
                return
            }
            val ids = matchmakingQueue.keys.toSet()
            matchmakingQueue.keys.removeAll(ids)
            ids
        }
        if (userIds.isNotEmpty()) {
            performMatchmaking(userIds)
        }
        // If not enough users remain, stop processing.
        queueMutex.withLock {
            if (matchmakingQueue.size < 2) {
                processingQueueInProgress = false
            }
        }
    }

    // Executes matchmaking on the given set of user IDs.
    private suspend fun performMatchmaking(userIds: Set<String>) {
        val users = getUsersByIds(userIds)
        val matchmakingResult: MatchmakingResult = pairUsersBySkill(users.toList())
        notifyAboutSuccessfulMatch(matchmakingResult.matchGroups)
        addUnmatchedUsersIdsBackToQueue(matchmakingResult.unmatchedUserIds)
    }

    private suspend fun getUsersByIds(userIds: Set<String>): Set<User> {
        val users = mutableSetOf<User>()
        userIds.forEach { userId ->
            val pnUuidMetadataResult: PNUUIDMetadataResult = getUserMetadata(userId)
            val user = UserImpl.fromDTO(matchmaking = matchmaking, user = pnUuidMetadataResult.data)
            users.add(user)
        }
        return users
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

    private suspend fun addUnmatchedUsersIdsBackToQueue(unmatchedUserIds: Set<String>) {
        if (unmatchedUserIds.isEmpty()) {
            println("No unmatched users to re-add to matchmaking queue")
            return
        }
        println("Adding ${unmatchedUserIds.size} unmatched users back to matchmaking queue.")
        queueMutex.withLock {
            unmatchedUserIds.forEach { userId ->
                if (!matchmakingQueue.containsKey(userId)) {
                    matchmakingQueue[userId] = MatchmakingStatus.RE_ADDED_TO_QUEUE
                    println("- Added $userId back to matchmaking queue.")
                }
            }
        }
    }

    private suspend fun notifyAboutSuccessfulMatch(matchGroups: Set<MatchGroup>) {
        matchGroups.forEach { group ->
            group.users.forEach { user ->
                notifyUserAboutSuccessfulMatch(user, group)
            }
        }
    }

    private suspend fun notifyUserAboutSuccessfulMatch(user: User, group: MatchGroup) {
        println("Found match for userId: ${user.id} in group: ${group.users.map { it.id }}")
        matchmaking.pubNub.publish(
            channel = USER_STATUS_CHANNEL_PREFIX + user.id,
            message = MatchmakingStatus.MATCH_FOUND
        ).await()
    }

    private fun calculateScore(userA: User, userB: User): Double {
        val constraints = Constraints.getConstraints()
        // todo those 3 values will be taken from Illuminate
        val maxEloGap = constraints["MAX_ELO_GAP"] as Int
        val skillGapWeight = constraints["SKILL_GAP_WEIGHT"] as Double
        val regionalPriority = constraints["REGIONAL_PRIORITY"] as Double

        val eloA = (userA.custom?.get("elo") as? Int) ?: 0
        val eloB = (userB.custom?.get("elo") as? Int) ?: 0
        val regionA = (userA.custom?.get("server") as? String) ?: "global"
        val regionB = (userB.custom?.get("server") as? String) ?: "global"

        val eloDifference = abs(eloA - eloB)
        if (eloDifference > maxEloGap) {
            return Double.POSITIVE_INFINITY
        }

        val regionMismatchPenalty = if (regionA == regionB) {
            0.0
        } else {
            regionalPriority
        }

        return skillGapWeight * eloDifference + regionMismatchPenalty
    }

    // Create all possible pairs (only including allowed pairs)
    private fun createAllPairs(users: List<User>): List<Triple<Int, Int, Double>> {
        val pairs = mutableListOf<Triple<Int, Int, Double>>()
        for (i in users.indices) {
            for (j in i + 1 until users.size) {
                val score = calculateScore(users[i], users[j])
                if (score != Double.POSITIVE_INFINITY) {
                    pairs.add(Triple(i, j, score))
                }
            }
        }
        return pairs
    }

    // Greedy pairing: sort by score and select pairs without conflicts.
// Instead of returning MatchmakingPair, we create a MatchGroup that holds a set of users.
    private fun pairUsersBySkill(users: List<User>): MatchmakingResult {
        val allPairs = createAllPairs(users).sortedBy { it.third }
        val pairedIndices = mutableSetOf<Int>()
        val groups = mutableSetOf<MatchGroup>()

        for ((i, j, _) in allPairs) {
            if (i !in pairedIndices && j !in pairedIndices) {
                groups.add(
                    MatchGroup(
                        setOf(
                            users[i],
                            users[j]
                        )
                    )
                ) // todo here we are creating group  that consist of 2 player. Make it flexible to be able to return group of N players
                pairedIndices.add(i)
                pairedIndices.add(j)
            }
        }
        val unpaired = users.indices.filter { it !in pairedIndices }
            .map { users[it].id }.toSet()
        return MatchmakingResult(groups, unpaired)
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

object Constraints {
    fun getConstraints(): Map<String, Any> = mapOf(
        "MAX_ELO_GAP" to 100,
        "SKILL_GAP_WEIGHT" to 1.0,
        "REGIONAL_PRIORITY" to 10.0
    )
}

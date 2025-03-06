package com.pubnub.matchmaking.server

import com.pubnub.api.PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.entities.MatchmakingCallback
import com.pubnub.matchmaking.entities.MatchmakingStatus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

// this class represents server-side REST API
class MatchmakingRestService(val pubNub: PubNub) {
    private val matchmakingQueue = ConcurrentHashMap<String, MatchmakingStatus>(0);
    private val processingQueueInProgress = AtomicBoolean(false)
    private val executor: ExecutorService = Executors.newFixedThreadPool(3)

    fun findMatch(userId: String): PNFuture<String> {
        // check if user exist in AppContext
        pubNub.getUUIDMetadata(uuid = null).sync() // 404 exception is thrown when user not found

        // exit if user already registered for matchmaking
        if (matchmakingQueue.containsKey(userId)) {
            setMatchmakingStatusForUser(userId, MatchmakingStatus.ALREADY_IN_QUEUE)
            return "User already registered for matchmaking. Duplication not permitted."
        }

        // add userId to the queue and notify status
        matchmakingQueue.putIfAbsent(userId, MatchmakingStatus.IN_QUEUE) //todo do we need status here?
        setMatchmakingStatusForUser(userId, MatchmakingStatus.IN_QUEUE)

        // start matchmaking loop if not working
        makeSureMatchmakingIsRunning()

        return "user added to matchmaking queue" // todo change value?
    }

    private fun makeSureMatchmakingIsRunning() {
        if (matchmakingQueue.size >= 2 && processingQueueInProgress.compareAndSet(false, true)) { // todo replace 2 with number of players in a match
            executor.submit {
                try {
                    while (true) {
                        try {
                            processMatchmakingQueue()
                            Thread.sleep(5000) // Check every 5 seconds todo adjust ?
                        } catch (e: InterruptedException) {
                            println("Exception occurred processing matchmaking queue.")
                            Thread.currentThread().interrupt() // Restore interrupted status
                            break
                        }
                    }
                } finally {
                    processingQueueInProgress.set(false)
                }
            }
        }
    }

    private fun processMatchmakingQueue() { // todo modify using my matchmaking logic
        println("Number of players in queue: " + matchmakingQueue.size)
        if (matchmakingQueue.size < 2) { // todo this should be configurable based on requirement 2,5,10, other users in game/healthcare
            // todo what about sending notification that there is not enough user in a queue?
            return
        }

        // get all users id's from the queue and remove them in thread-save manner
        val userIds: Set<String> = HashSet(matchmakingQueue.keys)
        matchmakingQueue.keys.removeAll(userIds)
        notifyAboutMatchmakingStarted(userIds)

        performMatchMaking(userIds)

        //finish processingMatchmakingQueue because there is not enough users to match
        val numberOfUserInQueue = matchmakingQueue.size
        if (numberOfUserInQueue < 2) { // todo instead of 2 there should be variable like minPlayersForMatch that defies min players/user in a match
            println("Stopping matchmaking because there is not enough users in a queue: $numberOfUserInQueue")
            processingQueueInProgress.set(false)
            //todo do we want to send notification that for remaining users there is not sufficient number of users to create a match?
        }
    }

    private fun performMatchMaking(userIds: Set<String>) {
        val users: Set<User> = getUsersByIds(userIds)
        val matchmakingResult: MatchmakingResult = matchmakingService.pairUsersBySkill(ArrayList<Any?>(users))
        notifyAboutSuccessfulMatch(matchmakingResult.pairs)
        addUnmatchedUsersIdsBackToMatchmakingQueueAndNotify(matchmakingResult.unmatchedUserIds)
    }

    private fun addUnmatchedUsersIdsBackToMatchmakingQueueAndNotify(unmatchedUserIds: List<String>) {
        if (unmatchedUserIds.isEmpty()) {
            println("No unmatched users to re-add to matchmaking queue")
            return
        }

        println("Adding $unmatchedUserIds unmatched users back to matchmaking queue.")
        for (unmatchedUserId in unmatchedUserIds) {
            if (matchmakingQueue.putIfAbsent(unmatchedUserId, MatchmakingStatus.RE_ADDED_TO_QUEUE) == null) {
                val callback: MatchmakingCallback = callbacks.get(unmatchedUserId)
                if (callback != null) {
                    // Asynchronously notify the user of the status change
                    executeCallback(java.lang.Runnable { callback.onStatusChange(MatchmakingStatus.RE_ADDED_TO_QUEUE) })
                }
            }
        }
    }


    // todo how long user can stay in queue waiting for match?
    private fun notifyAboutSuccessfulMatch(matchmakingPairs: List<MatchmakingPair>) {
        for (pair in matchmakingPairs) {
            val user1: User = pair.getUser1()
            val user2: User = pair.getUser2()
            notifyUserAboutSuccessfulMatch(user1, pair)
            notifyUserAboutSuccessfulMatch(user2, pair)
        }
    }

    private fun notifyUserAboutSuccessfulMatch(user: User?, pair: MatchmakingPair?) {
        if (user == null || pair == null) {
            println("User or MatchmakingGroup is null.")
        }
        val userId: String = user.getId()
        matchmakingQueue.put(userId, MatchmakingStatus.MATCH_FOUND)
        val callback: MatchmakingCallback = callbacks.remove(userId)
        if (callback != null) {
            // Notify on a separate thread to avoid blocking the matchmaking loop
            executeCallback(java.lang.Runnable { callback.onMatchFound(pair) })
        }
    }

    private fun notifyAboutMatchmakingStarted(userIds: Set<String>) {
        for (userId in userIds) {
            setMatchmakingStatusForUser(userId, MatchmakingStatus.MATCHMAKING_STARTED)
        }
    }

    private fun setMatchmakingStatusForUser(userId: String, status: MatchmakingStatus) {
        val matchmakingStatusChannelForUser = "MATCHMAKING_STATUS.$userId"
        pubNub.publish(channel = matchmakingStatusChannelForUser, message = status).sync()
    }
}
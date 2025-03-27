package com.pubnub.integration

import com.pubnub.matchmaking.entities.FindMatchResult
import com.pubnub.matchmaking.entities.MatchmakingStatus
import com.pubnub.test.await
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class MatchmakingTest : BaseMatchmakingIntegrationTest() {


    @Test // todo make it working for JS and Swift
    fun can_get_matchmaking_status_synchronously() = runTest {
        val userId = user1.id
        val user = matchmaking.createUser(id = userId, name = "userName-$userId").await()
        assertEquals(userId, user.id)

        val immediateResponse: String = matchmaking.findMatch(userId).await()
        assertEquals("accepted", immediateResponse)

        val status: MatchmakingStatus = matchmaking.getStatus(userId).await()
        assertEquals(MatchmakingStatus.IN_QUEUE, status)
    }

    @Test
    fun can_get_matchmaking_status_using_callback() = runTest(timeout = 10.seconds) {
        val userId = user1.id
        val callbackCompleted = CompletableDeferred<MatchmakingStatus>()

        val user = matchmaking.createUser(id = userId, name = "userName-$userId").await()

        val findMatch: FindMatchResult =
            matchmaking.findMatch(userId = userId) { matchmakingStatus: MatchmakingStatus ->
                callbackCompleted.complete(matchmakingStatus)
            }.await()

        assertEquals("accepted", findMatch.result)
        assertEquals(MatchmakingStatus.IN_QUEUE, callbackCompleted.await())

        findMatch.disconnect?.close()
    }

}


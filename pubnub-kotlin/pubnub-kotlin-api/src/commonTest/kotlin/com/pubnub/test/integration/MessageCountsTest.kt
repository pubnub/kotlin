package com.pubnub.test.integration

import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class MessageCountsTest : BaseIntegrationTest() {
    private val channel = randomString()

    @Test
    fun messageCounts() = runTest {
        val expectedMeta = mapOf(randomString() to randomString())
        val expectedMessage = randomString()
        val expectedCount = 3L
        val otherChannel = randomString()
        val timetokens = (0 until expectedCount).map {
            pubnub.publish(
                channel = channel,
                message = expectedMessage,
                meta = expectedMeta,
                shouldStore = true,
                ttl = 60,
            ).await().timetoken
        }
        val timetokensOther = (0 until expectedCount).map {
            pubnub.publish(
                channel = otherChannel,
                message = expectedMessage,
                meta = expectedMeta,
                shouldStore = true,
                ttl = 60,
            ).await().timetoken
        }

        withContext(Dispatchers.Default) {
            delay(2.seconds)
        }
        val counts = pubnub.messageCounts(
            listOf(channel, otherChannel),
            listOf(timetokens.first() - 1, timetokensOther.first() - 1)
        ).await()

        assertEquals(expectedCount, counts.channels[channel])
        assertEquals(expectedCount, counts.channels[otherChannel])
    }
}

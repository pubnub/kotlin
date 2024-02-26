package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.asyncRetry
import com.pubnub.api.await
import com.pubnub.api.v2.callbacks.getOrThrow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class StorageAndPlaybackIntegrationTests : BaseIntegrationTest() {
    @Test
    fun testHistoryMessages() {
        val expectedMessage = randomValue()
        val expectedChannel = randomChannel()

        pubnub.publish(
            channel = expectedChannel,
            message = expectedMessage,
        ).await { result -> }

        pubnub.history(
            channel = expectedChannel,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            assertEquals(expectedMessage, result.getOrThrow().messages[0].entry.asString)
        }
    }

    @Test
    fun testHistoryMessagesWithTimeToken() {
        val expectedChannel = randomChannel()

        repeat(3) {
            pubnub.publish(
                channel = expectedChannel,
                message = randomValue(),
            ).sync()
        }

        pubnub.history(
            channel = expectedChannel,
            includeTimetoken = true,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.getOrThrow().messages.forEach {
                assertNotNull(it.timetoken)
            }
        }
    }

    @Test
    fun testLoadingHistoryMessagesWithLimit() {
        val expectedChannel = randomChannel()

        repeat(20) {
            pubnub.publish(
                channel = expectedChannel,
                message = randomValue(),
            ).sync()
        }

        pubnub.history(
            channel = expectedChannel,
            count = 10,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            assertEquals(10, result.getOrThrow().messages.size)
        }
    }

    @Test
    fun testLoadingHistoryWithSpecificTimeInterval() {
        val expectedChannel = randomChannel()

        val before = System.currentTimeMillis() * 10000
        wait(5)
        repeat(3) {
            pubnub.publish(
                channel = expectedChannel,
                message = randomValue(),
            ).sync()
        }
        wait(5)
        val now = System.currentTimeMillis() * 10000

        pubnub.history(
            channel = expectedChannel,
            includeTimetoken = true,
            start = now,
            end = before,
            count = 10,
        ).await { result ->
            assertFalse(result.isFailure)
            assertEquals(3, result.getOrThrow().messages.size)
        }
    }

    @Test
    fun testReverseHistoryPaging() {
        val expectedChannel: String = randomValue()
        val message1: String = randomValue(20)
        val message2: String = randomValue(20)

        pubnub.publish(
            channel = expectedChannel,
            message = message1,
        ).sync()

        pubnub.publish(
            channel = expectedChannel,
            message = message2,
        ).sync()

        pubnub.history(
            channel = expectedChannel,
            count = 10,
            reverse = true,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            assertEquals(message1, result.getOrThrow().messages[0].entry.asString)
            assertEquals(message2, result.getOrThrow().messages[1].entry.asString)
        }
    }
}

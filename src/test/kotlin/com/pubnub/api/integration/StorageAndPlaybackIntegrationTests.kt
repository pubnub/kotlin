package com.pubnub.api.integration

import com.pubnub.api.asyncRetry
import com.pubnub.api.await
import com.pubnub.api.randomChannel
import com.pubnub.api.randomValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class StorageAndPlaybackIntegrationTests : BaseIntegrationTest() {

    @Test
    fun testHistoryMessages() {
        val expectedMessage = randomValue()
        val expectedChannel = randomChannel()

        pubnub.publish().apply {
            channel = expectedChannel
            message = expectedMessage
        }.await { _, _ -> }

        pubnub.history().apply {
            this.channel = expectedChannel
        }.asyncRetry { result, status ->
            assertFalse(status.error)
            assertEquals(expectedMessage, result!!.messages[0].entry.asString)
        }
    }

    @Test
    fun testHistoryMessagesWithTimeToken() {
        val expectedChannel = randomChannel()

        repeat(3) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
            }.sync()!!
        }

        pubnub.history().apply {
            channel = expectedChannel
            includeTimetoken = true
        }.asyncRetry { result, status ->
            assertFalse(status.error)
            result!!.messages.forEach {
                assertNotNull(it.timetoken)
            }
        }
    }

    @Test
    fun testLoadingHistoryMessagesWithLimit() {
        val expectedChannel = randomChannel()

        repeat(20) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
            }.sync()!!
        }

        pubnub.history().apply {
            channel = expectedChannel
            count = 10
        }.asyncRetry { result, status ->
            assertFalse(status.error)
            assertEquals(10, result!!.messages.size)
        }
    }

    @Test
    fun testLoadingHistoryWithSpecificTimeInterval() {
        val expectedChannel = randomChannel()

        val before = System.currentTimeMillis() * 10000
        wait(5)
        repeat(3) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
            }.sync()!!
        }
        wait(5)
        val now = System.currentTimeMillis() * 10000

        pubnub.history().apply {
            channel = expectedChannel
            includeTimetoken = true
            start = now
            end = before
            count = 10
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(3, result!!.messages.size)
        }
    }

    @Test
    fun testReverseHistoryPaging() {
        val expectedChannel: String = randomValue()
        val message1: String = randomValue(20)
        val message2: String = randomValue(20)

        pubnub.publish().apply {
            channel = expectedChannel
            message = message1
        }.sync()!!

        pubnub.publish().apply {
            channel = expectedChannel
            message = message2
        }.sync()!!

        pubnub.history().apply {
            channel = expectedChannel
            count = 10
            reverse = true
        }.asyncRetry { result, status ->
            assertFalse(status.error)
            assertEquals(message1, result!!.messages[0].entry.asString)
            assertEquals(message2, result.messages[1].entry.asString)
        }
    }
}

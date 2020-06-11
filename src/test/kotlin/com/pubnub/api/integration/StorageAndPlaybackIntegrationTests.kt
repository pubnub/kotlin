package com.pubnub.api.integration

import com.pubnub.api.suite.await
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StorageAndPlaybackIntegrationTests : BaseIntegrationTest() {

    @Test
    fun testHistoryMessages() {
        val expectedMessage = randomValue()
        val expectedChannel = randomValue()

        pubnub.publish().apply {
            channel = expectedChannel
            message = expectedMessage
        }.await { _, _ -> }

        wait()

        pubnub.history().apply {
            this.channel = expectedChannel
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(expectedMessage, result!!.messages[0].entry.asString)
        }
    }

    @Test
    fun testHistoryMessagesWithTimeToken() {
        val expectedChannel = randomValue()

        repeat(3) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
            }.sync()!!
        }

        wait()

        pubnub.history().apply {
            channel = expectedChannel
            includeTimetoken = true
        }.await { result, status ->
            assertFalse(status.error)
            result!!.messages.forEach {
                assertNotNull(it.timetoken)
            }
        }
    }

    @Test
    fun testLoadingHistoryMessagesWithLimit() {
        val expectedChannel = randomValue()

        repeat(20) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
            }.sync()!!
        }

        wait()

        pubnub.history().apply {
            channel = expectedChannel
            count = 10
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(10, result!!.messages.size)
        }
    }

    @Test
    fun testLoadingHistoryWithSpecificTimeInterval() {
        val expectedChannel = randomValue()

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

        wait()

        pubnub.history().apply {
            channel = expectedChannel
            count = 10
            reverse = true
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(message1, result!!.messages[0].entry.asString)
            assertEquals(message2, result.messages[1].entry.asString)
        }
    }

}
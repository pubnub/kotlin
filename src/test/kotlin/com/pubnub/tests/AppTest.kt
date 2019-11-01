package com.pubnub.tests

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AppTest {

    lateinit var pubnub: PubNub

    @BeforeEach
    fun initPubnub() {
        pubnub = PubNub(
            PNConfiguration().apply {
                subscribeKey = "demo-36"
                publishKey = "demo-36"
                logVerbosity = PNLogVerbosity.BODY
            }
        )
    }

    @Test
    fun testPublish() {
        pubnub.publish {
            channel = UUID.randomUUID().toString()
            message = UUID.randomUUID().toString()
        }.sync().let {
            Assertions.assertNotNull(it)
        }
    }

    @Test
    fun testPublishWithHistory() {
        val expectedChannel = UUID.randomUUID().toString()
        val expectedCount = 3
        val expectedMessages = generateSequence { UUID.randomUUID().toString() }.take(expectedCount).toList()

        expectedMessages.forEach {
            pubnub.publish {
                channel = expectedChannel
                message = it
            }.sync().let {
                assertNotNull(it)
            }
        }

        Thread.sleep(1000)

        pubnub.history {
            channel = expectedChannel
        }.sync().let { result ->
            assertNotNull(result)
            assertEquals(expectedCount, result!!.messages.size)

            assertEquals(result.messages.map { it.entry.asString }, expectedMessages)

            result.messages.forEach {
                expectedMessages.contains(it.entry.asString)
            }
        }

    }
}
package com.pubnub.api

import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class PublishTest : BaseIntegrationTest() {
    private val channel = "myChannel"

    @Test
    fun can_publish_message() =
        runTest(timeout = 10.seconds) {
            val result = pubnub.publish(channel, "some message").await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    fun can_receive_message_with_object_metadata() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            pubnub.publish(channel, "some message", createCustomObject(mapOf("aa" to "bb"))).await()
            val result = nextMessage()
            assertEquals("some message", result.message.asString())
            assertEquals(mapOf("aa" to "bb"), result.userMetadata?.asMap()?.mapValues { it.value.asString() })
        }
    }

    @Test
    fun can_receive_message_with_primitive_metadata() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            pubnub.publish(channel, "some message", "some meta").await()
            val result = nextMessage()
            assertEquals("some message", result.message.asString())
            assertEquals("some meta", result.userMetadata?.asString())

            pubnub.publish(channel, "some message", 123).await()
            val result2 = nextMessage()
            assertEquals("some message", result2.message.asString())
            assertEquals("123", result2.userMetadata?.asString())
        }
    }
}

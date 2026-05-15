package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.CommonUtils.retry
import com.pubnub.test.await
import com.pubnub.test.listen
import com.pubnub.test.subscribeToBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

// make sure feature is enabled on keyset publish_v2_enabled=1 publish_v2_max_size=2097152
@Ignore("Large message (V2 publish) integration tests. Remove @Ignore to run on demand.")
class LargeMessagePublishIntegrationTests : BaseIntegrationTest() {
    @Test
    fun testPublishLargeMessageOverV1Limit_Post() {
        val expectedChannel = randomChannel()
        // Create a message larger than V1 POST limit (32,768 bytes)
        // Account for JSON quotes (2 bytes), so 40KB string ensures we exceed the limit
        val largeMessage = "x".repeat(40_000)

        pubnub.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).await { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublishLargeMessageOverV1Limit_Get() {
        val expectedChannel = randomChannel()
        // Create a message larger than V1 GET path limit (32,752 bytes)
        val largeMessage = "x".repeat(40_000)

        // Even with usePost=false, large messages should be routed to V2 POST
        server.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = false,
        ).await { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublishLargeMessage_History() {
        val expectedChannel = randomChannel()
        val largeMessage = "x".repeat(50_000)

        pubnub.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).sync()

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannel),
                page = PNBoundedPage(limit = 1),
            ).sync().run {
                assertEquals(1, channels.size)
                assertEquals(1, channels[expectedChannel]!!.size)
                assertEquals(largeMessage, channels[expectedChannel]!![0].message.asString)
            }
        }
    }

    @Test
    fun testPublishLargeMessage_Receive() {
        val expectedChannel = randomChannel()
        val largeMessage = "x".repeat(50_000)
        val success = AtomicBoolean()

        val observer = createPubNub {}

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                }

                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    if (event.message.asString == largeMessage) {
                        success.set(true)
                    }
                }
            },
        )

        pubnub.subscribeToBlocking(expectedChannel)

        observer.publish(
            message = largeMessage,
            channel = expectedChannel,
            usePost = true,
        ).sync()

        success.listen()
    }

    @Test
    fun testPublishLargeJsonObject_History() {
        val expectedChannel = randomChannel()
        // Create a large JSON object (> 32KB)
        val largePayload = JSONObject().apply {
            put("id", randomValue())
            put("data", "x".repeat(40_000))
            put("timestamp", System.currentTimeMillis())
        }

        pubnub.publish(
            channel = expectedChannel,
            message = largePayload,
            usePost = true,
        ).sync()

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannel),
                page = PNBoundedPage(limit = 1),
            ).sync().run {
                assertEquals(1, channels.size)
                assertEquals(1, channels[expectedChannel]!!.size)
                val receivedMessage = JSONObject(channels[expectedChannel]!![0].message.toString())
                assertEquals(largePayload.getString("id"), receivedMessage.getString("id"))
                assertEquals(largePayload.getString("data"), receivedMessage.getString("data"))
            }
        }
    }

    @Test
    fun testPublish100KBMessage() {
        val expectedChannel = randomChannel()
        val largeMessage = "x".repeat(100_000)

        pubnub.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).await { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublish500KBMessage() {
        val expectedChannel = randomChannel()
        val largeMessage = "x".repeat(500_000)

        server.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).await { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublish2MBMessage() {
        val expectedChannel = randomChannel()
        // We can't send exactly 2MB of raw content because the payload grows due to
        // JSON serialization overhead and encryption (if configured).
        val largeMessage = "x".repeat((1.9 * 1024 * 1024).toInt()) // 1,992,294 chars

        pubnub.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).await(seconds = 10) { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublish2MBMessageWithCrypto() {
        val expectedChannel = randomChannel()
        val pubnubWithCrypto =
            createPubNub {
                cryptoModule = CryptoModule.createAesCbcCryptoModule("test", false)
            }

        // We can't send exactly 2MB of raw content because the payload grows due to
        // JSON serialization overhead and encryption (if configured).
        val largeMessage = "x".repeat((1.4 * 1024 * 1024).toInt())

        pubnubWithCrypto.publish(
            channel = expectedChannel,
            message = largeMessage,
            usePost = true,
        ).await(seconds = 15) { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublishOversizedMessage_ShouldFail() {
        val expectedChannel = randomChannel()
        // Create a message > 2MB which should be rejected by client side validation
        val oversizedMessage = "x".repeat(2_500_000)

        pubnub.publish(
            channel = expectedChannel,
            message = oversizedMessage,
            usePost = true,
        ).await { result ->
            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is PubNubException)
            assertEquals(PubNubError.MESSAGE_TOO_LARGE, (exception as PubNubException).pubnubError)
            assertEquals(PubNubError.MESSAGE_TOO_LARGE.message, exception.errorMessage)
        }
    }
}

package com.pubnub.api.integration

import com.google.gson.Gson
import com.pubnub.api.CommonUtils
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.SpaceId
import com.pubnub.api.await
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class SignalIntegrationTests : BaseIntegrationTest() {

    lateinit var expectedChannel: String
    lateinit var expectedPayload: String

    override fun onBefore() {
        expectedChannel = randomChannel()
        expectedPayload = randomValue(5)
    }

    @Test
    fun testPublishSignalMessageAsync() {
        pubnub.signal(
            message = expectedPayload,
            channel = expectedChannel
        ).await { result, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNSignalOperation, status.operation)
            assertEquals(status.uuid, pubnub.configuration.userId.value)
            assertNotNull(result)
        }
    }

    @Test
    fun testPublishSignalMessageSync() {
        pubnub.signal(
            message = expectedPayload,
            channel = expectedChannel
        ).sync()!!
    }

    @Test
    fun testReceiveSignalMessage() {
        val success = AtomicBoolean()

        val observerClient = createPubNub()

        observerClient.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    pubnub.signal(
                        message = expectedPayload,
                        channel = expectedChannel
                    ).async { result, status ->
                        assertFalse(status.error)
                        assertEquals(PNOperationType.PNSignalOperation, status.operation)
                        assertEquals(status.uuid, pubnub.configuration.userId.value)
                        assertNotNull(result)
                    }
                }
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                assertEquals(pubnub.configuration.userId.value, pnSignalResult.publisher)
                assertEquals(expectedChannel, pnSignalResult.channel)
                assertEquals(expectedPayload, Gson().fromJson(pnSignalResult.message, String::class.java))
                success.set(true)
            }
        })

        observerClient.subscribe(
            channels = listOf(expectedChannel)
        )

        success.listen()
    }

    @Test
    fun testPublishSignalMessageSyncWithoutSubKey() {
        try {
            pubnub.configuration.subscribeKey = ""
            pubnub.signal(
                channel = randomChannel(),
                message = randomValue()
            ).sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testSignalWithSpaceIdAndMessageType() {
        val expectedChannel = randomChannel()

        val messageFuture = CompletableFuture<PNSignalResult>()
        val connected = CountDownLatch(1)
        val expectedSpaceId = SpaceId("thisIsSpace")
        val expectedMessageType = MessageType("thisIsMessageType")

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                println(pnStatus)
                connected.countDown()
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                messageFuture.complete(pnSignalResult)
            }
        })

        pubnub.subscribe(channels = listOf(expectedChannel))

        Assert.assertTrue(connected.await(1_000, TimeUnit.MILLISECONDS))

        pubnub.signal(
            channel = expectedChannel,
            message = CommonUtils.generatePayload(),
            spaceId = expectedSpaceId,
            messageType = expectedMessageType
        ).await { _, status ->
            assertFalse(status.error)
            assertEquals(status.uuid, pubnub.configuration.userId.value)
        }

        val message = messageFuture.get(1_000, TimeUnit.MILLISECONDS)

        MatcherAssert.assertThat(message.spaceId, Is.`is`(expectedSpaceId))
        MatcherAssert.assertThat(message.messageType, Is.`is`(expectedMessageType))
    }
}

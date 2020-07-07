package com.pubnub.api.integration

import com.google.gson.Gson
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.assertPnException
import com.pubnub.api.await
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.randomChannel
import com.pubnub.api.randomValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
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
        pubnub.signal().apply {
            message = expectedPayload
            channel = expectedChannel
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNSignalOperation, status.operation)
            assertEquals(status.uuid, pubnub.configuration.uuid)
            assertNotNull(result)
        }
    }

    @Test
    fun testPublishSignalMessageSync() {
        pubnub.signal().apply {
            message = expectedPayload
            channel = expectedChannel
        }.sync()!!
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
                    pubnub.signal().apply {
                        message = expectedPayload
                        channel = expectedChannel
                    }.async { result, status ->
                        assertFalse(status.error)
                        assertEquals(PNOperationType.PNSignalOperation, status.operation)
                        assertEquals(status.uuid, pubnub.configuration.uuid)
                        assertNotNull(result)
                    }
                }
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                assertEquals(pubnub.configuration.uuid, pnSignalResult.publisher)
                assertEquals(expectedChannel, pnSignalResult.channel)
                assertEquals(expectedPayload, Gson().fromJson(pnSignalResult.message, String::class.java))
                success.set(true)
            }
        })

        observerClient.subscribe().apply {
            channels = listOf(expectedChannel)
        }.execute()

        success.listen()
    }

    @Test
    fun testPublishSignalMessageSyncWithoutChannel() {
        try {
            pubnub.signal().apply {
                message = randomValue()
            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testPublishSignalMessageSyncWithoutMessage() {
        try {
            pubnub.signal().apply {
                channel = randomChannel()
            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_MISSING, e)
        }
    }

    @Test
    fun testPublishSignalMessageSyncWithoutSubKey() {
        try {
            pubnub.configuration.subscribeKey = ""
            pubnub.signal().apply {
                channel = randomChannel()
                message = randomValue()
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }
}

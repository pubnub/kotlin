package com.pubnub.api.integration

import com.google.gson.Gson
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNubError
import com.pubnub.api.await
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

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
        ).await { result ->
            assertFalse(result.isFailure)
//            assertEquals(PNOperationType.PNSignalOperation, status.operation) // TODO can't check this now
//            assertEquals(status.uuid, pubnub.configuration.userId.value)
            assertNotNull(result)
        }
    }

    @Test
    fun testPublishSignalMessageSync() {
        pubnub.signal(
            message = expectedPayload,
            channel = expectedChannel
        ).sync()
    }

    @Test
    fun testReceiveSignalMessage() {
        val observerClient = createPubNub()
        pubnub.test {
            subscribe(expectedChannel)
            observerClient.signal(
                message = expectedPayload,
                channel = expectedChannel
            ).sync()
            val pnSignalResult = nextEvent<PNSignalResult>()
            assertEquals(observerClient.configuration.userId.value, pnSignalResult.publisher)
            assertEquals(expectedChannel, pnSignalResult.channel)
            assertEquals(expectedPayload, Gson().fromJson(pnSignalResult.message, String::class.java))
        }
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
}

package com.pubnub.api.integration

import com.google.gson.Gson
import com.pubnub.api.PubNubError
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Base64

class SignalIntegrationTests : BaseIntegrationTest() {
    lateinit var expectedChannel: String
    lateinit var expectedPayload: String
    lateinit var expectedCustomMessageType: String

    override fun onBefore() {
        expectedChannel = randomChannel()
        expectedPayload = randomValue(5)
        expectedCustomMessageType = randomValue(5)
    }

    @Test
    fun testPublishSignalMessageAsync() {
        pubnub.signal(
            channel = expectedChannel,
            message = expectedPayload,
            customMessageType = expectedCustomMessageType,
        ).await { result ->
            assertFalse(result.isFailure)
            assertTrue(result.getOrThrow().timetoken > 0)
        }
    }

    @Test
    fun testPublishSignalMessageSync() {
        pubnub.signal(
            channel = expectedChannel,
            message = expectedPayload,
            customMessageType = expectedCustomMessageType,
        ).sync()
    }

    @Test
    fun testReceiveSignalMessage() {
        val observerClient = createPubNub {}
        pubnub.test {
            subscribe(expectedChannel)
            observerClient.signal(
                channel = expectedChannel,
                message = expectedPayload,
                customMessageType = expectedCustomMessageType,
            ).sync()
            val pnSignalResult = nextEvent<PNSignalResult>()
            assertEquals(observerClient.configuration.userId.value, pnSignalResult.publisher)
            assertEquals(expectedChannel, pnSignalResult.channel)
            assertEquals(expectedPayload, Gson().fromJson(pnSignalResult.message, String::class.java))
            assertEquals(expectedCustomMessageType, pnSignalResult.customMessageType)
        }
    }

    @Test
    fun testReceiveSignalWithCryptoConfiguredOnSubscriber() {
        val subscriber = createPubNub { cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false) }
        subscriber.test {
            subscribe(expectedChannel)
            pubnub.signal(
                channel = expectedChannel,
                message = expectedPayload,
                customMessageType = expectedCustomMessageType,
            ).sync()
            val pnSignalResult = nextEvent<PNSignalResult>()
            assertEquals(expectedChannel, pnSignalResult.channel)
            assertEquals(expectedPayload, Gson().fromJson(pnSignalResult.message, String::class.java))
            assertEquals(expectedCustomMessageType, pnSignalResult.customMessageType)
        }
    }

    @Test
    fun testReceivePreEncryptedSignalIsNotDecrypted() {
        val ciphertext =
            Base64.getEncoder().encodeToString(
                CryptoModule.createLegacyCryptoModule("enigma", randomIv = false).encrypt("\"x\"".toByteArray()),
            )
        val subscriber = createPubNub { cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false) }
        subscriber.test {
            subscribe(expectedChannel)
            pubnub.signal(channel = expectedChannel, message = ciphertext).sync()
            val pnSignalResult = nextEvent<PNSignalResult>()
            assertEquals(ciphertext, Gson().fromJson(pnSignalResult.message, String::class.java))
        }
    }

    @Test
    fun testPublishSignalMessageSyncWithoutSubKey() {
        try {
            clientConfig = { subscribeKey = "" }
            pubnub.signal(
                channel = randomChannel(),
                message = randomValue(),
            ).sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }
}

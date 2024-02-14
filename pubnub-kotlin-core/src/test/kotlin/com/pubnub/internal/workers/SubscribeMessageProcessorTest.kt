package com.pubnub.internal.workers

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.encryptString
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.models.server.SubscribeMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isA
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.hamcrest.core.Is.`is` as iz

@RunWith(Parameterized::class)
class SubscribeMessageProcessorTest(
    private val messageJson: JsonElement
) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Any> {
            return listOf(
                JsonPrimitive("thisIsMessage"),
                JsonObject().apply { add("test", JsonPrimitive("value")) },
                JsonArray().apply {
                    add(JsonPrimitive("array"))
                    add(JsonPrimitive("of"))
                    add(JsonPrimitive("elements"))
                },
                JsonPrimitive(true),
                JsonPrimitive(1337),
                JsonNull.INSTANCE,
                JsonObject().apply {
                    add(
                        "with",
                        JsonObject().apply {
                            add(
                                "array",
                                JsonArray().apply {
                                    add(JsonPrimitive("array"))
                                    add(JsonPrimitive("of"))
                                    add(JsonPrimitive("elements"))
                                }
                            )
                        }
                    )
                },
            )
        }
    }

    @Test
    fun testDifferentJsonMessages() {
        val gson = Gson()
        val configuration = config()

        val messageProcessor = messageProcessor(configuration)

        val result = messageProcessor.processIncomingPayload(
            gson.fromJson(
                fileMessage(messageJson.toString()),
                SubscribeMessage::class.java
            )
        )

        assertThat(result, isA(PNFileEventResult::class.java))
        assertThat((result as PNFileEventResult).jsonMessage, iz(messageJson))
    }

    @Test
    fun testProcessFileUnencryptedWithCrypto() {
        val gson = Gson()
        val configuration = config()
        configuration.cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false)

        val messageProcessor = messageProcessor(configuration)

        val result = messageProcessor.processIncomingPayload(
            gson.fromJson(
                fileMessage(messageJson.toString()),
                SubscribeMessage::class.java
            )
        )

        assertThat(result, isA(PNFileEventResult::class.java))
        assertThat((result as PNFileEventResult).jsonMessage, iz(messageJson))
        assertThat((result as PNFileEventResult).error, iz(PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED))
    }

    @Test
    fun testProcessMessageEncryptedWithCrypto() {
        // given
        val gson = Gson()
        val config: PNConfiguration = config()
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false)
        val subscribeMessageProcessor = messageProcessor(config)
        val messageEncrypted = config.cryptoModule!!.encryptString(messageJson.toString())

        // when
        val result = subscribeMessageProcessor.processIncomingPayload(
            gson.fromJson(
                message(JsonPrimitive(messageEncrypted)),
                SubscribeMessage::class.java
            )
        )

        // then
        assertThat(result, isA(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).message, iz(messageJson))
    }

    @Test
    @Throws(PubNubException::class)
    fun testProcessMessageUnencryptedWithCrypto() {
        // given
        val gson = Gson()
        val config: PNConfiguration = config()
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false)
        val subscribeMessageProcessor = messageProcessor(config)

        // when
        val result = subscribeMessageProcessor.processIncomingPayload(
            gson.fromJson(
                message(messageJson),
                SubscribeMessage::class.java
            )
        )

        // then
        assertThat(result, isA(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).message, iz(messageJson))
    }

    @Test
    fun testProcessMessageWithPnOtherEncryptedWithCrypto() {
        // given
        val gson = Gson()
        val config: PNConfiguration = config()
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false)
        val subscribeMessageProcessor = messageProcessor(config)
        val message = "Hello world."
        val messageEncrypted = "bk8x+ZEg+Roq8ngUo7lfFg=="
        val messageObject = JsonObject()
        messageObject.add("something", messageJson)
        messageObject.addProperty("pn_other", messageEncrypted)
        val expectedObject = JsonObject()
        expectedObject.add("something", messageJson)
        expectedObject.addProperty("pn_other", message)

        // when
        val result = subscribeMessageProcessor.processIncomingPayload(
            gson.fromJson(
                message(messageObject),
                SubscribeMessage::class.java
            )
        )

        // then
        assertThat(result, isA(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).message, iz(expectedObject))
    }

    private fun config() = PNConfiguration(userId = UserId("test"))
    private fun messageProcessor(configuration: PNConfiguration) = SubscribeMessageProcessor(
        pubnub = TestPubNub(configuration).pubNubImpl,
        duplicationManager = DuplicationManager(configuration)
    )

    private fun message(messageJson: JsonElement): String {
        return "{\"a\":\"2\",\"f\":0,\"i\":\"client-2bdc6006-1b48-45e4-9c09-9cc4c5ac5e8c\",\"s\":1,\"p\":{\"t\":\"17000393136828867\",\"r\":43},\"k\":\"sub-c-33f55052-190b-11e6-bfbc-02ee2ddab7fe\",\"c\":\"ch_cxnysctxlw\",\"d\":$messageJson,\"b\":\"ch_cxnysctxlw\"}"
    }
    private fun fileMessage(messageJson: String) =
        """{"a":"0","f":0,"e":4,"i":"client-52774e6f-2f4e-4915-aefd-e8bb75cd2e7d","p":{"t":"16632349939765880","r":43},"k":"sub-c-4b1dbfef-2fa9-495f-a316-2b634063083d","c":"ch_1663234993171_F4FC4F460F","u":"This is meta","d":{"message":$messageJson,"file":{"id":"30ce0095-3c50-4cdc-a626-bf402d233731","name":"fileNamech_1663234993171_F4FC4F460F.txt"}}}"""
}

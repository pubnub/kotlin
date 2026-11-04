package com.pubnub.internal.workers

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.pubnub.api.PubNubError
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.crypto.encryptString
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.models.server.SubscribeMessage
import com.pubnub.internal.v2.PNConfigurationImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.junit.Test
import org.hamcrest.core.Is.`is` as iz

/**
 * Only client-encrypted subscribe types, as defined by [SubscribeMessage.supportsEncryption] (null/e=0 messages,
 * e=4 files), go through the crypto module. Signals (e=1), server-generated types (e=2/3/5) and unknown types are
 * delivered or dropped without a decrypt attempt or a
 * [PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED] warn.
 */
class SubscribeMessageProcessorCryptoSkipTest {
    private val gson = Gson()

    private class CapturingLogger : CustomLogger {
        val warnMessages = mutableListOf<LogMessage>()

        override fun warn(logMessage: LogMessage) {
            warnMessages.add(logMessage)
        }
    }

    private val notEncryptedText = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED.message

    private fun CapturingLogger.loggedNotEncryptedWarn(): Boolean =
        warnMessages.any { (it.message as? LogMessageContent.Text)?.message == notEncryptedText }

    private fun processWithCrypto(
        type: Int?,
        dataJson: String,
        logger: CapturingLogger,
    ): Any? {
        val configuration =
            PNConfigurationImpl.Builder(userId = UserId("test"), "").apply {
                cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma", false)
            }.build()
        val processor = SubscribeMessageProcessor(
            pubnub = PubNubImpl(configuration),
            duplicationManager = DuplicationManager(configuration),
            logConfig = LogConfig("testPnInstanceId", "testUserId", listOf(logger)),
        )
        val frame =
            """
            {"a":"1","f":0,"e":$type,"i":"client-1","p":{"t":"17000393136828867","r":43},
             "k":"sub-key","c":"ch-1","b":"ch-1","d":$dataJson}
            """.trimIndent()
        // Decryption runs before type-specific parsing, so the warn (or its absence) is independent of
        // whether this minimal payload parses for the given type.
        return runCatching { processor.processIncomingPayload(gson.fromJson(frame, SubscribeMessage::class.java)) }
            .getOrNull()
    }

    private val setUserData =
        """
        {
          "version": "1.0",
          "metadata": { "event": "create", "type": "user", "className": "User" },
          "data": {
            "id": "user-1",
            "createdAt": "2026-01-01T00:00:00Z",
            "updatedAt": "2026-01-02T00:00:00Z",
            "eTag": "etag-1",
            "expiresAt": "2026-02-01T00:00:00Z"
          }
        }
        """.trimIndent()

    @Test
    fun `e5 DataSync event is delivered without a not-encrypted warn`() {
        val logger = CapturingLogger()

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_DATASYNC, setUserData, logger)

        assertThat(result, instanceOf(PNDataSyncEventResult::class.java))
        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e2 objects event does not log a not-encrypted warn`() {
        val logger = CapturingLogger()

        processWithCrypto(SubscribeMessageProcessor.TYPE_OBJECT, """{"test":"value"}""", logger)

        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e3 message action event does not log a not-encrypted warn`() {
        val logger = CapturingLogger()

        processWithCrypto(SubscribeMessageProcessor.TYPE_MESSAGE_ACTION, """{"test":"value"}""", logger)

        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e0 plain message still goes through decryption and warns`() {
        // Control: proves the capture works and that client-publishable types are still decrypted.
        val logger = CapturingLogger()

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_MESSAGE, """{"test":"value"}""", logger)

        assertThat(result, instanceOf(PNMessageResult::class.java))
        assertThat(logger.loggedNotEncryptedWarn(), iz(true))
    }

    @Test
    fun `e1 object signal is delivered unchanged without a not-encrypted warn`() {
        val logger = CapturingLogger()

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_SIGNAL, """{"test":"value"}""", logger)

        assertThat(result, instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).message, iz(JsonParser.parseString("""{"test":"value"}""")))
        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e1 plain string signal is delivered unchanged without a not-encrypted warn`() {
        val logger = CapturingLogger()

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_SIGNAL, "\"thisIsSignal\"", logger)

        assertThat(result, instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).message, iz(JsonPrimitive("thisIsSignal") as JsonElement))
        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e1 signal whose payload is valid ciphertext is not decrypted`() {
        val logger = CapturingLogger()
        val ciphertext = CryptoModule.createAesCbcCryptoModule("enigma", false).encryptString("\"secret\"")
        val payloadJson = gson.toJson(JsonPrimitive(ciphertext))

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_SIGNAL, payloadJson, logger)

        assertThat(result, instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).message, iz(JsonPrimitive(ciphertext) as JsonElement))
    }

    @Test
    fun `e1 signal whose ciphertext decrypts to non-JSON does not throw`() {
        val logger = CapturingLogger()
        val ciphertext = CryptoModule.createAesCbcCryptoModule("enigma", false).encryptString("not json {")
        val payloadJson = gson.toJson(JsonPrimitive(ciphertext))

        val result = processWithCrypto(SubscribeMessageProcessor.TYPE_SIGNAL, payloadJson, logger)

        assertThat(result, instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).message, iz(JsonPrimitive(ciphertext) as JsonElement))
    }

    @Test
    fun `unknown event type is not decrypted`() {
        val logger = CapturingLogger()

        val result = processWithCrypto(6, "\"thisIsUnknown\"", logger)

        assertThat(result, iz(nullValue()))
        assertThat(logger.loggedNotEncryptedWarn(), iz(false))
    }

    @Test
    fun `e4 file payload still goes through decryption and warns`() {
        val logger = CapturingLogger()

        processWithCrypto(SubscribeMessageProcessor.TYPE_FILES, """{"test":"value"}""", logger)

        assertThat(logger.loggedNotEncryptedWarn(), iz(true))
    }

    @Test
    fun `legacy frame with null type still goes through decryption and warns`() {
        val logger = CapturingLogger()

        val result = processWithCrypto(null, """{"test":"value"}""", logger)

        assertThat(result, instanceOf(PNMessageResult::class.java))
        assertThat(logger.loggedNotEncryptedWarn(), iz(true))
    }
}

package com.pubnub.internal.workers

import com.google.gson.Gson
import com.pubnub.api.PubNubError
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.models.server.SubscribeMessage
import com.pubnub.internal.v2.PNConfigurationImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.Test
import org.hamcrest.core.Is.`is` as iz

/**
 * Server-generated subscribe types (`e=2` objects, `e=3` message actions, `e=5` DataSync) are never
 * client-encrypted, so a configured crypto module must not try to decrypt them and log a
 * [PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED] warn per event.
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
        type: Int,
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
}

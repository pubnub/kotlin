package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.test.CommonUtils.generatePayload
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.Keys
import org.junit.Assert.assertTrue
import org.junit.Test

class LoggingIntegrationTest : BaseIntegrationTest() {
    @Test
    fun testCanLogMessagesUsingCustomLogger() {
        // Clear any previous messages
        CustomLoggerTestImpl.clear()

        val expectedUuid = PubNub.generateUUID()
        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = Keys.pubKey
            customLoggers = listOf(CustomLoggerTestImpl())
        }
        val pubNub = PubNub.create(configBuilder.build())

        val expectedChannel = randomChannel()
        pubNub.publish(
            channel = expectedChannel,
            message = generatePayload(),
            customMessageType = "myType"
        ).sync()

        // Verify logging
        assertTrue("Should have received string messages", CustomLoggerTestImpl.stringMessages.isNotEmpty())
        assertTrue("Should have received LogMessage objects", CustomLoggerTestImpl.logMessages.isNotEmpty())

        // Verify content
        assertTrue(
            "Should have called publish on the expected channel",
            CustomLoggerTestImpl.stringMessages.any { msg ->
                msg.contains("PublishEndpoint") && msg.contains(expectedChannel)
            }
        )

        assertTrue(
            "Should have called publish on the expected channel",
            CustomLoggerTestImpl.stringMessages.any { msg ->
                msg.contains("NetworkRequest") && msg.contains(expectedChannel)
            }
        )

        assertTrue(
            "Should have called publish on the expected channel",
            CustomLoggerTestImpl.stringMessages.any { msg ->
                msg.contains("NetworkResponse") && msg.contains(expectedChannel)
            }
        )

        assertTrue(
            "Should have called publish on the expected channel",
            CustomLoggerTestImpl.logMessages.any { msg ->
                msg.type == LogMessageType.OBJECT && msg.location!!.contains("PublishEndpoint") &&
                    (msg.message as? LogMessageContent.Object)?.message?.get("channel") == expectedChannel
            }
        )

        assertTrue(
            "Should have called publish API. LogMessage type should be NETWORK_REQUEST",
            CustomLoggerTestImpl.logMessages.any { msg ->
                msg.type == LogMessageType.NETWORK_REQUEST && msg.location!!.contains("publish")
            }
        )

        assertTrue(
            "Should have called publish API. LogMessage type should be NETWORK_RESPONSE",
            CustomLoggerTestImpl.logMessages.any { msg ->
                msg.type == LogMessageType.NETWORK_RESPONSE && msg.location!!.contains("publish")
            }
        )
    }

    class CustomLoggerTestImpl : CustomLogger {
        override val name: String = "CustomLoggerTestImpl"

        override fun trace(message: String?) {
            stringMessages.add(message ?: "")
        }

        override fun trace(message: LogMessage) {
            logMessages.add(message)
        }

        override fun debug(message: String?) {
            stringMessages.add(message ?: "")
        }

        override fun debug(logMessage: LogMessage) {
            logMessages.add(logMessage)
        }

        companion object {
            val stringMessages = mutableListOf<String>()
            val logMessages = mutableListOf<LogMessage>()

            fun clear() {
                stringMessages.clear()
                logMessages.clear()
            }
        }
    }
}

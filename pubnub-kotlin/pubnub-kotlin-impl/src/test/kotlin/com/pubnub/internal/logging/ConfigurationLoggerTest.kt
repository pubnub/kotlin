package com.pubnub.internal.logging

import com.pubnub.api.UserId
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.v2.PNConfigurationImpl
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.event.Level

class ConfigurationLoggerTest {
    @Test
    fun `should log configuration with all required fields`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key"
        )
        val instanceId = "test-instance-123"
        val testClass = String::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        assertEquals(instanceId, capturedMessage.pubNubId)
        assertEquals(Level.DEBUG, capturedMessage.logLevel)
        assertEquals(testClass.toString(), capturedMessage.location)
        assertEquals(LogMessageType.OBJECT, capturedMessage.type)
        assertEquals("Configuration logged", capturedMessage.details)

        val configSummary = capturedMessage.message as LogMessageContent.Object
        assertNotNull(configSummary.message)
        assertEquals("test-user", configSummary.message["userId"])
        assertEquals("test-sub-key", configSummary.message["subscribeKey"])
    }

    @Test
    fun `should handle optional fields correctly`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            publishKey = "test-pub-key",
            secretKey = "test-secret",
            authKey = "test-auth",
            origin = "custom-origin"
        )
        val instanceId = "test-instance-123"
        val testClass = Int::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("test-pub-key", configSummary.message["publishKey"])
        assertEquals("set: *****", configSummary.message["secretKey"])
        assertEquals("(@Deprecated) set: *****", configSummary.message["authKey"])
        assertEquals("custom-origin", configSummary.message["origin"])
    }

    @Test
    fun `should handle null and empty optional fields`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            publishKey = "",
            secretKey = "",
            authKey = "",
            origin = ""
        )
        val instanceId = "test-instance-123"
        val testClass = Boolean::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("not set", configSummary.message["publishKey"])
        assertEquals("not set", configSummary.message["secretKey"])
        assertEquals("not set", configSummary.message["authKey"])
        assertEquals("default", configSummary.message["origin"])
    }

    @Test
    fun `should handle custom loggers configuration`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()
        val customLogger = mockk<com.pubnub.api.logging.CustomLogger>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            customLoggers = listOf(customLogger)
        )
        val instanceId = "test-instance-123"
        val testClass = List::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("enabled (1 logger)", configSummary.message["customLoggers"])
    }

    @Test
    fun `should handle multiple custom loggers`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()
        val customLogger1 = mockk<com.pubnub.api.logging.CustomLogger>()
        val customLogger2 = mockk<com.pubnub.api.logging.CustomLogger>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            customLoggers = listOf(customLogger1, customLogger2)
        )
        val instanceId = "test-instance-123"
        val testClass = Map::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("enabled (2 loggers)", configSummary.message["customLoggers"])
    }

    @Test
    fun `should handle null custom loggers`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            customLoggers = null
        )
        val instanceId = "test-instance-123"
        val testClass = Set::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("not set", configSummary.message["customLoggers"])
    }

    @Test
    fun `should handle crypto module configuration`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()
        val cryptoModule = mockk<com.pubnub.api.crypto.CryptoModule>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            cryptoModule = cryptoModule
        )
        val instanceId = "test-instance-123"
        val testClass = Array::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("enabled", configSummary.message["cryptoModule"])
    }

    @Test
    fun `should handle disabled crypto module`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key",
            cryptoModule = null
        )
        val instanceId = "test-instance-123"
        val testClass = CharSequence::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        assertEquals("disabled", configSummary.message["cryptoModule"])
    }

    @Test
    fun `should include all configuration fields in summary`() {
        // Given
        val mockLogger = mockk<PNLogger>(relaxed = true)
        val logMessageSlot = slot<LogMessage>()

        val config = PNConfigurationImpl(
            userId = UserId("test-user"),
            subscribeKey = "test-sub-key"
        )
        val instanceId = "test-instance-123"
        val testClass = Any::class.java

        // When
        ConfigurationLogger.logConfiguration(config, mockLogger, instanceId, testClass)

        // Then
        verify { mockLogger.debug(capture(logMessageSlot)) }

        val capturedMessage = logMessageSlot.captured
        val configSummary = capturedMessage.message as LogMessageContent.Object

        // Verify all expected fields are present
        val expectedFields = listOf(
            "userId", "subscribeKey", "publishKey", "secretKey", "secure", "origin",
            "logVerbosity", "cacheBusting", "connectTimeout", "subscribeTimeout",
            "nonSubscribeReadTimeout", "presenceTimeout", "heartbeatInterval",
            "heartbeatNotificationOptions", "suppressLeaveEvents", "maintainPresenceState",
            "authKey", "authToken", "filterExpression", "dedupOnSubscribe",
            "maximumMessagesCacheSize", "retryConfiguration", "fileMessagePublishRetryLimit",
            "includeInstanceIdentifier", "includeRequestIdentifier", "pnsdkSuffixes",
            "cryptoModule", "managePresenceListManually", "customLoggers", "proxy",
            "proxySelector", "proxyAuthenticator", "maximumConnections", "httpLoggingInterceptor",
            "sslSocketFactory", "x509ExtendedTrustManager", "connectionSpec",
            "hostnameVerifier", "certificatePinner", "googleAppEngineNetworking", "instanceId"
        )

        expectedFields.forEach { field ->
            assertTrue(configSummary.message.containsKey(field), "Missing field: $field")
        }
    }
}

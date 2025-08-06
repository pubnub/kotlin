package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.event.Level

class LoggerManagerTest {
    @Test
    fun `should create logger successfully when customLoggers are null`() {
        val mockSlf4jLogger = mockk<Logger>(relaxed = true)
        val mockFactory = { _: Class<*> -> mockSlf4jLogger }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val testLogConfig = LogConfig("test-instance-id", "test-user-id", customLoggers = null)

        val logger = manager.getLogger(testLogConfig, String::class.java)

        assertThat(logger, IsInstanceOf.instanceOf(CompositeLogger::class.java))
    }

    @Test
    fun `should create CompositeLogger when custom loggers are provided`() {
        val mockSlf4jLogger = mockk<Logger>(relaxed = true)
        val mockFactory = { _: Class<*> -> mockSlf4jLogger }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val customLogger = mockk<CustomLogger>(relaxed = true)
        every { customLogger.name } returns "TestCustomLogger"

        val testLogConfig = LogConfig(
            pnInstanceId = "test-instance-id",
            userId = "test-user-id",
            customLoggers = listOf(customLogger)
        )

        val logger = manager.getLogger(testLogConfig, String::class.java)

        assertThat(logger, IsInstanceOf.instanceOf(CompositeLogger::class.java))
    }

    @Test
    fun `should handle logger creation failure gracefully`() {
        val mockFactory = { _: Class<*> -> throw RuntimeException("SLF4J failed!") }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val testLogConfig = LogConfig("test-instance-id", "test-user-id")

        // Should not throw exception
        val logger = manager.getLogger(testLogConfig, String::class.java)

        // Verify it's not null and logging works
        assertThat(logger, IsInstanceOf.instanceOf(ExtendedLogger::class.java))

        // Should not crash when logging
        logger.error(
            LogMessage(
                pubNubId = testLogConfig.pnInstanceId,
                logLevel = Level.ERROR,
                location = "test",
                type = LogMessageType.ERROR,
                message = LogMessageContent.Text("test error")
            )
        )
    }

    @Test
    fun `should return fallback logger when SLF4J creation fails`() {
        val mockFactory = { _: Class<*> -> throw RuntimeException("SLF4J failed!") }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val testLogConfig = LogConfig("test-instance-id", "test-user-id")
        val logger = manager.getLogger(testLogConfig, String::class.java)

        // Should still be able to log without crashing
        logger.info(
            LogMessage(
                pubNubId = "test",
                logLevel = Level.INFO,
                location = "test",
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("test info")
            )
        )
    }

    @Test
    fun `should handle empty custom loggers list`() {
        val mockSlf4jLogger = mockk<Logger>(relaxed = true)
        val mockFactory = { _: Class<*> -> mockSlf4jLogger }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val testLogConfig = LogConfig(
            pnInstanceId = "test-instance-id",
            userId = "test-user-id",
            customLoggers = emptyList()
        )

        val logger = manager.getLogger(testLogConfig, String::class.java)

        // Should create ToPortalLogger, not CompositeLogger
        assertThat(logger, IsInstanceOf.instanceOf(CompositeLogger::class.java))
    }

    @Test
    fun `should handle null custom loggers`() {
        val mockSlf4jLogger = mockk<Logger>(relaxed = true)
        val mockFactory = { _: Class<*> -> mockSlf4jLogger }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val pnInstanceId = "test-instance-id"
        val testLogConfig = LogConfig(
            pnInstanceId = pnInstanceId,
            userId = "test-user-id",
            customLoggers = null
        )

        val logger = manager.getLogger(testLogConfig, String::class.java)

        // Should create ToPortalLogger, not CompositeLogger
        assertThat(logger, IsInstanceOf.instanceOf(CompositeLogger::class.java))
    }

    @Test
    fun `should use default instance correctly`() {
        val manager = LoggerManager.instance
        val testLogConfig = LogConfig("test-instance-id", "test-user-id")

        val logger = manager.getLogger(testLogConfig, String::class.java)

        assertThat(logger, IsInstanceOf.instanceOf(ExtendedLogger::class.java))
    }

    @Test
    fun `should create different loggers for different classes`() {
        val mockSlf4jLogger1 = mockk<Logger>(relaxed = true)
        val mockSlf4jLogger2 = mockk<Logger>(relaxed = true)

        val mockFactory = { clazz: Class<*> ->
            when (clazz) {
                String::class.java -> mockSlf4jLogger1
                Int::class.java -> mockSlf4jLogger2
                else -> mockk<Logger>(relaxed = true)
            }
        }

        val manager = LoggerManager(loggerFactory = mockFactory)
        val testLogConfig = LogConfig("test-instance-id", "test-user-id")

        val logger1 = manager.getLogger(testLogConfig, String::class.java)
        val logger2 = manager.getLogger(testLogConfig, Int::class.java)

        // Both should be ExtendedLogger but potentially different instances
        assertThat(logger1, IsInstanceOf.instanceOf(ExtendedLogger::class.java))
        assertThat(logger2, IsInstanceOf.instanceOf(ExtendedLogger::class.java))
    }

    @Test
    fun `should handle multiple custom loggers`() {
        val mockSlf4jLogger = mockk<Logger>(relaxed = true)
        val mockFactory = { _: Class<*> -> mockSlf4jLogger }
        val manager = LoggerManager(loggerFactory = mockFactory)

        val customLogger1 = mockk<CustomLogger>(relaxed = true)
        val customLogger2 = mockk<CustomLogger>(relaxed = true)
        every { customLogger1.name } returns "CustomLogger1"
        every { customLogger2.name } returns "CustomLogger2"

        val testLogConfig = LogConfig(
            pnInstanceId = "test-instance-id",
            userId = "test-user-id",
            customLoggers = listOf(customLogger1, customLogger2)
        )

        val logger = manager.getLogger(testLogConfig, String::class.java)

        assertThat(logger, IsInstanceOf.instanceOf(CompositeLogger::class.java))
    }

    @Test
    fun `should handle ToPortalLogger creation failure and fallback to basic SLF4J`() {
        var callCount = 0
        val mockFactory = { _: Class<*> ->
            callCount++
            if (callCount == 1) {
                throw RuntimeException("First call failed")
            } else {
                mockk<Logger>(relaxed = true)
            }
        }

        val manager = LoggerManager(loggerFactory = mockFactory)
        val testLogConfig = LogConfig("test-instance-id", "test-user-id")

        val logger = manager.getLogger(testLogConfig, String::class.java)

        // Should still get a working logger (fallback)
        assertThat(logger, IsInstanceOf.instanceOf(ExtendedLogger::class.java))

        // Should be able to log without crashing
        logger.warn(
            LogMessage(
                pubNubId = "test",
                logLevel = Level.WARN,
                location = "test",
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("test warning")
            )
        )
    }
}

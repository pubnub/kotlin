package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.event.Level

class CompositeLoggerTest {
    private val mockSlf4jLogger = mockk<Logger>(relaxed = true)
    private val mockToPortalLogger = mockk<PNLogger>(relaxed = true)
    private val mockCustomLogger1 = mockk<CustomLogger>(relaxed = true)
    private val mockCustomLogger2 = mockk<CustomLogger>(relaxed = true)
    private val testInstanceId = "test-instance-id"

    private fun createTestLogMessage(level: Level): LogMessage {
        return LogMessage(
            location = "test-location",
            type = LogMessageType.TEXT,
            message = LogMessageContent.Text("test message"),
            pubNubId = testInstanceId,
            logLevel = level
        )
    }

    @Test
    fun `should delegate trace message to all loggers`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.TRACE)

        compositeLogger.trace(testMessage)

        verify { mockSlf4jLogger.trace(any<String>()) }
        verify { mockToPortalLogger.trace(testMessage) }
        verify { mockCustomLogger1.trace(logMessage = testMessage) }
        verify { mockCustomLogger1.trace(message = any<String>()) }
        verify { mockCustomLogger2.trace(logMessage = testMessage) }
        verify { mockCustomLogger2.trace(message = any<String>()) }
    }

    @Test
    fun `should delegate debug message to all loggers`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.DEBUG)

        compositeLogger.debug(testMessage)

        verify { mockSlf4jLogger.debug(any<String>()) }
        verify { mockToPortalLogger.debug(testMessage) }
        verify { mockCustomLogger1.debug(logMessage = testMessage) }
        verify { mockCustomLogger1.debug(message = any<String>()) }
        verify { mockCustomLogger2.debug(logMessage = testMessage) }
        verify { mockCustomLogger2.debug(message = any<String>()) }
    }

    @Test
    fun `should delegate info message to all loggers`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(testMessage) }
        verify { mockCustomLogger1.info(logMessage = testMessage) }
        verify { mockCustomLogger1.info(message = any<String>()) }
        verify { mockCustomLogger2.info(logMessage = testMessage) }
        verify { mockCustomLogger2.info(message = any<String>()) }
    }

    @Test
    fun `should delegate warn message to all loggers`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.WARN)

        compositeLogger.warn(testMessage)

        verify { mockSlf4jLogger.warn(any<String>()) }
        verify { mockToPortalLogger.warn(testMessage) }
        verify { mockCustomLogger1.warn(logMessage = testMessage) }
        verify { mockCustomLogger1.warn(message = any<String>()) }
        verify { mockCustomLogger2.warn(logMessage = testMessage) }
        verify { mockCustomLogger2.warn(message = any<String>()) }
    }

    @Test
    fun `should delegate error message to all loggers`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.ERROR)

        compositeLogger.error(testMessage)

        verify { mockSlf4jLogger.error(any<String>()) }
        verify { mockToPortalLogger.error(testMessage) }
        verify { mockCustomLogger1.error(logMessage = testMessage) }
        verify { mockCustomLogger1.error(message = any<String>()) }
        verify { mockCustomLogger2.error(logMessage = testMessage) }
        verify { mockCustomLogger2.error(message = any<String>()) }
    }

    @Test
    fun `should work without portal logger`() {
        val customLoggers = listOf(mockCustomLogger1)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = null,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockCustomLogger1.info(logMessage = testMessage) }
        verify { mockCustomLogger1.info(message = any<String>()) }
    }

    @Test
    fun `should work without custom loggers`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = null,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(testMessage) }
    }

    @Test
    fun `should work with empty custom loggers list`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = emptyList(),
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(testMessage) }
    }

    @Test
    fun `should isolate custom logger failures`() {
        every { mockCustomLogger1.name } returns "FailingLogger1"
        every { mockCustomLogger1.info(logMessage = any()) } throws RuntimeException("Logger 1 failed")
        every { mockCustomLogger2.name } returns "WorkingLogger2"

        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        compositeLogger.info(testMessage)

        // Primary loggers should still work
        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(testMessage) }

        // Working custom logger should still be called
        verify { mockCustomLogger2.info(logMessage = testMessage) }
        verify { mockCustomLogger2.info(message = any<String>()) }

        // Error should be logged about the failing logger
        verify { mockSlf4jLogger.error(match<String> { it.contains("FailingLogger1") && it.contains("failed") }) }
        verify { mockToPortalLogger.error(any()) }
    }

    @Test
    fun `should handle failure in error reporting`() {
        every { mockCustomLogger1.name } returns "FailingLogger"
        every { mockCustomLogger1.info(logMessage = any()) } throws RuntimeException("Logger failed")
        every { mockSlf4jLogger.error(any<String>()) } throws RuntimeException("SLF4J also failed")

        val customLoggers = listOf(mockCustomLogger1)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)

        // Should not throw exception even when error reporting fails
        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(testMessage) }
    }

    @Test
    fun `should use correct instance id in error messages`() {
        every { mockCustomLogger1.name } returns "FailingLogger"
        every { mockCustomLogger1.info(logMessage = any()) } throws RuntimeException("Test failure")

        val customLoggers = listOf(mockCustomLogger1)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage(Level.INFO)
        val errorMessageSlot = slot<LogMessage>()
        every { mockToPortalLogger.error(capture(errorMessageSlot)) } returns Unit

        compositeLogger.info(testMessage)

        verify { mockToPortalLogger.error(any()) }
        val capturedErrorMessage = errorMessageSlot.captured
        assertEquals(testInstanceId, capturedErrorMessage.pubNubId)
        assertEquals("CompositeLogger", capturedErrorMessage.location)
        assertEquals(Level.WARN, capturedErrorMessage.logLevel)
        assertEquals(LogMessageType.ERROR, capturedErrorMessage.type)
    }

    @Test
    fun `should be thread safe with concurrent access`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            pnInstanceId = testInstanceId
        )

        val messages = (1..100).map { i ->
            LogMessage(
                location = "thread-test",
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("message $i"),
                pubNubId = testInstanceId,
                logLevel = Level.INFO
            )
        }

        // Execute logging from multiple threads concurrently
        val threads = (1..10).map { threadId ->
            Thread {
                messages.forEach { message ->
                    compositeLogger.info(message)
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // Verify that all messages were processed (1000 total: 10 threads * 100 messages each)
        verify(exactly = 1000) { mockSlf4jLogger.info(any<String>()) }
        verify(exactly = 1000) { mockToPortalLogger.info(any()) }
        verify(exactly = 1000) { mockCustomLogger1.info(logMessage = any()) }
        verify(exactly = 1000) { mockCustomLogger2.info(logMessage = any()) }
    }
}

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
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.event.Level

class CompositeLoggerTest {
    private val mockSlf4jLogger = mockk<Logger>(relaxed = true)
    private val mockToPortalLogger = mockk<PNLogger>(relaxed = true)
    private val mockCustomLogger1 = mockk<CustomLogger>(relaxed = true)
    private val mockCustomLogger2 = mockk<CustomLogger>(relaxed = true)
    private val testInstanceId = "test-instance-id"
    private val testLocation = "CompositeLoggerTest"

    private fun createTestLogMessage(): LogMessage {
        return LogMessage(
            message = LogMessageContent.Text("test message"),
            details = "test details",
            type = LogMessageType.TEXT,
            location = "test-location"
        )
    }

    private fun createTestLogMessageWithNulls(): LogMessage {
        return LogMessage(
            message = LogMessageContent.Text("test message"),
            details = null,
            type = LogMessageType.TEXT,
            location = "test-location",
            pubNubId = null,
            logLevel = null
        )
    }

    @Test
    fun `should delegate trace message to all loggers with enhanced message`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()
        val portalLogSlot = slot<LogMessage>()
        val customLog1Slot = slot<LogMessage>()
        val customLog2Slot = slot<LogMessage>()

        every { mockToPortalLogger.trace(capture(portalLogSlot)) } returns Unit
        every { mockCustomLogger1.trace(logMessage = capture(customLog1Slot)) } returns Unit
        every { mockCustomLogger2.trace(logMessage = capture(customLog2Slot)) } returns Unit

        compositeLogger.trace(originalMessage)

        verify { mockSlf4jLogger.trace(any<String>()) }
        verify { mockToPortalLogger.trace(any()) }
        verify { mockCustomLogger1.trace(logMessage = any()) }
        verify { mockCustomLogger1.trace(message = any<String>()) }
        verify { mockCustomLogger2.trace(logMessage = any()) }
        verify { mockCustomLogger2.trace(message = any<String>()) }

        // Verify enhanced messages have correct pubNubId and logLevel
        assertEquals(testInstanceId, portalLogSlot.captured.pubNubId)
        assertEquals(Level.TRACE, portalLogSlot.captured.logLevel)
        assertEquals(testInstanceId, customLog1Slot.captured.pubNubId)
        assertEquals(Level.TRACE, customLog1Slot.captured.logLevel)
        assertEquals(testInstanceId, customLog2Slot.captured.pubNubId)
        assertEquals(Level.TRACE, customLog2Slot.captured.logLevel)
    }

    @Test
    fun `should delegate debug message to all loggers with enhanced message`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()
        val portalLogSlot = slot<LogMessage>()
        val customLog1Slot = slot<LogMessage>()
        val customLog2Slot = slot<LogMessage>()

        every { mockToPortalLogger.debug(capture(portalLogSlot)) } returns Unit
        every { mockCustomLogger1.debug(logMessage = capture(customLog1Slot)) } returns Unit
        every { mockCustomLogger2.debug(logMessage = capture(customLog2Slot)) } returns Unit

        compositeLogger.debug(originalMessage)

        verify { mockSlf4jLogger.debug(any<String>()) }
        verify { mockToPortalLogger.debug(any()) }
        verify { mockCustomLogger1.debug(logMessage = any()) }
        verify { mockCustomLogger1.debug(message = any<String>()) }
        verify { mockCustomLogger2.debug(logMessage = any()) }
        verify { mockCustomLogger2.debug(message = any<String>()) }

        // Verify enhanced messages have correct pubNubId and logLevel
        assertEquals(testInstanceId, portalLogSlot.captured.pubNubId)
        assertEquals(Level.DEBUG, portalLogSlot.captured.logLevel)
        assertEquals(testInstanceId, customLog1Slot.captured.pubNubId)
        assertEquals(Level.DEBUG, customLog1Slot.captured.logLevel)
        assertEquals(testInstanceId, customLog2Slot.captured.pubNubId)
        assertEquals(Level.DEBUG, customLog2Slot.captured.logLevel)
    }

    @Test
    fun `should delegate info message to all loggers with enhanced message`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()
        val portalLogSlot = slot<LogMessage>()
        val customLog1Slot = slot<LogMessage>()
        val customLog2Slot = slot<LogMessage>()

        every { mockToPortalLogger.info(capture(portalLogSlot)) } returns Unit
        every { mockCustomLogger1.info(logMessage = capture(customLog1Slot)) } returns Unit
        every { mockCustomLogger2.info(logMessage = capture(customLog2Slot)) } returns Unit

        compositeLogger.info(originalMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(any()) }
        verify { mockCustomLogger1.info(logMessage = any()) }
        verify { mockCustomLogger1.info(message = any<String>()) }
        verify { mockCustomLogger2.info(logMessage = any()) }
        verify { mockCustomLogger2.info(message = any<String>()) }

        // Verify enhanced messages have correct pubNubId and logLevel
        assertEquals(testInstanceId, portalLogSlot.captured.pubNubId)
        assertEquals(Level.INFO, portalLogSlot.captured.logLevel)
        assertEquals(testInstanceId, customLog1Slot.captured.pubNubId)
        assertEquals(Level.INFO, customLog1Slot.captured.logLevel)
        assertEquals(testInstanceId, customLog2Slot.captured.pubNubId)
        assertEquals(Level.INFO, customLog2Slot.captured.logLevel)
    }

    @Test
    fun `should delegate warn message to all loggers with enhanced message`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()
        val portalLogSlot = slot<LogMessage>()
        val customLog1Slot = slot<LogMessage>()
        val customLog2Slot = slot<LogMessage>()

        every { mockToPortalLogger.warn(capture(portalLogSlot)) } returns Unit
        every { mockCustomLogger1.warn(logMessage = capture(customLog1Slot)) } returns Unit
        every { mockCustomLogger2.warn(logMessage = capture(customLog2Slot)) } returns Unit

        compositeLogger.warn(originalMessage)

        verify { mockSlf4jLogger.warn(any<String>()) }
        verify { mockToPortalLogger.warn(any()) }
        verify { mockCustomLogger1.warn(logMessage = any()) }
        verify { mockCustomLogger1.warn(message = any<String>()) }
        verify { mockCustomLogger2.warn(logMessage = any()) }
        verify { mockCustomLogger2.warn(message = any<String>()) }

        // Verify enhanced messages have correct pubNubId and logLevel
        assertEquals(testInstanceId, portalLogSlot.captured.pubNubId)
        assertEquals(Level.WARN, portalLogSlot.captured.logLevel)
        assertEquals(testInstanceId, customLog1Slot.captured.pubNubId)
        assertEquals(Level.WARN, customLog1Slot.captured.logLevel)
        assertEquals(testInstanceId, customLog2Slot.captured.pubNubId)
        assertEquals(Level.WARN, customLog2Slot.captured.logLevel)
    }

    @Test
    fun `should delegate error message to all loggers with enhanced message`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()
        val portalLogSlot = slot<LogMessage>()
        val customLog1Slot = slot<LogMessage>()
        val customLog2Slot = slot<LogMessage>()

        every { mockToPortalLogger.error(capture(portalLogSlot)) } returns Unit
        every { mockCustomLogger1.error(logMessage = capture(customLog1Slot)) } returns Unit
        every { mockCustomLogger2.error(logMessage = capture(customLog2Slot)) } returns Unit

        compositeLogger.error(originalMessage)

        verify { mockSlf4jLogger.error(any<String>()) }
        verify { mockToPortalLogger.error(any()) }
        verify { mockCustomLogger1.error(logMessage = any()) }
        verify { mockCustomLogger1.error(message = any<String>()) }
        verify { mockCustomLogger2.error(logMessage = any()) }
        verify { mockCustomLogger2.error(message = any<String>()) }

        // Verify enhanced messages have correct pubNubId and logLevel
        assertEquals(testInstanceId, portalLogSlot.captured.pubNubId)
        assertEquals(Level.ERROR, portalLogSlot.captured.logLevel)
        assertEquals(testInstanceId, customLog1Slot.captured.pubNubId)
        assertEquals(Level.ERROR, customLog1Slot.captured.logLevel)
        assertEquals(testInstanceId, customLog2Slot.captured.pubNubId)
        assertEquals(Level.ERROR, customLog2Slot.captured.logLevel)
    }

    @Test
    fun `should work without portal logger`() {
        val customLoggers = listOf(mockCustomLogger1)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = null,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val originalMessage = createTestLogMessage()

        compositeLogger.info(originalMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockCustomLogger1.info(logMessage = any()) }
        verify { mockCustomLogger1.info(message = any<String>()) }
    }

    @Test
    fun `should work without custom loggers`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = null,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage()

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(any()) }
    }

    @Test
    fun `should work with empty custom loggers list`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = emptyList(),
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage()

        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(any()) }
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
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage()

        compositeLogger.info(testMessage)

        // Primary loggers should still work
        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(any()) }

        // Working custom logger should still be called
        verify { mockCustomLogger2.info(logMessage = any()) }
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
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage()

        // Should not throw exception even when error reporting fails
        compositeLogger.info(testMessage)

        verify { mockSlf4jLogger.info(any<String>()) }
        verify { mockToPortalLogger.info(any()) }
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
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val testMessage = createTestLogMessage()
        val errorMessageSlot = slot<LogMessage>()
        every { mockToPortalLogger.error(capture(errorMessageSlot)) } returns Unit

        compositeLogger.info(testMessage)

        verify { mockToPortalLogger.error(any()) }
        val capturedErrorMessage = errorMessageSlot.captured
        assertEquals(testInstanceId, capturedErrorMessage.pubNubId)
        assertEquals("CompositeLogger", capturedErrorMessage.location)
        assertEquals(Level.ERROR, capturedErrorMessage.logLevel)
        assertEquals(LogMessageType.ERROR, capturedErrorMessage.type)
    }

    @Test
    fun `should be thread safe with concurrent access`() {
        val customLoggers = listOf(mockCustomLogger1, mockCustomLogger2)
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = customLoggers,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val messages = (1..100).map { i ->
            LogMessage(
                message = LogMessageContent.Text("message $i"),
                type = LogMessageType.TEXT,
                location = "thread-test"
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

    @Test
    fun `should preserve all original message fields including timestamp`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = null,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val customTimestamp = "23:59:59.999"
        val originalMessage = LogMessage(
            message = LogMessageContent.Object(mapOf("key" to "value")),
            details = "important details",
            type = LogMessageType.OBJECT,
            location = "test-location",
            timestamp = customTimestamp
        )

        val capturedSlot = slot<LogMessage>()
        every { mockToPortalLogger.info(capture(capturedSlot)) } returns Unit

        compositeLogger.info(originalMessage)

        val enhancedMessage = capturedSlot.captured

        // Verify original fields are preserved
        assertEquals("test-location", enhancedMessage.location)
        assertEquals(LogMessageType.OBJECT, enhancedMessage.type)
        assertEquals(originalMessage.message, enhancedMessage.message)
        assertEquals("important details", enhancedMessage.details)
        assertEquals(customTimestamp, enhancedMessage.timestamp)

        // Verify injected fields
        assertEquals(testInstanceId, enhancedMessage.pubNubId)
        assertEquals(Level.INFO, enhancedMessage.logLevel)
    }

    @Test
    fun `should handle null fields gracefully`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = null,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        val messageWithNulls = createTestLogMessageWithNulls()
        val capturedSlot = slot<LogMessage>()
        every { mockToPortalLogger.debug(capture(capturedSlot)) } returns Unit

        compositeLogger.debug(messageWithNulls)

        val enhancedMessage = capturedSlot.captured

        // Verify null fields are preserved as null
        assertNull(enhancedMessage.details)

        // Verify injected fields override any null values
        assertEquals(testInstanceId, enhancedMessage.pubNubId)
        assertEquals(Level.DEBUG, enhancedMessage.logLevel)

        // Verify required fields are preserved
        assertEquals("test-location", enhancedMessage.location)
        assertEquals(LogMessageType.TEXT, enhancedMessage.type)
    }

    @Test
    fun `should override existing pubNubId and logLevel in original message`() {
        val compositeLogger = CompositeLogger(
            slf4jLogger = mockSlf4jLogger,
            toPortalLogger = mockToPortalLogger,
            customLoggers = null,
            location = testLocation,
            pnInstanceId = testInstanceId
        )

        // Create message with different pubNubId and logLevel
        val messageWithExistingValues = LogMessage(
            message = LogMessageContent.Text("test message"),
            type = LogMessageType.TEXT,
            location = "test-location",
            pubNubId = "different-instance-id",
            logLevel = Level.ERROR // This should be overridden to WARN
        )

        val capturedSlot = slot<LogMessage>()
        every { mockToPortalLogger.warn(capture(capturedSlot)) } returns Unit

        compositeLogger.warn(messageWithExistingValues)

        val enhancedMessage = capturedSlot.captured

        // Verify values are overridden with CompositeLogger's values
        assertEquals(testInstanceId, enhancedMessage.pubNubId)
        assertEquals(Level.WARN, enhancedMessage.logLevel)
    }
}

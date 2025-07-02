package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageType
import org.slf4j.Logger
import org.slf4j.event.Level
import org.slf4j.event.Level.TRACE
import org.slf4j.event.Level.DEBUG
import org.slf4j.event.Level.INFO
import org.slf4j.event.Level.WARN
import org.slf4j.event.Level.ERROR

/**
 * Abstract base logger that provides common functionality.
 * Both internal and custom loggers can extend this class.
 */
abstract class BaseDefaultLogger(val logMessageContext: LogMessageContext) : ExtendedLogger {
    // Delegate all Logger methods to the underlying SLF4J logger
    protected abstract val delegate: Logger

    /**
     * Called whenever a log message is processed.
     * Subclasses can override this to add custom behavior.
     */
    protected open fun onLog(level: Level, message: LogMessage) {
        // Default implementation does nothing
        println("onLog") // todo remove
    }

    override fun trace(message: LogMessage) {
        val simplifiedMessage = getSimplifiedMessage(message)
        delegate.trace(simplifiedMessage)
        onLog(TRACE, message)
    }

    override fun debug(message: LogMessage) {
        val simplifiedMessage = getSimplifiedMessage(message)
        delegate.debug(simplifiedMessage)
        onLog(DEBUG, message)
    }

    override fun info(message: LogMessage) {
        val simplifiedMessage = getSimplifiedMessage(message)
        delegate.info(simplifiedMessage)
        onLog(INFO, message)
    }

    override fun warn(message: LogMessage) {
        val simplifiedMessage = getSimplifiedMessage(message)
        delegate.warn(simplifiedMessage)
        onLog(WARN, message)
    }

    override fun error(message: LogMessage) {
        val simplifiedMessage = getSimplifiedMessage(message)
        delegate.error(simplifiedMessage)
        onLog(ERROR, message)
    }
}

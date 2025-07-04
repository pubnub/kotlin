package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import com.pubnub.internal.logging.networkLogging.simplified
import org.slf4j.Logger
import org.slf4j.event.Level
import org.slf4j.event.Level.DEBUG
import org.slf4j.event.Level.ERROR
import org.slf4j.event.Level.INFO
import org.slf4j.event.Level.TRACE
import org.slf4j.event.Level.WARN

/**
 * Abstract base logger that provides common functionality.
 * Both internal and custom loggers can extend this class.
 */
// todo is BaseDefaultLogger ok. Maybe it can be renamed?
abstract class BaseDefaultLogger(val logMessageContext: LogMessageContext) : ExtendedLogger {
    // Delegate all Logger methods to the underlying SLF4J logger
    protected abstract val delegate: Logger

    /**
     * Called whenever a log message is processed.
     * Subclasses can override this to add custom behavior.
     */
    protected open fun onLog(level: Level, message: LogMessage) {
        // Default implementation does nothing
    }

    override fun trace(message: LogMessage) {
        delegate.trace(message.simplified())
        onLog(TRACE, message)
    }

    override fun debug(message: LogMessage) {
        delegate.debug(message.simplified())
        onLog(DEBUG, message)
    }

    override fun info(message: LogMessage) {
        delegate.info(message.simplified())
        onLog(INFO, message)
    }

    override fun warn(message: LogMessage) {
        delegate.warn(message.simplified())
        onLog(WARN, message)
    }

    override fun error(message: LogMessage) {
        delegate.error(message.simplified())
        onLog(ERROR, message)
    }
}

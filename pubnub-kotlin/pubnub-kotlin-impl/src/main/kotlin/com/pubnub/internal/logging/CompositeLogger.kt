package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.logging.networkLogging.simplified
import org.slf4j.Logger
import org.slf4j.event.Level

/**
 * Composite logger that delegates to multiple loggers.
 * Ensures all loggers receive the same messages.
 */
class CompositeLogger(
    private val slf4jLogger: Logger,
    private val location: String,
    private val pnInstanceId: String,
    private val toPortalLogger: PNLogger? = null,
    private val customLoggers: List<CustomLogger>? = null,
) : PNLogger {
    override fun trace(message: LogMessage) {
        val enhancedLogMessage = LogMessage(
            message = message.message,
            details = message.details,
            type = message.type,
            location = message.location ?: location,
            pubNubId = pnInstanceId,
            logLevel = Level.TRACE,
            timestamp = message.timestamp
        )
        slf4jLogger.trace(enhancedLogMessage.simplified())
        toPortalLogger?.trace(enhancedLogMessage)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, enhancedLogMessage)
        }
    }

    override fun debug(message: LogMessage) {
        val enhancedLogMessage = LogMessage(
            message = message.message,
            details = message.details,
            type = message.type,
            location = message.location ?: location,
            pubNubId = pnInstanceId,
            logLevel = Level.DEBUG,
            timestamp = message.timestamp
        )
        slf4jLogger.debug(enhancedLogMessage.simplified())
        toPortalLogger?.debug(enhancedLogMessage)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, enhancedLogMessage)
        }
    }

    override fun info(message: LogMessage) {
        val enhancedLogMessage = LogMessage(
            message = message.message,
            details = message.details,
            type = message.type,
            location = message.location ?: location,
            pubNubId = pnInstanceId,
            logLevel = Level.INFO,
            timestamp = message.timestamp
        )
        slf4jLogger.info(enhancedLogMessage.simplified())
        toPortalLogger?.info(enhancedLogMessage)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, enhancedLogMessage)
        }
    }

    override fun warn(message: LogMessage) {
        val enhancedLogMessage = LogMessage(
            message = message.message,
            details = message.details,
            type = message.type,
            location = message.location ?: location,
            pubNubId = pnInstanceId,
            logLevel = Level.WARN,
            timestamp = message.timestamp
        )
        slf4jLogger.warn(enhancedLogMessage.simplified())
        toPortalLogger?.warn(enhancedLogMessage)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, enhancedLogMessage)
        }
    }

    override fun error(message: LogMessage) {
        val enhancedLogMessage = LogMessage(
            message = message.message,
            details = message.details,
            type = message.type,
            location = message.location ?: location,
            pubNubId = pnInstanceId,
            logLevel = Level.ERROR,
            timestamp = message.timestamp
        )
        slf4jLogger.error(enhancedLogMessage.simplified())
        toPortalLogger?.error(enhancedLogMessage)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, enhancedLogMessage)
        }
    }

    override fun isDebugEnabled(): Boolean {
        return slf4jLogger.isDebugEnabled ||
            toPortalLogger?.isDebugEnabled() == true ||
            customLoggers?.any { isDebugEnabledSafely(it) } == true
    }

    private fun isDebugEnabledSafely(logger: CustomLogger): Boolean =
        runCatching { logger.isDebugEnabled() }
            .onFailure { reportCustomLoggerFailure(logger.name, "isDebugEnabled()", it) }
            .getOrDefault(true)

    private fun reportCustomLoggerFailure(loggerName: String, action: String, t: Throwable) {
        try {
            val logMessage = LogMessage(
                message = LogMessageContent.Error(
                    type = this::class.java.simpleName,
                    message = "Custom logger $loggerName $action failed: ${t.message}",
                    stack = null
                ),
                type = LogMessageType.ERROR,
                location = "CompositeLogger",
                pubNubId = pnInstanceId,
                logLevel = Level.ERROR
            )
            slf4jLogger.error(logMessage.simplified())
            toPortalLogger?.error(logMessage)
        } catch (_: Exception) {
            // Primary sinks themselves failed — there is nothing more we can safely do
            // without risking the caller's operation.
        }
    }

    private fun sendMessageToCustomLogger(logger: CustomLogger, message: LogMessage) {
        when (message.logLevel) {
            Level.TRACE -> {
                logger.trace(logMessage = message)
                logger.trace(message = message.simplified())
            }

            Level.DEBUG -> {
                logger.debug(logMessage = message)
                logger.debug(message = message.simplified())
            }

            Level.INFO -> {
                logger.info(logMessage = message)
                logger.info(message = message.simplified())
            }

            Level.WARN -> {
                logger.warn(logMessage = message)
                logger.warn(message = message.simplified())
            }

            Level.ERROR -> {
                logger.error(logMessage = message)
                logger.error(message = message.simplified())
            }

            else -> throw IllegalArgumentException("Unsupported log level: ${message.logLevel}")
        }
    }

    private fun delegateToCustomLoggers(action: (CustomLogger) -> Unit) {
        customLoggers?.forEach { logger ->
            try {
                action(logger)
            } catch (t: Throwable) {
                // A custom logger throwing must not crash the SDK call. Catch Throwable so
                // unchecked errors (LinkageError, AssertionError, etc.) from a misbehaving
                // logger cannot unwind through the SDK caller — e.g. aborting an in-flight
                // HTTP request from CustomPnHttpLoggingInterceptor purely because logging
                // failed. Report through primary sinks and continue with remaining loggers.
                reportCustomLoggerFailure(logger.name, "log dispatch", t)
            }
        }
    }
}

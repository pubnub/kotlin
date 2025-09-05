package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.ErrorDetails
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

    /**
     * Efficiently delegates to custom loggers with error handling.
     * Ensures one failing logger doesn't affect others.
     */
    private inline fun delegateToCustomLoggers(action: (CustomLogger) -> Unit) {
        customLoggers?.forEach { logger ->
            try {
                action(logger)
            } catch (e: Exception) {
                // Log through primary logger if custom logger fails
                // but don't let it crash the logging system
                try {
                    val logMessage = LogMessage(
                        message = LogMessageContent.Error(
                            ErrorDetails(
                                type = this::class.java.simpleName,
                                message = "Custom logger ${logger.name} failed: ${e.message}",
                            )
                        ),
                        type = LogMessageType.ERROR,
                        location = "CompositeLogger",
                        pubNubId = pnInstanceId,
                        logLevel = Level.ERROR
                    )
                    slf4jLogger.error(logMessage.simplified())
                    toPortalLogger?.error(logMessage)
                } catch (ignored: Exception) {
                    // If even primary logger fails, there's nothing more we can do
                    // Don't let logging crash the application
                }
            }
        }
    }
}

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
    private val toPortalLogger: ExtendedLogger? = null,
    private val customLoggers: List<CustomLogger>? = null,
    private val pnInstanceId: String? = null,
) : ExtendedLogger {
    override fun trace(message: LogMessage) {
        slf4jLogger.trace(message.simplified())
        toPortalLogger?.trace(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, message)
        }
    }

    override fun debug(message: LogMessage) {
        slf4jLogger.debug(message.simplified())
        toPortalLogger?.debug(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, message)
        }
    }

    override fun info(message: LogMessage) {
        slf4jLogger.info(message.simplified())
        toPortalLogger?.info(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, message)
        }
    }

    override fun warn(message: LogMessage) {
        slf4jLogger.warn(message.simplified())
        toPortalLogger?.warn(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, message)
        }
    }

    override fun error(message: LogMessage) {
        slf4jLogger.error(message.simplified())
        toPortalLogger?.error(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            sendMessageToCustomLogger(logger, message)
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
                        pubNubId = pnInstanceId ?: "unknown-instanceId",
                        logLevel = Level.WARN,
                        location = "CompositeLogger",
                        type = LogMessageType.ERROR,
                        message = LogMessageContent.Text(
                            "Custom logger ${logger.name} failed: ${e.message}"
                        )
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

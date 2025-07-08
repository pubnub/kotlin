package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.logging.networkLogging.simplified
import org.slf4j.event.Level

/**
 * Composite logger that delegates to multiple loggers.
 * Ensures all loggers receive the same messages.
 */
class CompositeLogger(
    private val primaryLogger: ExtendedLogger,
    private val logConfig: LogConfig
) : ExtendedLogger by primaryLogger {
    override fun trace(message: LogMessage) {
        primaryLogger.trace(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            logger.trace(logMessage = message)
            logger.trace(message = message.simplified())
        }
    }

    override fun debug(message: LogMessage) {
        println("-= about to log primaryLogger debug") // todo remove
        primaryLogger.debug(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            println("-= about to log customLogger.debug(message)") // todo remove
            logger.debug(logMessage = message)
            println("-= about to log customLogger.debug(message.simplified())") // todo remove
            logger.debug(message = message.simplified())
        }
    }

    override fun info(message: LogMessage) {
        println("-= about to log primaryLogger info") // todo remove
        primaryLogger.info(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            println("-= about to log customLogger.info(message)") // todo remove
            logger.info(logMessage = message)
            println("-= about to log customLogger.info(message.simplified())") // todo remove
            logger.info(message = message.simplified())
        }
    }

    override fun warn(message: LogMessage) {
        primaryLogger.warn(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            logger.warn(logMessage = message)
            logger.warn(message = message.simplified())
        }
    }

    override fun error(message: LogMessage) {
        primaryLogger.error(message)
        delegateToCustomLoggers { logger: CustomLogger ->
            logger.error(logMessage = message)
            logger.error(message = message.simplified())
        }
    }

    /**
     * Efficiently delegates to custom loggers with error handling.
     * Ensures one failing logger doesn't affect others.
     */
    private inline fun delegateToCustomLoggers(action: (CustomLogger) -> Unit) {
        val customLoggers = logConfig.customLoggers
        if (!customLoggers.isNullOrEmpty()) { // todo check
            customLoggers.forEach { logger ->
                try {
                    action(logger)
                } catch (e: Exception) {
                    // Log through primary logger if custom logger fails
                    // but don't let it crash the logging system
                    try {
                        primaryLogger.warn(
                            LogMessage(
                                pubNubId = logConfig.pnInstanceId,
                                logLevel = Level.WARN,
                                location = "CompositeLogger",
                                type = LogMessageType.ERROR,
                                message = LogMessageContent.Text(
                                    "Custom logger ${logger.name} failed: ${e.message}"
                                )
                            )
                        )
                    } catch (ignored: Exception) {
                        // If even primary logger fails, there's nothing more we can do
                        // Don't let logging crash the application
                    }
                }
            }
        }
    }
}

package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.internal.logging.networkLogging.simplified

/**
 * Composite logger that delegates to multiple loggers.
 * Ensures all loggers receive the same messages.
 */
class CompositeLogger(
    private val primaryLogger: ExtendedLogger,
    private val customLoggers: List<CustomLogger>
) : ExtendedLogger by primaryLogger {
    override fun trace(message: LogMessage) {
        primaryLogger.trace(message)
        customLoggers.forEach { customLogger ->
            customLogger.trace(message)
            customLogger.trace(message.simplified())
        }
    }

    override fun debug(message: LogMessage) {
        println("-= about to log primaryLogger debug") // todo remove
        primaryLogger.debug(message)
        customLoggers.forEach { customLogger ->
            println("-= about to log customLogger.debug(message)") // todo remove
            customLogger.debug(message)
            println("-= about to log customLogger.debug(message.simplified())") // todo remove
            customLogger.debug(message.simplified())
        }
    }

    override fun info(message: LogMessage) {
        println("-= about to log primaryLogger info") // todo remove
        primaryLogger.info(message)
        customLoggers.forEach { customLogger ->
            println("-= about to log customLogger.info(message)") // todo remove
            customLogger.info(message)
            println("-= about to log customLogger.info(message.simplified())") // todo remove
            customLogger.info(message.simplified())
        }
    }

    override fun warn(message: LogMessage) {
        primaryLogger.warn(message)
        customLoggers.forEach { customLogger ->
            customLogger.warn(message)
            customLogger.warn(message.simplified())
        }
    }

    override fun error(message: LogMessage) {
        primaryLogger.error(message)
        customLoggers.forEach { customLogger ->
            customLogger.error(message)
            customLogger.error(message.simplified())
        }
    }
}

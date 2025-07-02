package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage

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
            customLogger.trace(getSimplifiedMessage(message))
        }
    }

    override fun debug(message: LogMessage) {
        primaryLogger.debug(message)
        customLoggers.forEach { customLogger ->
            customLogger.debug(message)
            customLogger.debug(getSimplifiedMessage(message))
        }
    }

    override fun info(message: LogMessage) {
        primaryLogger.info(message)
        customLoggers.forEach { customLogger ->
            customLogger.info(message)
            customLogger.info(getSimplifiedMessage(message))
        }
    }

    override fun warn(message: LogMessage) {
        primaryLogger.warn(message)
        customLoggers.forEach { customLogger ->
            customLogger.warn(message)
            customLogger.warn(getSimplifiedMessage(message))
        }
    }

    override fun error(message: LogMessage) {
        primaryLogger.error(message)
        customLoggers.forEach { customLogger ->
            customLogger.error(message)
            customLogger.error(getSimplifiedMessage(message))
        }
    }



    // todo implement other Logger methods as needed.
}

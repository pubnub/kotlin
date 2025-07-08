package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger

/**
 * Manages logger creation ensuring InMemoryLoggerAndToPortalSender always works.
 */
object LoggerManager {
    fun getLogger(logConfig: LogConfig, clazz: Class<*>): ExtendedLogger {
        val slf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz)

        val toPortalLogger: ExtendedLogger = ToPortalLogger(
            delegate = slf4jLogger,
            userId = logConfig.userId
        )

        val customLoggers: List<CustomLogger>? = logConfig.customLoggers
        return if (!customLoggers.isNullOrEmpty()) {
            CompositeLogger(toPortalLogger, customLoggers)
        } else {
            toPortalLogger
        }
    }
}

package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger

/**
 * Manages logger creation ensuring InMemoryLoggerAndToPortalSender always works.
 */
object LoggerManager {
    fun getLogger(logConfig: LogConfig, clazz: Class<*>): ExtendedLogger {
        val slf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz)

        val logMessageContext = LogMessageContext(
            pnInstanceId = logConfig.pnInstanceId,
            userId = logConfig.userId,
        )

        val inMemoryLogger: ExtendedLogger = LoggerToPortalSender(
            slf4jLogger = slf4jLogger,
            logMessageContext = logMessageContext
        )

        val customLoggers: List<CustomLogger>? = logConfig.customLoggers
        return if (customLoggers != null) { // todo add check if list is not empty
            CompositeLogger(inMemoryLogger, customLoggers)
        } else {
            inMemoryLogger
        }
    }
}

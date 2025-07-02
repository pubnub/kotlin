package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger


/**
 * Manages logger creation ensuring InMemoryLoggerAndToPortalSender always works.
 */
object LoggerManager {
    //    fun getLogger(pubNub: PubNub, clazz: Class<*>): Logger {
//    fun getLogger(pubNub: PubNub, clazz: Class<*>): ExtendedLogger {
    fun getLogger(logConfig: LogConfig, clazz: Class<*>): ExtendedLogger {
        val slf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz)

        val logMessageContext = LogMessageContext(
            pnInstanceId = logConfig.pnInstanceId,
            userId = logConfig.userId,
        )

        // Always create the InMemoryLoggerAndToPortalSender
        val inMemoryLogger: ExtendedLogger = LoggerToPortalSender(
            slf4jLogger = slf4jLogger, logMessageContext = logMessageContext
        )

        // If user provided a custom logger factory, create a composite logger
        val customLoggers: List<CustomLogger>? = logConfig.customLoggers
        return if (customLoggers != null) {
            CompositeLogger(inMemoryLogger, customLoggers)
        } else {
            // Only use InMemoryLoggerAndToPortalSender
            inMemoryLogger
        }
    }
}
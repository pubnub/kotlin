package com.pubnub.internal.logging

import org.slf4j.Logger

/**
 * Manages logger creation with dependency injection support for better testability.
 * Ensures logging never fails the application by providing fallback mechanisms.
 */
class LoggerManager(
    private val loggerFactory: (Class<*>) -> Logger = { org.slf4j.LoggerFactory.getLogger(it) },
) {
    /**
     * Creates a logger for the given configuration and class.
     * Returns a fallback logger if creation fails to prevent application crashes.
     */
    fun getLogger(logConfig: LogConfig, clazz: Class<*>): ExtendedLogger {
        return try {
            createLogger(logConfig, clazz)
        } catch (e: Exception) {
            // Return fallback logger if creation fails
            createFallbackLogger(clazz, e)
        }
    }

    private fun createLogger(logConfig: LogConfig, clazz: Class<*>): ExtendedLogger {
        val slf4jLogger: Logger = loggerFactory(clazz)
        val toPortalLogger = ToPortalLogger(userId = logConfig.userId)
        return CompositeLogger(slf4jLogger, toPortalLogger, logConfig.customLoggers, logConfig.pnInstanceId)
    }

    private fun createFallbackLogger(clazz: Class<*>, cause: Exception): ExtendedLogger {
        // Try to create a basic SLF4J logger as fallback
        return try {
            val fallbackSlf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz)
            fallbackSlf4jLogger.warn("Failed to create portal logger. Using fallback", cause)

            // Create a minimal logger
            CompositeLogger(fallbackSlf4jLogger)
        } catch (fallbackException: Exception) {
            // If even SLF4J fails, return no-op logger
            NoOpLogger(clazz)
        }
    }

    companion object {
        /**
         * Default instance for backwards compatibility.
         */
        @JvmStatic
        val instance = LoggerManager()
    }
}

/**
 * No-operation logger implementation used as last resort fallback.
 * Ensures logging calls never crash the application.
 */
private class NoOpLogger(private val clazz: Class<*>) : ExtendedLogger {
    override fun trace(message: com.pubnub.api.logging.LogMessage) {
        // No-op
    }

    override fun debug(message: com.pubnub.api.logging.LogMessage) {
        // No-op
    }

    override fun info(message: com.pubnub.api.logging.LogMessage) {
        // No-op
    }

    override fun warn(message: com.pubnub.api.logging.LogMessage) {
        // No-op
    }

    override fun error(message: com.pubnub.api.logging.LogMessage) {
        // Emergency fallback - print to System.err only in critical cases
        System.err.println("CRITICAL: Logger failure in ${clazz.simpleName}: ${message.message}")
    }
}

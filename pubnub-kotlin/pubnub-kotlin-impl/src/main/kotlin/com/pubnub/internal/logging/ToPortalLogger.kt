package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import org.slf4j.event.Level
import org.slf4j.event.Level.DEBUG
import org.slf4j.event.Level.ERROR
import org.slf4j.event.Level.INFO
import org.slf4j.event.Level.TRACE
import org.slf4j.event.Level.WARN

/**
 * Logger implementation that sends logs to a portal.
 */
class ToPortalLogger(
    private val userId: String,
) : PNLogger {
    override fun trace(message: LogMessage) {
        onLog(TRACE, message)
    }

    override fun debug(message: LogMessage) {
        onLog(DEBUG, message)
    }

    override fun info(message: LogMessage) {
        onLog(INFO, message)
    }

    override fun warn(message: LogMessage) {
        onLog(WARN, message)
    }

    override fun error(message: LogMessage) {
        onLog(ERROR, message)
    }

    private fun onLog(level: Level, message: LogMessage) {
        if (isLoggingToPortalEnabled() && shouldLogMessageForThisUser() && shouldLogMessageForThisLevel(level)) {
            // todo implement sending to portal
            // replace message content with calculated finger print using logic from ADR both for LogMessageType.OBJECT
            // that is used to log details about API calls and also in NetworkRequest, NetworkResponse
        }
    }

    private fun shouldLogMessageForThisLevel(level: Level): Boolean {
        val configuredLevel = getConfigFromPortal().logLevel
        return level.toInt() >= configuredLevel.toInt()
    }

    private fun isLoggingToPortalEnabled(): Boolean {
        return getConfigFromPortal().isLoggingEnabled
    }

    private fun shouldLogMessageForThisUser(): Boolean {
        return getConfigFromPortal().userId == userId
    }

    private fun getConfigFromPortal(): LogConfigFromPortal = portalConfig

    // Thread-safe lazy configuration loading
    private val portalConfig: LogConfigFromPortal by lazy {
        loadConfigFromPortal()
    }

    // todo when service available replace it with configu retrival from portal
    private fun loadConfigFromPortal(): LogConfigFromPortal {
        // This function should retrieve the logging configuration from the portal
        // For now, we return a default configuration
        return LogConfigFromPortal(
            isLoggingEnabled = false,
            logLevel = INFO,
            userId = "defaultUserId"
        )
    }
}

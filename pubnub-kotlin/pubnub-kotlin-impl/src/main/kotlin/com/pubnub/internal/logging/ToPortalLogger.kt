package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import com.pubnub.internal.logging.networkLogging.simplified
import com.pubnub.internal.managers.MapperManager
import org.slf4j.Logger
import org.slf4j.event.Level
import org.slf4j.event.Level.DEBUG
import org.slf4j.event.Level.ERROR
import org.slf4j.event.Level.INFO
import org.slf4j.event.Level.TRACE
import org.slf4j.event.Level.WARN

/**
 * Logger implementation that sends logs to a portal while delegating to a standard SLF4J logger based on customer
 * configuration of slf4j implementation e.g. logback.xml, log4j2.xml, etc.
 */
class ToPortalLogger(
    private val delegate: Logger,
    private val userId: String,
) : ExtendedLogger {
    // todo is logMessageContext needed here?
    // Delegate all Logger methods to the underlying SLF4J logger

    companion object {
        private const val PORTAL_CHANNEL = "pubnub-internal-logs"
    }

    /**
     * Called whenever a log message is processed.
     * Subclasses can override this to add custom behavior.
     */
    private fun onLog(level: Level, message: LogMessage) {
        if (isLoggingToPortalEnabled() && shouldLogMessageForThisUser() && shouldLogMessageForThisLevel(level)) {
            println("Logging to portal is enabled for user: ${getConfigFromPortal().userId} for level: $level")

            // format message.
            val messageAsJsonToBeSentToPortal: String = MapperManager().toJson(message)
            // ToDo
            // todo create separate publish method that doesn't have logging to avoid recursion or rather  in Retrofit Manger filter publish to Portal
            // pubNub.publish(...) or similar
        } else {
            println("Logging to portal is disabled for user: $userId") // todo remove
            // todo delete below

            // format message.
            val messageAsJsonToBeSentToPortal: String = MapperManager().toJson(message)

            println("\n-=ToPortalLogger message: $messageAsJsonToBeSentToPortal \n")
        }
    }

    override fun trace(message: LogMessage) {
        delegate.trace(message.simplified())
        onLog(TRACE, message)
    }

    override fun debug(message: LogMessage) {
        delegate.debug(message.simplified())
        onLog(DEBUG, message)
    }

    override fun info(message: LogMessage) {
        delegate.info(message.simplified())
        onLog(INFO, message)
    }

    override fun warn(message: LogMessage) {
        delegate.warn(message.simplified())
        onLog(WARN, message)
    }

    override fun error(message: LogMessage) {
        delegate.error(message.simplified())
        onLog(ERROR, message)
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

    // Thread-safe lazy configuration loading
    private val portalConfig: LogConfigFromPortal by lazy {
        loadConfigFromPortal()
    }

    private fun getConfigFromPortal(): LogConfigFromPortal = portalConfig

    private fun loadConfigFromPortal(): LogConfigFromPortal {
        // This function should retrieve the logging configuration from the portal
        // For now, we return a default configuration
        return LogConfigFromPortal(
            isLoggingEnabled = true,
            logLevel = Level.TRACE,
            userId = "defaultUserId"
        )
    }
}

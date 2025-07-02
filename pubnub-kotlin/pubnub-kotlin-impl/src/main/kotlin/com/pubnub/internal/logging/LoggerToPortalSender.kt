package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import com.pubnub.internal.managers.MapperManager
import org.slf4j.Logger
import org.slf4j.event.Level

class LoggerToPortalSender(
    slf4jLogger: Logger,
    logMessageContext: LogMessageContext
) : BaseDefaultLogger(logMessageContext) {

    companion object {
        private const val PORTAL_CHANNEL = "pubnub-internal-logs"
    }

    override val delegate: Logger = slf4jLogger

//    override val contextInfo: Map<String, String> = buildMap {
//        putAll(additionalContext)
//    }

    override fun onLog(level: Level, message: LogMessage) {
        if (isLoggingToPortalEnabled() && shouldLogMessageForThisUser() && shouldLogMessageForThisLevel(level)) {
            println("Logging to portal is enabled for user: ${getConfigFromPortal().userId} for level: $level")

            // format message.
            val messageAsJsonToBeSentToPortal: String = MapperManager().toJson(message)
            // ToDo
            // todo create separate publish method that doesn't have logging to avoid recursion or rather  in Retrofit Manger filter publish to Portal
            // pubNub.publish(...) or similar
        } else {
            println("Logging to portal is disabled for user: ${logMessageContext.userId}")
            // todo delete below

            // format message.
            val messageAsJsonToBeSentToPortal: String = MapperManager().toJson(message)

            println("\n-=Log message: $messageAsJsonToBeSentToPortal \n")
        }
    }

    private fun shouldLogMessageForThisLevel(level: Level): Boolean {
        return getConfigFromPortal().logLevel == level // todo when there is TRACE set in portal config then all level below should be logged
    }

    private fun isLoggingToPortalEnabled(): Boolean {
        return getConfigFromPortal().isLoggingEnabled
    }

    private fun shouldLogMessageForThisUser(): Boolean {
        return getConfigFromPortal().userId == logMessageContext.userId
    }

    private fun getConfigFromPortal(): LogConfigFromPortal {
        // This function should retrieve the logging configuration from the portal
        // For now, we return a default configuration
        return LogConfigFromPortal(
            isLoggingEnabled = true,
            logLevel = Level.TRACE,
            userId = "defaultUserId"
        )
    }
}

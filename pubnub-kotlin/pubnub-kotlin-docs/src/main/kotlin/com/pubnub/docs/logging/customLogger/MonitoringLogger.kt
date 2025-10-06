package com.pubnub.docs.logging.customlogger

// snippet.customLoggerImplementation

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType

class MonitoringLogger : CustomLogger {
    private val monitoringService: MonitoringService = MonitoringService()
    private val analyticsService: AnalyticsService = AnalyticsService()
    override val name = "MonitoringLogger"

    override fun error(logMessage: LogMessage) {
        // Access structured error data
        when (val content = logMessage.message) {
            is LogMessageContent.Error -> {
                // Send error details to monitoring service
                monitoringService.reportError(
                    message = content.message,
                    stackTrace = content.stack,
                    errorType = content.type,
                    instanceId = logMessage.pubNubId,
                    timestamp = logMessage.timestamp,
                    location = logMessage.location
                )
            }

            is LogMessageContent.NetworkRequest -> {
                // Track failed network requests
                val request = content
                if (request.failed) {
                    monitoringService.reportNetworkFailure(
                        url = "${request.origin}${request.path}",
                        method = request.method.value,
                        instanceId = logMessage.pubNubId
                    )
                }
            }

            else -> {
                // Handle other content types as needed
            }
        }
    }

    override fun warn(message: String?) {
        // Simple string-based warning for deprecations
        message?.let { monitoringService.logWarning(it) }
    }

    override fun debug(logMessage: LogMessage) {
        // Log configuration changes
        if (logMessage.type == LogMessageType.OBJECT) {
            val content = logMessage.message as? LogMessageContent.Object
            content?.operation?.let { operation ->
                if (operation.contains("Configuration")) {
                    analyticsService.trackConfigChange(
                        instanceId = logMessage.pubNubId,
                        details = content.arguments
                    )
                }
            }
        }
    }

    // Other methods use default no-op implementation
}
// snippet.end

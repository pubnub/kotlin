package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.v2.PNConfiguration
import org.slf4j.event.Level

object ConfigurationLogger {
    fun logConfiguration(
        configuration: PNConfiguration,
        logger: ExtendedLogger,
        instanceId: String,
        className: Class<*>
    ) {
        val configSummary = mapOf(
            // Required parameters
            "userId" to configuration.userId.value,
            "subscribeKey" to configuration.subscribeKey,
            // Optional parameters
            "publishKey" to (configuration.publishKey.takeIf { it.isNotBlank() } ?: "not set"),
            "secretKey" to (configuration.secretKey.takeIf { it.isNotBlank() }?.let { "set: *****" } ?: "not set"),
            // Security and connection settings
            "secure" to configuration.secure,
            "origin" to (configuration.origin.takeIf { it.isNotBlank() } ?: "default"),
            "logVerbosity" to configuration.logVerbosity.name,
            "cacheBusting" to configuration.cacheBusting,
            // Timeout configurations
            "connectTimeout" to configuration.connectTimeout,
            "subscribeTimeout" to configuration.subscribeTimeout,
            "nonSubscribeReadTimeout" to configuration.nonSubscribeReadTimeout,
            // Presence and heartbeat settings
            "presenceTimeout" to configuration.presenceTimeout,
            "heartbeatInterval" to configuration.heartbeatInterval,
            "heartbeatNotificationOptions" to configuration.heartbeatNotificationOptions.name,
            "suppressLeaveEvents" to configuration.suppressLeaveEvents,
            "maintainPresenceState" to configuration.maintainPresenceState,
            // Authentication
            "authKey" to (configuration.authKey.takeIf { it.isNotBlank() }?.let { "set: *****" } ?: "not set"),
            "authToken" to (configuration.authToken?.takeIf { it.isNotBlank() }?.let { "(@Deprecated) set: *****" } ?: "not set"),
            // Filtering and subscriptions
            "filterExpression" to (configuration.filterExpression.takeIf { it.isNotBlank() } ?: "not set"),
            "dedupOnSubscribe" to (configuration.dedupOnSubscribe ?: "not set"),
            "maximumMessagesCacheSize" to (configuration.maximumMessagesCacheSize ?: "not set"),
            // Retry and reliability
            "retryConfiguration" to (configuration.retryConfiguration?.javaClass?.simpleName ?: "none"),
            "fileMessagePublishRetryLimit" to configuration.fileMessagePublishRetryLimit,
            // Identification and tracking
            "includeInstanceIdentifier" to configuration.includeInstanceIdentifier,
            "includeRequestIdentifier" to configuration.includeRequestIdentifier,
            "pnsdkSuffixes" to (configuration.pnsdkSuffixes?.takeIf { it.isNotEmpty() } ?: "not set"),
            // Crypto and encryption
            "cryptoModule" to (
                if (configuration.cryptoModule != null) {
                    "enabled"
                } else {
                    "disabled"
                }
            ),
            // Manual presence management
            "managePresenceListManually" to (configuration.managePresenceListManually ?: "not set"),
            // Logging configuration
            "customLoggers" to (
                configuration.customLoggers?.let {
                    "enabled (${it.size} logger${if (it.size != 1) {
                        "s"
                    } else {
                        ""
                    }})"
                } ?: "not set"
            ),
            // Network and proxy settings
            "proxy" to (configuration.proxy?.toString() ?: "not set"),
            "proxySelector" to (configuration.proxySelector?.toString() ?: "not set"),
            "proxyAuthenticator" to (configuration.proxyAuthenticator?.toString() ?: "not set"),
            "maximumConnections" to (configuration.maximumConnections?.toString() ?: "not set"),
            "httpLoggingInterceptor" to (configuration.httpLoggingInterceptor?.let { "(@Deprecated) enabled (${it.level})" } ?: "not set"),
            // SSL/TLS settings
            "sslSocketFactory" to (configuration.sslSocketFactory?.toString() ?: "not set"),
            "x509ExtendedTrustManager" to (configuration.x509ExtendedTrustManager?.toString() ?: "not set"),
            "connectionSpec" to (configuration.connectionSpec?.toString() ?: "not set"),
            "hostnameVerifier" to (configuration.hostnameVerifier?.toString() ?: "not set"),
            "certificatePinner" to (configuration.certificatePinner?.toString() ?: "not set"),
            // App Engine settings
            "googleAppEngineNetworking" to configuration.googleAppEngineNetworking,
            // Instance identification
            "instanceId" to instanceId
        )

        logger.debug(
            LogMessage(
                pubNubId = instanceId,
                logLevel = Level.DEBUG,
                location = className.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(message = configSummary),
                details = "Configuration logged"
            )
        )
    }
}

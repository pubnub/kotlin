package com.pubnub.internal.logging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.v2.PNConfiguration

private const val NOT_SET = "not set"

object ConfigurationLogger {
    fun logConfiguration(
        configuration: PNConfiguration,
        logger: PNLogger,
        instanceId: String,
        className: Class<*>
    ) {
        val configSummary = mapOf(
            // Required parameters
            "userId" to configuration.userId.value,
            "subscribeKey" to configuration.subscribeKey,
            // Optional parameters
            "publishKey" to (configuration.publishKey.takeIf { it.isNotBlank() } ?: NOT_SET),
            "secretKey" to (configuration.secretKey.takeIf { it.isNotBlank() }?.let { "set: *****" } ?: NOT_SET),
            // Security and connection settings
            "secure" to configuration.secure,
            "origin" to (configuration.origin.takeIf { it.isNotBlank() } ?: "default"),
            "logVerbosity" to (configuration.logVerbosity.takeIf { it == PNLogVerbosity.BODY }?.let { "(Deprecated) ${it.name}" } ?: NOT_SET),
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
            "authKey" to (configuration.authKey.takeIf { it.isNotBlank() }?.let { "(@Deprecated) set: *****" } ?: NOT_SET),
            "authToken" to (configuration.authToken?.takeIf { it.isNotBlank() }?.let { "set: *****" } ?: NOT_SET),
            // Filtering and subscriptions
            "filterExpression" to (configuration.filterExpression.takeIf { it.isNotBlank() } ?: NOT_SET),
            "dedupOnSubscribe" to configuration.dedupOnSubscribe,
            "maximumMessagesCacheSize" to configuration.maximumMessagesCacheSize,
            // Retry and reliability
            "retryConfiguration" to (configuration.retryConfiguration.javaClass.simpleName ?: "none"),
            "fileMessagePublishRetryLimit" to configuration.fileMessagePublishRetryLimit,
            // Identification and tracking
            "includeInstanceIdentifier" to configuration.includeInstanceIdentifier,
            "includeRequestIdentifier" to configuration.includeRequestIdentifier,
            "pnsdkSuffixes" to (configuration.pnsdkSuffixes.takeIf { it.isNotEmpty() } ?: NOT_SET),
            // Crypto and encryption
            "cryptoModule" to (
                if (configuration.cryptoModule != null) {
                    "enabled"
                } else {
                    "disabled"
                }
            ),
            // Manual presence management
            "managePresenceListManually" to (configuration.managePresenceListManually ?: NOT_SET),
            // Logging configuration
            "customLoggers" to (
                configuration.customLoggers?.let {
                    "enabled (${it.size} logger${if (it.size != 1) {
                        "s"
                    } else {
                        ""
                    }})"
                } ?: NOT_SET
            ),
            // Network and proxy settings
            "proxy" to (configuration.proxy?.toString() ?: NOT_SET),
            "proxySelector" to (configuration.proxySelector?.toString() ?: NOT_SET),
            "proxyAuthenticator" to (configuration.proxyAuthenticator?.toString() ?: NOT_SET),
            "maximumConnections" to (configuration.maximumConnections?.toString() ?: NOT_SET),
            "httpLoggingInterceptor" to (configuration.httpLoggingInterceptor?.let { "(@Deprecated) enabled (${it.level})" } ?: NOT_SET),
            // SSL/TLS settings
            "sslSocketFactory" to (configuration.sslSocketFactory?.toString() ?: NOT_SET),
            "x509ExtendedTrustManager" to (configuration.x509ExtendedTrustManager?.toString() ?: NOT_SET),
            "connectionSpec" to (configuration.connectionSpec?.toString() ?: NOT_SET),
            "hostnameVerifier" to (configuration.hostnameVerifier?.toString() ?: NOT_SET),
            "certificatePinner" to (configuration.certificatePinner?.toString() ?: NOT_SET),
            // App Engine settings
            "googleAppEngineNetworking" to configuration.googleAppEngineNetworking,
            // Instance identification
            "instanceId" to instanceId
        )

        logger.debug(
            LogMessage(
                message = LogMessageContent.Object(arguments = configSummary),
                details = "Configuration logged",
                location = className.toString(),
            )
        )
    }
}

package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.internal.crypto.CryptoModuleImpl
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

class PNConfigurationImpl(
    override val userId: UserId,
    override val subscribeKey: String = "",
    override val publishKey: String = "",
    override val secretKey: String = "",
    override val authKey: String = "",
    override val authToken: String? = null,
    override val cryptoModule: CryptoModule? = null, // don't use getter directly use getCryptoModuleWithLogConfig to be able to properly configure logging in CryptoModule
    override val origin: String = "",
    override val secure: Boolean = true,
    override val logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
    override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES,
    override val presenceTimeout: Int = PRESENCE_TIMEOUT,
    override val heartbeatInterval: Int = 0,
    override val subscribeTimeout: Int = SUBSCRIBE_TIMEOUT,
    override val connectTimeout: Int = CONNECT_TIMEOUT,
    override val nonSubscribeReadTimeout: Int = NON_SUBSCRIBE_REQUEST_TIMEOUT,
    override val cacheBusting: Boolean = false,
    override val suppressLeaveEvents: Boolean = false,
    override val maintainPresenceState: Boolean = true,
    override val filterExpression: String = "",
    override val includeInstanceIdentifier: Boolean = false,
    override val includeRequestIdentifier: Boolean = true,
    override val maximumConnections: Int? = null,
    override val connectionPoolMaxIdleConnections: Int = CONNECTION_POOL_MAX_IDLE_CONNECTIONS,
    override val connectionPoolKeepAliveDuration: Int = CONNECTION_POOL_KEEP_ALIVE_DURATION,
    override val googleAppEngineNetworking: Boolean = false,
    override val proxy: Proxy? = null,
    override val proxySelector: ProxySelector? = null,
    override val proxyAuthenticator: Authenticator? = null,
    override val certificatePinner: CertificatePinner? = null,
    override val httpLoggingInterceptor: HttpLoggingInterceptor? = null,
    override val sslSocketFactory: SSLSocketFactory? = null,
    override val x509ExtendedTrustManager: X509ExtendedTrustManager? = null,
    override val connectionSpec: ConnectionSpec? = null,
    override val hostnameVerifier: HostnameVerifier? = null,
    override val fileMessagePublishRetryLimit: Int = 0,
    override val dedupOnSubscribe: Boolean = false,
    override val maximumMessagesCacheSize: Int = DEFAULT_DEDUPE_SIZE,
    override val pnsdkSuffixes: Map<String, String> = emptyMap(),
    override val retryConfiguration: RetryConfiguration = RetryConfiguration.Exponential(
        excludedOperations = listOf(
            RetryableEndpointGroup.PUBLISH,
            RetryableEndpointGroup.PRESENCE,
            RetryableEndpointGroup.FILE_PERSISTENCE,
            RetryableEndpointGroup.MESSAGE_PERSISTENCE,
            RetryableEndpointGroup.CHANNEL_GROUP,
            RetryableEndpointGroup.PUSH_NOTIFICATION,
            RetryableEndpointGroup.APP_CONTEXT,
            RetryableEndpointGroup.MESSAGE_REACTION,
            RetryableEndpointGroup.ACCESS_MANAGER,
        )
    ),
    override val managePresenceListManually: Boolean = false,
    override val customLoggers: List<CustomLogger>? = null,
) : PNConfiguration, PNConfigurationOverride {
    companion object {
        const val DEFAULT_DEDUPE_SIZE = 100
        const val PRESENCE_TIMEOUT = 300
        const val MINIMUM_PRESENCE_TIMEOUT = 20
        const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        const val SUBSCRIBE_TIMEOUT = 310
        const val CONNECT_TIMEOUT = 5
        const val CONNECTION_POOL_MAX_IDLE_CONNECTIONS = 5 // OkHttp default
        const val CONNECTION_POOL_KEEP_ALIVE_DURATION = 300 // 5 minutes, OkHttp default
    }

    fun getCryptoModuleWithLogConfig(logConfig: LogConfig): CryptoModule? {
        return cryptoModule?.let { module ->
            if (module is CryptoModuleImpl) {
                CryptoModuleImpl(
                    primaryCryptor = module.primaryCryptor,
                    cryptorsForDecryptionOnly = module.cryptorsForDecryptionOnly,
                    logConfig = logConfig
                )
            } else {
                // For custom implementations, return the original instance
                module
            }
        }
    }

    class Builder(defaultConfiguration: PNConfiguration) :
        PNConfiguration.Builder,
        PNConfigurationOverride.Builder {
        constructor(userId: UserId, subscribeKey: String) : this(PNConfigurationImpl(userId, subscribeKey))

        override var userId: UserId = defaultConfiguration.userId

        override var subscribeKey: String = defaultConfiguration.subscribeKey

        override var publishKey: String = defaultConfiguration.publishKey

        override var secretKey: String = defaultConfiguration.secretKey

        override var authKey: String = defaultConfiguration.authKey

        override var authToken: String? = defaultConfiguration.authToken

        override var cryptoModule: CryptoModule? = defaultConfiguration.cryptoModule

        override var origin: String = defaultConfiguration.origin

        override var secure: Boolean = defaultConfiguration.secure

        @Deprecated(
            message = "LogVerbosity setting is deprecated and will be removed in future versions. " +
                "For logging configuration:\n" +
                "1. Use an SLF4J implementation (recommended)\n" +
                "2. Or implement CustomLogger interface and set via customLoggers property. " +
                "Use CustomLogger if your slf4j implementation like logback, log4j2, etc. can't meet " +
                "your specific logging requirements.",
            level = DeprecationLevel.WARNING
        )
        override var logVerbosity: PNLogVerbosity = defaultConfiguration.logVerbosity

        override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions = defaultConfiguration.heartbeatNotificationOptions

        override var presenceTimeout: Int = defaultConfiguration.presenceTimeout
            set(value) {
                field =
                    if (value < MINIMUM_PRESENCE_TIMEOUT) {
                        println("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                        MINIMUM_PRESENCE_TIMEOUT
                    } else {
                        value
                    }
                heartbeatInterval = (presenceTimeout / 2) - 1
            }

        override var heartbeatInterval: Int = defaultConfiguration.heartbeatInterval

        override var subscribeTimeout: Int = defaultConfiguration.subscribeTimeout

        override var connectTimeout: Int = defaultConfiguration.connectTimeout

        @Deprecated(
            "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
            replaceWith = ReplaceWith("nonSubscribeReadTimeout")
        )
        override var nonSubscribeRequestTimeout: Int
            get() = nonSubscribeReadTimeout
            set(value) {
                nonSubscribeReadTimeout = value
            }

        override var nonSubscribeReadTimeout: Int = defaultConfiguration.nonSubscribeReadTimeout

        override var cacheBusting: Boolean = defaultConfiguration.cacheBusting

        override var suppressLeaveEvents: Boolean = defaultConfiguration.suppressLeaveEvents

        override var maintainPresenceState: Boolean = defaultConfiguration.maintainPresenceState

        override var filterExpression: String = defaultConfiguration.filterExpression

        override var includeInstanceIdentifier: Boolean = defaultConfiguration.includeInstanceIdentifier

        override var includeRequestIdentifier: Boolean = defaultConfiguration.includeRequestIdentifier

        override var maximumConnections: Int? = defaultConfiguration.maximumConnections

        override var connectionPoolMaxIdleConnections: Int = defaultConfiguration.connectionPoolMaxIdleConnections

        override var connectionPoolKeepAliveDuration: Int = defaultConfiguration.connectionPoolKeepAliveDuration

        override var googleAppEngineNetworking: Boolean = defaultConfiguration.googleAppEngineNetworking

        override var proxy: Proxy? = defaultConfiguration.proxy

        override var proxySelector: ProxySelector? = defaultConfiguration.proxySelector

        override var proxyAuthenticator: Authenticator? = defaultConfiguration.proxyAuthenticator

        override var certificatePinner: CertificatePinner? = defaultConfiguration.certificatePinner

        @Deprecated(
            message = "This setting is deprecated. Use customLoggers instead.",
            level = DeprecationLevel.WARNING
        )
        override var httpLoggingInterceptor: HttpLoggingInterceptor? = defaultConfiguration.httpLoggingInterceptor

        override var sslSocketFactory: SSLSocketFactory? = defaultConfiguration.sslSocketFactory

        override var x509ExtendedTrustManager: X509ExtendedTrustManager? = defaultConfiguration.x509ExtendedTrustManager

        override var connectionSpec: ConnectionSpec? = defaultConfiguration.connectionSpec

        override var hostnameVerifier: HostnameVerifier? = defaultConfiguration.hostnameVerifier

        @Deprecated(
            message = "This setting is deprecated. Use retryConfiguration instead e.g. retryConfiguration = RetryConfiguration.Linear()",
            level = DeprecationLevel.WARNING
        )
        override var fileMessagePublishRetryLimit: Int = defaultConfiguration.fileMessagePublishRetryLimit
        override var dedupOnSubscribe: Boolean = defaultConfiguration.dedupOnSubscribe
        override var maximumMessagesCacheSize: Int = defaultConfiguration.maximumMessagesCacheSize
        override var pnsdkSuffixes: Map<String, String> = defaultConfiguration.pnsdkSuffixes

        override var retryConfiguration: RetryConfiguration = defaultConfiguration.retryConfiguration

        override var managePresenceListManually: Boolean = defaultConfiguration.managePresenceListManually

        override var customLoggers: List<CustomLogger>? = defaultConfiguration.customLoggers

        override fun build(): PNConfiguration {
            return PNConfigurationImpl(
                userId = userId,
                subscribeKey = subscribeKey,
                publishKey = publishKey,
                secretKey = secretKey,
                authKey = authKey,
                authToken = authToken,
                cryptoModule = cryptoModule,
                origin = origin,
                secure = secure,
                logVerbosity = logVerbosity,
                heartbeatNotificationOptions = heartbeatNotificationOptions,
                presenceTimeout = presenceTimeout,
                heartbeatInterval = heartbeatInterval,
                subscribeTimeout = subscribeTimeout,
                connectTimeout = connectTimeout,
                nonSubscribeReadTimeout = nonSubscribeReadTimeout,
                cacheBusting = cacheBusting,
                suppressLeaveEvents = suppressLeaveEvents,
                maintainPresenceState = maintainPresenceState,
                filterExpression = filterExpression,
                includeInstanceIdentifier = includeInstanceIdentifier,
                includeRequestIdentifier = includeRequestIdentifier,
                maximumConnections = maximumConnections,
                connectionPoolMaxIdleConnections = connectionPoolMaxIdleConnections,
                connectionPoolKeepAliveDuration = connectionPoolKeepAliveDuration,
                googleAppEngineNetworking = googleAppEngineNetworking,
                proxy = proxy,
                proxySelector = proxySelector,
                proxyAuthenticator = proxyAuthenticator,
                certificatePinner = certificatePinner,
                httpLoggingInterceptor = httpLoggingInterceptor,
                sslSocketFactory = sslSocketFactory,
                x509ExtendedTrustManager = x509ExtendedTrustManager,
                connectionSpec = connectionSpec,
                hostnameVerifier = hostnameVerifier,
                fileMessagePublishRetryLimit = fileMessagePublishRetryLimit,
                dedupOnSubscribe = dedupOnSubscribe,
                maximumMessagesCacheSize = maximumMessagesCacheSize,
                pnsdkSuffixes = pnsdkSuffixes,
                retryConfiguration = retryConfiguration,
                managePresenceListManually = managePresenceListManually,
                customLoggers = customLoggers,
            )
        }
    }
}

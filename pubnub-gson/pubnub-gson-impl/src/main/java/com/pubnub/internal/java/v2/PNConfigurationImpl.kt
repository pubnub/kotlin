package com.pubnub.internal.java.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.java.v2.PNConfiguration
import com.pubnub.api.java.v2.PNConfigurationOverride
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
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
    override val authToken: String? = null, // this property is not used, user can't create configuration with authToken
    override val cryptoModule: CryptoModule? = null, // todo same as in kotlin
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
    override val fileMessagePublishRetryLimit: Int = 5,
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
) : PNConfiguration {
    companion object {
        const val DEFAULT_DEDUPE_SIZE = 100
        const val PRESENCE_TIMEOUT = 300
        const val MINIMUM_PRESENCE_TIMEOUT = 20
        const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        const val SUBSCRIBE_TIMEOUT = 310
        const val CONNECT_TIMEOUT = 5
    }

    class Builder internal constructor(defaultConfiguration: com.pubnub.api.v2.PNConfiguration) :
        PNConfiguration.Builder, PNConfigurationOverride.Builder {
            constructor(userId: UserId, subscribeKey: String) : this(PNConfigurationImpl(userId, subscribeKey))

//            private val log = LoggerFactory.getLogger(this::class.simpleName)

            override var userId: UserId = defaultConfiguration.userId

            override fun setUserId(userId: UserId): PNConfiguration.Builder {
                this.userId = userId
                return this
            }

            override var subscribeKey: String = defaultConfiguration.subscribeKey

            override fun subscribeKey(subscribeKey: String): PNConfiguration.Builder {
                this.subscribeKey = subscribeKey
                return this
            }

            override fun publishKey(publishKey: String): Builder {
                this.publishKey = publishKey
                return this
            }

            override var publishKey: String = defaultConfiguration.publishKey

            override fun secretKey(secretKey: String): Builder {
                this.secretKey = secretKey
                return this
            }

            override var secretKey: String = defaultConfiguration.secretKey

            override fun authKey(authKey: String): Builder {
                this.authKey = authKey
                return this
            }

            override var authKey: String = defaultConfiguration.authKey

            override fun cryptoModule(cryptoModule: CryptoModule?): Builder {
                this.cryptoModule = cryptoModule
                return this
            }

            override var cryptoModule: CryptoModule? = defaultConfiguration.cryptoModule

            override fun origin(origin: String): Builder {
                this.origin = origin
                return this
            }

            override var origin: String = defaultConfiguration.origin

            override fun secure(secure: Boolean): Builder {
                this.secure = secure
                return this
            }

            override var secure: Boolean = defaultConfiguration.secure

            override fun logVerbosity(logVerbosity: PNLogVerbosity): Builder {
                this.logVerbosity = logVerbosity
                return this
            }

            override var logVerbosity: PNLogVerbosity = defaultConfiguration.logVerbosity

            override fun heartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): Builder {
                this.heartbeatNotificationOptions = heartbeatNotificationOptions
                return this
            }

            override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions =
                defaultConfiguration.heartbeatNotificationOptions

            override fun presenceTimeout(presenceTimeout: Int): Builder {
                this.presenceTimeout = if (presenceTimeout < MINIMUM_PRESENCE_TIMEOUT) {
                    println("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                    MINIMUM_PRESENCE_TIMEOUT
                } else {
                    presenceTimeout
                }
                heartbeatInterval = (presenceTimeout / 2) - 1
                return this
            }

            override var presenceTimeout: Int = defaultConfiguration.presenceTimeout

            override fun heartbeatInterval(heartbeatInterval: Int): Builder {
                this.heartbeatInterval = heartbeatInterval
                return this
            }

            override var heartbeatInterval: Int = defaultConfiguration.heartbeatInterval

            override fun subscribeTimeout(subscribeTimeout: Int): Builder {
                this.subscribeTimeout = subscribeTimeout
                return this
            }

            override var subscribeTimeout: Int = defaultConfiguration.subscribeTimeout

            override fun connectTimeout(connectTimeout: Int): Builder {
                this.connectTimeout = connectTimeout
                return this
            }

            override var connectTimeout: Int = defaultConfiguration.connectTimeout

            @Deprecated(
                "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
                replaceWith = ReplaceWith("nonSubscribeReadTimeout")
            )
            override fun nonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): PNConfiguration.Builder {
                return this.nonSubscribeReadTimeout(nonSubscribeRequestTimeout)
            }

            override fun nonSubscribeReadTimeout(nonSubscribeReadTimeout: Int): Builder {
                this.nonSubscribeReadTimeout = nonSubscribeReadTimeout
                return this
            }

            override var nonSubscribeReadTimeout: Int = defaultConfiguration.nonSubscribeReadTimeout

            override fun cacheBusting(cacheBusting: Boolean): Builder {
                this.cacheBusting = cacheBusting
                return this
            }

            override var cacheBusting: Boolean = defaultConfiguration.cacheBusting

            override fun suppressLeaveEvents(suppressLeaveEvents: Boolean): Builder {
                this.suppressLeaveEvents = suppressLeaveEvents
                return this
            }

            override var suppressLeaveEvents: Boolean = defaultConfiguration.suppressLeaveEvents

            override fun maintainPresenceState(maintainPresenceState: Boolean): Builder {
                this.maintainPresenceState = maintainPresenceState
                return this
            }

            override var maintainPresenceState: Boolean = defaultConfiguration.maintainPresenceState

            override fun filterExpression(filterExpression: String): Builder {
                this.filterExpression = filterExpression
                return this
            }

            override var filterExpression: String = defaultConfiguration.filterExpression

            override fun includeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder {
                this.includeInstanceIdentifier = includeInstanceIdentifier
                return this
            }

            override var includeInstanceIdentifier: Boolean = defaultConfiguration.includeInstanceIdentifier

            override fun includeRequestIdentifier(includeRequestIdentifier: Boolean): Builder {
                this.includeRequestIdentifier = includeRequestIdentifier
                return this
            }

            override var includeRequestIdentifier: Boolean = defaultConfiguration.includeRequestIdentifier

            override fun maximumConnections(maximumConnections: Int?): Builder {
                this.maximumConnections = maximumConnections
                return this
            }

            override var maximumConnections: Int? = defaultConfiguration.maximumConnections

            override fun googleAppEngineNetworking(googleAppEngineNetworking: Boolean): Builder {
                this.googleAppEngineNetworking = googleAppEngineNetworking
                return this
            }

            override var googleAppEngineNetworking: Boolean = defaultConfiguration.googleAppEngineNetworking

            override fun proxy(proxy: Proxy?): Builder {
                this.proxy = proxy
                return this
            }

            override var proxy: Proxy? = defaultConfiguration.proxy

            override fun proxySelector(proxySelector: ProxySelector?): Builder {
                this.proxySelector = proxySelector
                return this
            }

            override var proxySelector: ProxySelector? = defaultConfiguration.proxySelector

            override fun proxyAuthenticator(proxyAuthenticator: Authenticator?): Builder {
                this.proxyAuthenticator = proxyAuthenticator
                return this
            }

            override var proxyAuthenticator: Authenticator? = defaultConfiguration.proxyAuthenticator

            override fun certificatePinner(certificatePinner: CertificatePinner?): Builder {
                this.certificatePinner = certificatePinner
                return this
            }

            override var certificatePinner: CertificatePinner? = defaultConfiguration.certificatePinner

            override fun httpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): Builder {
                this.httpLoggingInterceptor = httpLoggingInterceptor
                return this
            }

            override var httpLoggingInterceptor: HttpLoggingInterceptor? = defaultConfiguration.httpLoggingInterceptor

            override fun sslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder {
                this.sslSocketFactory = sslSocketFactory
                return this
            }

            override var sslSocketFactory: SSLSocketFactory? = defaultConfiguration.sslSocketFactory

            override fun x509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): Builder {
                this.x509ExtendedTrustManager = x509ExtendedTrustManager
                return this
            }

            override var x509ExtendedTrustManager: X509ExtendedTrustManager? = defaultConfiguration.x509ExtendedTrustManager

            override fun connectionSpec(connectionSpec: ConnectionSpec?): Builder {
                this.connectionSpec = connectionSpec
                return this
            }

            override var connectionSpec: ConnectionSpec? = defaultConfiguration.connectionSpec

            override fun hostnameVerifier(hostnameVerifier: HostnameVerifier?): Builder {
                this.hostnameVerifier = hostnameVerifier
                return this
            }

            override var hostnameVerifier: HostnameVerifier? = defaultConfiguration.hostnameVerifier

            override fun fileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): Builder {
                this.fileMessagePublishRetryLimit = fileMessagePublishRetryLimit
                return this
            }

            override var fileMessagePublishRetryLimit: Int = defaultConfiguration.fileMessagePublishRetryLimit

            override fun dedupOnSubscribe(dedupOnSubscribe: Boolean): Builder {
                this.dedupOnSubscribe = dedupOnSubscribe
                return this
            }

            override var dedupOnSubscribe: Boolean = defaultConfiguration.dedupOnSubscribe

            override fun maximumMessagesCacheSize(maximumMessagesCacheSize: Int): Builder {
                this.maximumMessagesCacheSize = maximumMessagesCacheSize
                return this
            }

            override var maximumMessagesCacheSize: Int = defaultConfiguration.maximumMessagesCacheSize

            override fun pnsdkSuffixes(pnsdkSuffixes: Map<String, String>): Builder {
                this.pnsdkSuffixes = pnsdkSuffixes
                return this
            }

            override var pnsdkSuffixes: Map<String, String> = defaultConfiguration.pnsdkSuffixes

            override fun retryConfiguration(retryConfiguration: RetryConfiguration): Builder {
                this.retryConfiguration = retryConfiguration
                return this
            }

            override var retryConfiguration: RetryConfiguration = defaultConfiguration.retryConfiguration

            override fun managePresenceListManually(managePresenceListManually: Boolean): Builder {
                this.managePresenceListManually = managePresenceListManually
                return this
            }

            override var managePresenceListManually: Boolean = defaultConfiguration.managePresenceListManually

            override fun customLoggers(customLoggers: List<CustomLogger>?): Builder {
                this.customLoggers = customLoggers
                return this
            }

            override var customLoggers: List<CustomLogger>? = defaultConfiguration.customLoggers

            override fun build(): PNConfiguration {
                return PNConfigurationImpl(
                    userId = userId,
                    subscribeKey = subscribeKey,
                    publishKey = publishKey,
                    secretKey = secretKey,
                    authKey = authKey,
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
                    customLoggers = customLoggers
                )
            }
        }
}

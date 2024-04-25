package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.subscriptions.ConversationContext
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

class PNConfigurationImpl(
    override val userId: UserId,
    override val subscribeKey: String,
    override val publishKey: String,
    override val secretKey: String,
    override val authKey: String,
    override val cryptoModule: CryptoModule?,
    override val origin: String,
    override val secure: Boolean,
    override val logVerbosity: PNLogVerbosity,
    override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
    override val presenceTimeout: Int,
    override val heartbeatInterval: Int,
    override val subscribeTimeout: Int,
    override val connectTimeout: Int,
    override val nonSubscribeReadTimeout: Int,
    override val cacheBusting: Boolean,
    override val suppressLeaveEvents: Boolean,
    override val maintainPresenceState: Boolean,
    override val filterExpression: String,
    override val includeInstanceIdentifier: Boolean,
    override val includeRequestIdentifier: Boolean,
    override val maximumConnections: Int?,
    override val googleAppEngineNetworking: Boolean,
    override val proxy: Proxy?,
    override val proxySelector: ProxySelector?,
    override val proxyAuthenticator: Authenticator?,
    override val certificatePinner: CertificatePinner?,
    override val httpLoggingInterceptor: HttpLoggingInterceptor?,
    override val sslSocketFactory: SSLSocketFactory?,
    override val x509ExtendedTrustManager: X509ExtendedTrustManager?,
    override val connectionSpec: ConnectionSpec?,
    override val hostnameVerifier: HostnameVerifier?,
    override val fileMessagePublishRetryLimit: Int,
    override val dedupOnSubscribe: Boolean,
    override val maximumMessagesCacheSize: Int,
    override val pnsdkSuffixes: Map<String, String>,
    override val retryConfiguration: RetryConfiguration,
    override val managePresenceListManually: Boolean,
    override val conversationContext: ConversationContext,
    override val apiKey: String?,
    override val aiProvider: String?,
    override val webHookUrl: String?,
    override val monitoredChannels: List<String>?,
) : BasePNConfigurationImpl(userId), PNConfiguration {
    class Builder internal constructor(defaultConfiguration: BasePNConfiguration) :
        BasePNConfigurationImpl.Builder(defaultConfiguration),
        PNConfiguration.Builder {
            private val log = LoggerFactory.getLogger(this::class.simpleName)

            override var userId: UserId = super.userId
                private set

            override fun setUserId(userId: UserId): PNConfiguration.Builder {
                this.userId = userId
                return this
            }

            override var subscribeKey: String = super.subscribeKey
                private set

            override fun subscribeKey(subscribeKey: String): PNConfiguration.Builder {
                this.subscribeKey = subscribeKey
                return this
            }

            override fun publishKey(publishKey: String): Builder {
                this.publishKey = publishKey
                return this
            }

            override var publishKey: String = super.publishKey
                private set

            override fun secretKey(secretKey: String): Builder {
                this.secretKey = secretKey
                return this
            }

            override var secretKey: String = super.secretKey
                private set

            override fun authKey(authKey: String): Builder {
                this.authKey = authKey
                return this
            }

            override var authKey: String = super.authKey
                private set

            override fun cryptoModule(cryptoModule: CryptoModule?): Builder {
                this.cryptoModule = cryptoModule
                return this
            }

            override var cryptoModule: CryptoModule? = super.cryptoModule
                private set

            override fun origin(origin: String): Builder {
                this.origin = origin
                return this
            }

            override var origin: String = super.origin
                private set

            override fun secure(secure: Boolean): Builder {
                this.secure = secure
                return this
            }

            override var secure: Boolean = super.secure
                private set

            override fun logVerbosity(logVerbosity: PNLogVerbosity): Builder {
                this.logVerbosity = logVerbosity
                return this
            }

            override var logVerbosity: PNLogVerbosity = super.logVerbosity
                private set

            override fun heartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): Builder {
                this.heartbeatNotificationOptions = heartbeatNotificationOptions
                return this
            }

            override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions = super.heartbeatNotificationOptions
                private set

            override fun presenceTimeout(presenceTimeout: Int): Builder {
                this.presenceTimeout = if (presenceTimeout < MINIMUM_PRESENCE_TIMEOUT) {
                    log.warn("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                    MINIMUM_PRESENCE_TIMEOUT
                } else {
                    presenceTimeout
                }
                heartbeatInterval = (presenceTimeout / 2) - 1
                return this
            }

            override var presenceTimeout: Int = super.presenceTimeout
                private set

            override fun heartbeatInterval(heartbeatInterval: Int): Builder {
                this.heartbeatInterval = heartbeatInterval
                return this
            }

            override var heartbeatInterval: Int = super.heartbeatInterval
                private set

            override fun subscribeTimeout(subscribeTimeout: Int): Builder {
                this.subscribeTimeout = subscribeTimeout
                return this
            }

            override var subscribeTimeout: Int = super.subscribeTimeout
                private set

            override fun connectTimeout(connectTimeout: Int): Builder {
                this.connectTimeout = connectTimeout
                return this
            }

            override var connectTimeout: Int = super.connectTimeout
                private set

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

            override var nonSubscribeReadTimeout: Int = super.nonSubscribeReadTimeout
                private set

            override fun cacheBusting(cacheBusting: Boolean): Builder {
                this.cacheBusting = cacheBusting
                return this
            }

            override var cacheBusting: Boolean = super.cacheBusting
                private set

            override fun suppressLeaveEvents(suppressLeaveEvents: Boolean): Builder {
                this.suppressLeaveEvents = suppressLeaveEvents
                return this
            }

            override var suppressLeaveEvents: Boolean = super.suppressLeaveEvents
                private set

            override fun maintainPresenceState(maintainPresenceState: Boolean): Builder {
                this.maintainPresenceState = maintainPresenceState
                return this
            }

            override var maintainPresenceState: Boolean = super.maintainPresenceState
                private set

            override fun filterExpression(filterExpression: String): Builder {
                this.filterExpression = filterExpression
                return this
            }

            override var filterExpression: String = super.filterExpression
                private set

            override fun includeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder {
                this.includeInstanceIdentifier = includeInstanceIdentifier
                return this
            }

            override var includeInstanceIdentifier: Boolean = super.includeInstanceIdentifier
                private set

            override fun includeRequestIdentifier(includeRequestIdentifier: Boolean): Builder {
                this.includeRequestIdentifier = includeRequestIdentifier
                return this
            }

            override var includeRequestIdentifier: Boolean = super.includeRequestIdentifier
                private set

            override fun maximumConnections(maximumConnections: Int?): Builder {
                this.maximumConnections = maximumConnections
                return this
            }

            override var maximumConnections: Int? = super.maximumConnections
                private set

            override fun googleAppEngineNetworking(googleAppEngineNetworking: Boolean): Builder {
                this.googleAppEngineNetworking = googleAppEngineNetworking
                return this
            }

            override var googleAppEngineNetworking: Boolean = super.googleAppEngineNetworking
                private set

            override fun proxy(proxy: Proxy?): Builder {
                this.proxy = proxy
                return this
            }

            override var proxy: Proxy? = super.proxy
                private set

            override fun proxySelector(proxySelector: ProxySelector?): Builder {
                this.proxySelector = proxySelector
                return this
            }

            override var proxySelector: ProxySelector? = super.proxySelector
                private set

            override fun proxyAuthenticator(proxyAuthenticator: Authenticator?): Builder {
                this.proxyAuthenticator = proxyAuthenticator
                return this
            }

            override var proxyAuthenticator: Authenticator? = super.proxyAuthenticator
                private set

            override fun certificatePinner(certificatePinner: CertificatePinner?): Builder {
                this.certificatePinner = certificatePinner
                return this
            }

            override var certificatePinner: CertificatePinner? = super.certificatePinner
                private set

            override fun httpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): Builder {
                this.httpLoggingInterceptor = httpLoggingInterceptor
                return this
            }

            override var httpLoggingInterceptor: HttpLoggingInterceptor? = super.httpLoggingInterceptor
                private set

            override fun sslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder {
                this.sslSocketFactory = sslSocketFactory
                return this
            }

            override var sslSocketFactory: SSLSocketFactory? = super.sslSocketFactory
                private set

            override fun x509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): Builder {
                this.x509ExtendedTrustManager = x509ExtendedTrustManager
                return this
            }

            override var x509ExtendedTrustManager: X509ExtendedTrustManager? = super.x509ExtendedTrustManager
                private set

            override fun connectionSpec(connectionSpec: ConnectionSpec?): Builder {
                this.connectionSpec = connectionSpec
                return this
            }

            override var connectionSpec: ConnectionSpec? = super.connectionSpec
                private set

            override fun hostnameVerifier(hostnameVerifier: HostnameVerifier?): Builder {
                this.hostnameVerifier = hostnameVerifier
                return this
            }

            override var hostnameVerifier: HostnameVerifier? = super.hostnameVerifier
                private set

            override fun fileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): Builder {
                this.fileMessagePublishRetryLimit = fileMessagePublishRetryLimit
                return this
            }

            override var fileMessagePublishRetryLimit: Int = super.fileMessagePublishRetryLimit
                private set

            override fun dedupOnSubscribe(dedupOnSubscribe: Boolean): Builder {
                this.dedupOnSubscribe = dedupOnSubscribe
                return this
            }

            override var dedupOnSubscribe: Boolean = super.dedupOnSubscribe
                private set

            override fun maximumMessagesCacheSize(maximumMessagesCacheSize: Int): Builder {
                this.maximumMessagesCacheSize = maximumMessagesCacheSize
                return this
            }

            override var maximumMessagesCacheSize: Int = super.maximumMessagesCacheSize
                private set

            override fun pnsdkSuffixes(pnsdkSuffixes: Map<String, String>): Builder {
                this.pnsdkSuffixes = pnsdkSuffixes
                return this
            }

            override var pnsdkSuffixes: Map<String, String> = super.pnsdkSuffixes
                private set

            override fun retryConfiguration(retryConfiguration: RetryConfiguration): Builder {
                this.retryConfiguration = retryConfiguration
                return this
            }

            override var retryConfiguration: RetryConfiguration = super.retryConfiguration
                private set

            override fun managePresenceListManually(managePresenceListManually: Boolean): Builder {
                this.managePresenceListManually = managePresenceListManually
                return this
            }

            override var managePresenceListManually: Boolean = super.managePresenceListManually
                private set

            override fun conversationContext(conversationContext: ConversationContext): Builder {
                this.conversationContext = conversationContext
                return this
            }

            override var conversationContext: ConversationContext = super.conversationContext
                private set

            override fun apiKey(apiKey: String): Builder {
                this.apiKey = apiKey
                return this
            }

            override var apiKey: String? = super.apiKey
                private set

            override fun aiProvider(aiProvider: String): Builder {
                this.aiProvider = aiProvider
                return this
            }

            override var aiProvider: String? = super.aiProvider
                private set

            override fun webHookUrl(webHookUrl: String): Builder {
                this.webHookUrl = webHookUrl
                return this
            }

            override var webHookUrl: String? = super.webHookUrl
                private set

            override fun monitoredChannels(monitoredChannels: List<String>): Builder {
                this.monitoredChannels = monitoredChannels
                return this
            }

            override var monitoredChannels: List<String>? = super.monitoredChannels
                private set

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
                    conversationContext = conversationContext,
                    apiKey = apiKey,
                    aiProvider = aiProvider,
                    webHookUrl = webHookUrl,
                    monitoredChannels = monitoredChannels
                )
            }
        }
}

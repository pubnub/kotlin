package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.PNConfiguration
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
    override val nonSubscribeRequestTimeout: Int,
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

            /**
             * The subscribe key from the admin panel.
             */
            override fun setSubscribeKey(subscribeKey: String): PNConfiguration.Builder {
                this.subscribeKey = subscribeKey
                return this
            }

            /**
             * The publish key from the admin panel (only required if publishing).
             */
            override fun setPublishKey(publishKey: String): Builder {
                this.publishKey = publishKey
                return this
            }

            override var publishKey: String = super.publishKey
                private set

            /**
             * The secret key from the admin panel (only required for modifying/revealing access permissions).
             *
             * Keep away from Android.
             */
            override fun setSecretKey(secretKey: String): Builder {
                this.secretKey = secretKey
                return this
            }

            override var secretKey: String = super.secretKey
                private set

            /**
             * If Access Manager is utilized, client will use this authKey in all restricted requests.
             */
            override fun setAuthKey(authKey: String): Builder {
                this.authKey = authKey
                return this
            }

            override var authKey: String = super.authKey
                private set

            /**
             * CryptoModule is responsible for handling encryption and decryption.
             * If set, all communications to and from PubNub will be encrypted.
             */
            override fun setCryptoModule(cryptoModule: CryptoModule?): Builder {
                this.cryptoModule = cryptoModule
                return this
            }

            override var cryptoModule: CryptoModule? = super.cryptoModule
                private set

            /**
             * Custom origin if needed.
             *
             * Defaults to `ps.pndsn.com`
             */
            override fun setOrigin(origin: String): Builder {
                this.origin = origin
                return this
            }

            override var origin: String = super.origin
                private set

            /**
             * If set to `true`,  requests will be made over HTTPS.
             *
             * Deafults to `true`.
             */
            override fun setSecure(secure: Boolean): Builder {
                this.secure = secure
                return this
            }

            override var secure: Boolean = super.secure
                private set

            /**
             * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
             */
            override fun setLogVerbosity(logVerbosity: PNLogVerbosity): Builder {
                this.logVerbosity = logVerbosity
                return this
            }

            override var logVerbosity: PNLogVerbosity = super.logVerbosity
                private set

            /**
             * Set Heartbeat notification options.
             *
             * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
             */
            override fun setHeartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): Builder {
                this.heartbeatNotificationOptions = heartbeatNotificationOptions
                return this
            }

            override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions = super.heartbeatNotificationOptions
                private set

            /**
             * Sets the custom presence server timeout.
             *
             * The value is in seconds, and the minimum value is 20 seconds.
             *
             * Also sets the value of [heartbeatInterval]
             */
            override fun setPresenceTimeout(presenceTimeout: Int): Builder {
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

            /**
             * How often the client will announce itself to server.
             *
             * The value is in seconds.
             */
            override fun setHeartbeatInterval(heartbeatInterval: Int): Builder {
                this.heartbeatInterval = heartbeatInterval
                return this
            }

            override var heartbeatInterval: Int = super.heartbeatInterval
                private set

            /**
             * The subscribe request timeout.
             *
             * The value is in seconds.
             *
             * Defaults to 310.
             */
            override fun setSubscribeTimeout(subscribeTimeout: Int): Builder {
                this.subscribeTimeout = subscribeTimeout
                return this
            }

            override var subscribeTimeout: Int = super.subscribeTimeout
                private set

            /**
             * How long before the client gives up trying to connect with a subscribe call.
             *
             * The value is in seconds.
             *
             * Defaults to 5.
             */
            override fun setConnectTimeout(connectTimeout: Int): Builder {
                this.connectTimeout = connectTimeout
                return this
            }

            override var connectTimeout: Int = super.connectTimeout
                private set

            /**
             * For non subscribe operations (publish, herenow, etc)
             * how long to wait to connect to PubNub before giving up with a connection timeout error.
             *
             * The value is in seconds.
             *
             * Defaults to 10.
             */
            override fun setNonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): Builder {
                this.nonSubscribeRequestTimeout = nonSubscribeRequestTimeout
                return this
            }

            override var nonSubscribeRequestTimeout: Int = super.nonSubscribeRequestTimeout
                private set

            /**
             * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
             *
             * Defaults to `false`.
             */
            override fun setCacheBusting(cacheBusting: Boolean): Builder {
                this.cacheBusting = cacheBusting
                return this
            }

            override var cacheBusting: Boolean = super.cacheBusting
                private set

            /**
             * When `true` the SDK doesn't send out the leave requests.
             *
             * Defaults to `false`.
             */
            override fun setSuppressLeaveEvents(suppressLeaveEvents: Boolean): Builder {
                this.suppressLeaveEvents = suppressLeaveEvents
                return this
            }

            override var suppressLeaveEvents: Boolean = super.suppressLeaveEvents
                private set

            /**
             * When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState]
             * for the current [userId] with every automatic heartbeat (if [heartbeatInterval] is greater than 0)
             * and initial subscribe connection (also after e.g. loss of network).
             *
             * Applies only when [enableEventEngine] is true.
             *
             * Defaults to `true`.
             *
             * Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups.
             * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState]
             * otherwise that state may be overwritten by individual channel states.
             */
            override fun setMaintainPresenceState(maintainPresenceState: Boolean): Builder {
                this.maintainPresenceState = maintainPresenceState
                return this
            }

            override var maintainPresenceState: Boolean = super.maintainPresenceState
                private set

            /**
             * Feature to subscribe with a custom filter expression.
             */
            override fun setFilterExpression(filterExpression: String): Builder {
                this.filterExpression = filterExpression
                return this
            }

            override var filterExpression: String = super.filterExpression
                private set

            /**
             * Whether to include a [PubNubCore.instanceId] with every request.
             *
             * Defaults to `false`.
             */
            override fun setIncludeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder {
                this.includeInstanceIdentifier = includeInstanceIdentifier
                return this
            }

            override var includeInstanceIdentifier: Boolean = super.includeInstanceIdentifier
                private set

            /**
             * Whether to include a [PubNubCore.requestId] with every request.
             *
             * Defaults to `true`.
             */
            override fun setIncludeRequestIdentifier(includeRequestIdentifier: Boolean): Builder {
                this.includeRequestIdentifier = includeRequestIdentifier
                return this
            }

            override var includeRequestIdentifier: Boolean = super.includeRequestIdentifier
                private set

            /**
             * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
             */
            override fun setMaximumConnections(maximumConnections: Int?): Builder {
                this.maximumConnections = maximumConnections
                return this
            }

            override var maximumConnections: Int? = super.maximumConnections
                private set

            /**
             * Enable Google App Engine networking.
             *
             * Defaults to `false`.
             */
            override fun setGoogleAppEngineNetworking(googleAppEngineNetworking: Boolean): Builder {
                this.googleAppEngineNetworking = googleAppEngineNetworking
                return this
            }

            override var googleAppEngineNetworking: Boolean = super.googleAppEngineNetworking
                private set

            /**
             * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
             *
             * @see [Proxy]
             */
            override fun setProxy(proxy: Proxy?): Builder {
                this.proxy = proxy
                return this
            }

            override var proxy: Proxy? = super.proxy
                private set

            /**
             * @see [ProxySelector]
             */
            override fun setProxySelector(proxySelector: ProxySelector?): Builder {
                this.proxySelector = proxySelector
                return this
            }

            override var proxySelector: ProxySelector? = super.proxySelector
                private set

            /**
             * @see [Authenticator]
             */
            override fun setProxyAuthenticator(proxyAuthenticator: Authenticator?): Builder {
                this.proxyAuthenticator = proxyAuthenticator
                return this
            }

            override var proxyAuthenticator: Authenticator? = super.proxyAuthenticator
                private set

            /**
             * @see [CertificatePinner]
             */
            override fun setCertificatePinner(certificatePinner: CertificatePinner?): Builder {
                this.certificatePinner = certificatePinner
                return this
            }

            override var certificatePinner: CertificatePinner? = super.certificatePinner
                private set

            /**
             * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
             *
             * @see [HttpLoggingInterceptor]
             */
            override fun setHttpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): Builder {
                this.httpLoggingInterceptor = httpLoggingInterceptor
                return this
            }

            override var httpLoggingInterceptor: HttpLoggingInterceptor? = super.httpLoggingInterceptor
                private set

            /**
             * @see [SSLSocketFactory]
             */
            override fun setSslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder {
                this.sslSocketFactory = sslSocketFactory
                return this
            }

            override var sslSocketFactory: SSLSocketFactory? = super.sslSocketFactory
                private set

            /**
             * @see [X509ExtendedTrustManager]
             */
            override fun setX509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): Builder {
                this.x509ExtendedTrustManager = x509ExtendedTrustManager
                return this
            }

            override var x509ExtendedTrustManager: X509ExtendedTrustManager? = super.x509ExtendedTrustManager
                private set

            /**
             * @see [okhttp3.ConnectionSpec]
             */
            override fun setConnectionSpec(connectionSpec: ConnectionSpec?): Builder {
                this.connectionSpec = connectionSpec
                return this
            }

            override var connectionSpec: ConnectionSpec? = super.connectionSpec
                private set

            /**
             * @see [javax.net.ssl.HostnameVerifier]
             */
            override fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?): Builder {
                this.hostnameVerifier = hostnameVerifier
                return this
            }

            override var hostnameVerifier: HostnameVerifier? = super.hostnameVerifier
                private set

            /**
             * How many times publishing file message should automatically retry before marking the action as failed
             *
             * Defaults to `5`
             */
            override fun setFileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): Builder {
                this.fileMessagePublishRetryLimit = fileMessagePublishRetryLimit
                return this
            }

            override var fileMessagePublishRetryLimit: Int = super.fileMessagePublishRetryLimit
                private set

            override fun setDedupOnSubscribe(dedupOnSubscribe: Boolean): Builder {
                this.dedupOnSubscribe = dedupOnSubscribe
                return this
            }

            override var dedupOnSubscribe: Boolean = super.dedupOnSubscribe
                private set

            override fun setMaximumMessagesCacheSize(maximumMessagesCacheSize: Int): Builder {
                this.maximumMessagesCacheSize = maximumMessagesCacheSize
                return this
            }

            override var maximumMessagesCacheSize: Int = super.maximumMessagesCacheSize
                private set

            override fun setPnsdkSuffixes(pnsdkSuffixes: Map<String, String>): Builder {
                this.pnsdkSuffixes = pnsdkSuffixes
                return this
            }

            override var pnsdkSuffixes: Map<String, String> = super.pnsdkSuffixes
                private set

            /**
             * Retry configuration for requests.
             *  Defaults to [RetryConfiguration.None].
             *
             *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
             *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
             *  Delay will valy from provided value by random value.
             */
            override fun setRetryConfiguration(retryConfiguration: RetryConfiguration): Builder {
                this.retryConfiguration = retryConfiguration
                return this
            }

            override var retryConfiguration: RetryConfiguration = super.retryConfiguration
                private set

            /**
             * Enables explicit presence control.
             * When set to true heartbeat calls will contain only channels and groups added explicitly
             * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
             * For more information please contact PubNub support
             * @see PubNubCore.presence
             * @see BasePNConfigurationImpl.heartbeatInterval
             */
            override fun setManagePresenceListManually(managePresenceListManually: Boolean): Builder {
                this.managePresenceListManually = managePresenceListManually
                return this
            }

            override var managePresenceListManually: Boolean = super.managePresenceListManually
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
                    nonSubscribeRequestTimeout = nonSubscribeRequestTimeout,
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
                )
            }
        }
}

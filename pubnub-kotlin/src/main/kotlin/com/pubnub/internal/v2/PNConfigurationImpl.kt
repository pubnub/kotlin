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

            /**
             * The subscribe key from the admin panel.
             */
            override var subscribeKey: String = super.subscribeKey

            /**
             * The publish key from the admin panel (only required if publishing).
             */
            override var publishKey: String = super.publishKey

            /**
             * The secret key from the admin panel (only required for modifying/revealing access permissions).
             *
             * Keep away from Android.
             */
            override var secretKey: String = super.secretKey

            /**
             * If Access Manager is utilized, client will use this authKey in all restricted requests.
             */
            override var authKey: String = super.authKey

            /**
             * CryptoModule is responsible for handling encryption and decryption.
             * If set, all communications to and from PubNub will be encrypted.
             */
            override var cryptoModule: CryptoModule? = super.cryptoModule

            /**
             * Custom origin if needed.
             *
             * Defaults to `ps.pndsn.com`
             */
            override var origin: String = super.origin

            /**
             * If set to `true`,  requests will be made over HTTPS.
             *
             * Deafults to `true`.
             */
            override var secure: Boolean = super.secure

            /**
             * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
             */
            override var logVerbosity: PNLogVerbosity = super.logVerbosity

            /**
             * Set Heartbeat notification options.
             *
             * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
             */
            override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions = super.heartbeatNotificationOptions

            /**
             * Sets the custom presence server timeout.
             *
             * The value is in seconds, and the minimum value is 20 seconds.
             *
             * Also sets the value of [heartbeatInterval]
             */
            override var presenceTimeout: Int = super.presenceTimeout
                set(value) {
                    field =
                        if (value < MINIMUM_PRESENCE_TIMEOUT) {
                            log.warn("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                            MINIMUM_PRESENCE_TIMEOUT
                        } else {
                            value
                        }
                    heartbeatInterval = (presenceTimeout / 2) - 1
                }

            /**
             * How often the client will announce itself to server.
             *
             * The value is in seconds.
             */
            override var heartbeatInterval: Int = super.heartbeatInterval

            /**
             * The subscribe request timeout.
             *
             * The value is in seconds.
             *
             * Defaults to 310.
             */
            override var subscribeTimeout: Int = super.subscribeTimeout

            /**
             * How long before the client gives up trying to connect with a subscribe call.
             *
             * The value is in seconds.
             *
             * Defaults to 5.
             */
            override var connectTimeout: Int = super.connectTimeout

            /**
             * For non subscribe operations (publish, herenow, etc)
             * how long to wait to connect to PubNub before giving up with a connection timeout error.
             *
             * The value is in seconds.
             *
             * Defaults to 10.
             */
            override var nonSubscribeRequestTimeout: Int = super.nonSubscribeRequestTimeout

            /**
             * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
             *
             * Defaults to `false`.
             */
            override var cacheBusting: Boolean = super.cacheBusting

            /**
             * When `true` the SDK doesn't send out the leave requests.
             *
             * Defaults to `false`.
             */
            override var suppressLeaveEvents: Boolean = super.suppressLeaveEvents

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
            override var maintainPresenceState: Boolean = super.maintainPresenceState

            /**
             * Feature to subscribe with a custom filter expression.
             */
            override var filterExpression: String = super.filterExpression

            /**
             * Whether to include a [PubNubCore.instanceId] with every request.
             *
             * Defaults to `false`.
             */
            override var includeInstanceIdentifier: Boolean = super.includeInstanceIdentifier

            /**
             * Whether to include a [PubNubCore.requestId] with every request.
             *
             * Defaults to `true`.
             */
            override var includeRequestIdentifier: Boolean = super.includeRequestIdentifier

            /**
             * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
             */
            override var maximumConnections: Int? = super.maximumConnections

            /**
             * Enable Google App Engine networking.
             *
             * Defaults to `false`.
             */
            override var googleAppEngineNetworking: Boolean = super.googleAppEngineNetworking

            /**
             * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
             *
             * @see [Proxy]
             */
            override var proxy: Proxy? = super.proxy

            /**
             * @see [ProxySelector]
             */
            override var proxySelector: ProxySelector? = super.proxySelector

            /**
             * @see [Authenticator]
             */
            override var proxyAuthenticator: Authenticator? = super.proxyAuthenticator

            /**
             * @see [CertificatePinner]
             */
            override var certificatePinner: CertificatePinner? = super.certificatePinner

            /**
             * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
             *
             * @see [HttpLoggingInterceptor]
             */
            override var httpLoggingInterceptor: HttpLoggingInterceptor? = super.httpLoggingInterceptor

            /**
             * @see [SSLSocketFactory]
             */
            override var sslSocketFactory: SSLSocketFactory? = super.sslSocketFactory

            /**
             * @see [X509ExtendedTrustManager]
             */
            override var x509ExtendedTrustManager: X509ExtendedTrustManager? = super.x509ExtendedTrustManager

            /**
             * @see [okhttp3.ConnectionSpec]
             */
            override var connectionSpec: ConnectionSpec? = super.connectionSpec

            /**
             * @see [javax.net.ssl.HostnameVerifier]
             */
            override var hostnameVerifier: HostnameVerifier? = super.hostnameVerifier

            /**
             * How many times publishing file message should automatically retry before marking the action as failed
             *
             * Defaults to `5`
             */
            override var fileMessagePublishRetryLimit: Int = super.fileMessagePublishRetryLimit
            override var dedupOnSubscribe: Boolean = super.dedupOnSubscribe
            override var maximumMessagesCacheSize: Int = super.maximumMessagesCacheSize
            override var pnsdkSuffixes: Map<String, String> = super.pnsdkSuffixes

            /**
             * Retry configuration for requests.
             *  Defaults to [RetryConfiguration.None].
             *
             *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
             *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
             *  Delay will valy from provided value by random value.
             */
            override var retryConfiguration: RetryConfiguration = super.retryConfiguration

            /**
             * Enables explicit presence control.
             * When set to true heartbeat calls will contain only channels and groups added explicitly
             * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
             * For more information please contact PubNub support
             * @see PubNubCore.presence
             * @see BasePNConfigurationImpl.heartbeatInterval
             */
            override var managePresenceListManually: Boolean = super.managePresenceListManually

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

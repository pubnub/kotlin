package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

/**
 * A storage for user-provided information which describe further PubNub client behaviour.
 * Configuration instance contains additional set of properties which
 * allow performing precise PubNub client configuration.
 *
 */
open class BasePNConfigurationImpl internal constructor(
    override val userId: UserId,
    /**
     * The subscribe key from the admin panel.
     */
    override val subscribeKey: String = "",
    /**
     * The publish key from the admin panel (only required if publishing).
     */
    override val publishKey: String = "",
    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    override val secretKey: String = "",
    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    override val authKey: String = "",
    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    override val cryptoModule: CryptoModule? = null,
    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    override val origin: String = "",
    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    override val secure: Boolean = true,
    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    override val logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES,
    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    override val presenceTimeout: Int = PRESENCE_TIMEOUT,
    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    override val heartbeatInterval: Int = 0,
    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    override val subscribeTimeout: Int = SUBSCRIBE_TIMEOUT,
    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    override val connectTimeout: Int = CONNECT_TIMEOUT,
    /**
     * For non subscribe operations (publish, herenow, etc)
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    override val nonSubscribeRequestTimeout: Int = NON_SUBSCRIBE_REQUEST_TIMEOUT,
    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    override val cacheBusting: Boolean = false,
    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    override val suppressLeaveEvents: Boolean = false,
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
    override val maintainPresenceState: Boolean = true,
    /**
     * Feature to subscribe with a custom filter expression.
     */
    override val filterExpression: String = "",
    /**
     * Whether to include a [PubNubCore.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    override val includeInstanceIdentifier: Boolean = false,
    /**
     * Whether to include a [PubNubCore.requestId] with every request.
     *
     * Defaults to `true`.
     */
    override val includeRequestIdentifier: Boolean = true,
    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    override val maximumConnections: Int? = null,
    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    override val googleAppEngineNetworking: Boolean = false,
    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    override val proxy: Proxy? = null,
    /**
     * @see [ProxySelector]
     */
    override val proxySelector: ProxySelector? = null,
    /**
     * @see [Authenticator]
     */
    override val proxyAuthenticator: Authenticator? = null,
    /**
     * @see [CertificatePinner]
     */
    override val certificatePinner: CertificatePinner? = null,
    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    override val httpLoggingInterceptor: HttpLoggingInterceptor? = null,
    /**
     * @see [SSLSocketFactory]
     */
    override val sslSocketFactory: SSLSocketFactory? = null,
    /**
     * @see [X509ExtendedTrustManager]
     */
    override val x509ExtendedTrustManager: X509ExtendedTrustManager? = null,
    /**
     * @see [okhttp3.ConnectionSpec]
     */
    override val connectionSpec: ConnectionSpec? = null,
    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    override val hostnameVerifier: HostnameVerifier? = null,
    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    override val fileMessagePublishRetryLimit: Int = 5,
    override val dedupOnSubscribe: Boolean = false,
    override val maximumMessagesCacheSize: Int = DEFAULT_DEDUPE_SIZE,
    override val pnsdkSuffixes: Map<String, String> = emptyMap(),
    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will valy from provided value by random value.
     */
    override val retryConfiguration: RetryConfiguration = RetryConfiguration.None,
    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     * @see PubNubCore.presence
     * @see BasePNConfigurationImpl.heartbeatInterval
     */
    override val managePresenceListManually: Boolean = false,
) : BasePNConfiguration {
    companion object {
        const val DEFAULT_DEDUPE_SIZE = 100
        const val PRESENCE_TIMEOUT = 300
        const val MINIMUM_PRESENCE_TIMEOUT = 20
        const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        const val SUBSCRIBE_TIMEOUT = 310
        const val CONNECT_TIMEOUT = 5
    }

    constructor(
        userId: UserId,
    ) : this(userId, "")

    abstract class Builder(defaultConfiguration: BasePNConfiguration) : BasePNConfiguration.Builder {
        override val userId: UserId = defaultConfiguration.userId

        /**
         * The subscribe key from the admin panel.
         */
        override val subscribeKey: String = defaultConfiguration.subscribeKey

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        override val publishKey: String = defaultConfiguration.publishKey

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        override val secretKey: String = defaultConfiguration.secretKey

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        override val authKey: String = defaultConfiguration.authKey

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        override val cryptoModule: CryptoModule? = defaultConfiguration.cryptoModule

        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        override val origin: String = defaultConfiguration.origin

        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        override val secure: Boolean = defaultConfiguration.secure

        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        override val logVerbosity: PNLogVerbosity = defaultConfiguration.logVerbosity

        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions =
            defaultConfiguration.heartbeatNotificationOptions

        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         *
         * Also sets the value of [heartbeatInterval]
         */
        override val presenceTimeout: Int = defaultConfiguration.presenceTimeout

        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        override val heartbeatInterval: Int = defaultConfiguration.heartbeatInterval

        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        override val subscribeTimeout: Int = defaultConfiguration.subscribeTimeout

        /**
         * How long before the client gives up trying to connect with a subscribe call.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        override val connectTimeout: Int = defaultConfiguration.connectTimeout

        /**
         * For non subscribe operations (publish, herenow, etc)
         * how long to wait to connect to PubNub before giving up with a connection timeout error.
         *
         * The value is in seconds.
         *
         * Defaults to 10.
         */
        override val nonSubscribeRequestTimeout: Int = defaultConfiguration.nonSubscribeRequestTimeout

        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        override val cacheBusting: Boolean = defaultConfiguration.cacheBusting

        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        override val suppressLeaveEvents: Boolean = defaultConfiguration.suppressLeaveEvents

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
        override val maintainPresenceState: Boolean = defaultConfiguration.maintainPresenceState

        /**
         * Feature to subscribe with a custom filter expression.
         */
        override val filterExpression: String = defaultConfiguration.filterExpression

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        override val includeInstanceIdentifier: Boolean = defaultConfiguration.includeInstanceIdentifier

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        override val includeRequestIdentifier: Boolean = defaultConfiguration.includeRequestIdentifier

        /**
         * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
         */
        override val maximumConnections: Int? = defaultConfiguration.maximumConnections

        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        override val googleAppEngineNetworking: Boolean = defaultConfiguration.googleAppEngineNetworking

        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         *
         * @see [Proxy]
         */
        override val proxy: Proxy? = defaultConfiguration.proxy

        /**
         * @see [ProxySelector]
         */
        override val proxySelector: ProxySelector? = defaultConfiguration.proxySelector

        /**
         * @see [Authenticator]
         */
        override val proxyAuthenticator: Authenticator? = defaultConfiguration.proxyAuthenticator

        /**
         * @see [CertificatePinner]
         */
        override val certificatePinner: CertificatePinner? = defaultConfiguration.certificatePinner

        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         *
         * @see [HttpLoggingInterceptor]
         */
        override val httpLoggingInterceptor: HttpLoggingInterceptor? = defaultConfiguration.httpLoggingInterceptor

        /**
         * @see [SSLSocketFactory]
         */
        override val sslSocketFactory: SSLSocketFactory? = defaultConfiguration.sslSocketFactory

        /**
         * @see [X509ExtendedTrustManager]
         */
        override val x509ExtendedTrustManager: X509ExtendedTrustManager? = defaultConfiguration.x509ExtendedTrustManager

        /**
         * @see [okhttp3.ConnectionSpec]
         */
        override val connectionSpec: ConnectionSpec? = defaultConfiguration.connectionSpec

        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        override val hostnameVerifier: HostnameVerifier? = defaultConfiguration.hostnameVerifier

        /**
         * How many times publishing file message should automatically retry before marking the action as failed
         *
         * Defaults to `5`
         */
        override val fileMessagePublishRetryLimit: Int = defaultConfiguration.fileMessagePublishRetryLimit
        override val dedupOnSubscribe: Boolean = defaultConfiguration.dedupOnSubscribe
        override val maximumMessagesCacheSize: Int = defaultConfiguration.maximumMessagesCacheSize
        override val pnsdkSuffixes: Map<String, String> = defaultConfiguration.pnsdkSuffixes

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will valy from provided value by random value.
         */
        override val retryConfiguration: RetryConfiguration = defaultConfiguration.retryConfiguration

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see BasePNConfigurationImpl.heartbeatInterval
         */
        override val managePresenceListManually: Boolean = defaultConfiguration.managePresenceListManually
    }
}

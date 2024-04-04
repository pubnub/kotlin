package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
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
interface BasePNConfiguration : BasePNConfigurationOverride {
    /**
     * The user ID that the PubNub client will use.
     */
    override val userId: UserId

    /**
     * The subscribe key from the admin panel.
     */
    override val subscribeKey: String

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    override val publishKey: String

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    val secretKey: String

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    override val authKey: String

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    override val cryptoModule: CryptoModule?

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    val origin: String

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    val secure: Boolean

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    val logVerbosity: PNLogVerbosity

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    val heartbeatNotificationOptions: PNHeartbeatNotificationOptions

    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    val presenceTimeout: Int

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    val heartbeatInterval: Int

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    val subscribeTimeout: Int

    /**
     * How long before the client gives up trying to connect with the server.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    val connectTimeout: Int

    /**
     * For non subscribe operations (publish, herenow, etc),
     * This property relates to a read timeout that is applied from the moment the connection between a client
     * and the server has been successfully established. It defines a maximum time of inactivity between two
     * data packets when waiting for the server’s response.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    val nonSubscribeRequestTimeout: Int

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    val cacheBusting: Boolean

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    val suppressLeaveEvents: Boolean

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
    val maintainPresenceState: Boolean

    /**
     * Feature to subscribe with a custom filter expression.
     */
    val filterExpression: String

    /**
     * Whether to include a [PubNubCore.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    override val includeInstanceIdentifier: Boolean

    /**
     * Whether to include a [PubNubCore.requestId] with every request.
     *
     * Defaults to `true`.
     */
    override val includeRequestIdentifier: Boolean

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    val maximumConnections: Int?

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    val googleAppEngineNetworking: Boolean

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    val proxy: Proxy?

    /**
     * @see [ProxySelector]
     */
    val proxySelector: ProxySelector?

    /**
     * @see [Authenticator]
     */
    val proxyAuthenticator: Authenticator?

    /**
     * @see [CertificatePinner]
     */
    val certificatePinner: CertificatePinner?

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    val httpLoggingInterceptor: HttpLoggingInterceptor?

    /**
     * @see [SSLSocketFactory]
     */
    val sslSocketFactory: SSLSocketFactory?

    /**
     * @see [X509ExtendedTrustManager]
     */
    val x509ExtendedTrustManager: X509ExtendedTrustManager?

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    val connectionSpec: ConnectionSpec?

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    val hostnameVerifier: HostnameVerifier?

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    val fileMessagePublishRetryLimit: Int

    val dedupOnSubscribe: Boolean
    val maximumMessagesCacheSize: Int
    val pnsdkSuffixes: Map<String, String>

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will valy from provided value by random value.
     */
    override val retryConfiguration: RetryConfiguration

    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     * @see PubNubCore.presence
     * @see BasePNConfiguration.heartbeatInterval
     */
    val managePresenceListManually: Boolean

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """Use UserId instead e.g. config.userId.value""",
        replaceWith = ReplaceWith("userId.value"),
    )
    val uuid: String
        get() = userId.value

    companion object {
        fun String.isValid() = isNotBlank()
    }

    interface Builder {
        /**
         * The user ID that the PubNub client will use.
         */
        val userId: UserId

        /**
         * The subscribe key from the admin panel.
         */
        val subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        val publishKey: String

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        val secretKey: String

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        val authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        val cryptoModule: CryptoModule?

        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        val origin: String

        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        val secure: Boolean

        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        val logVerbosity: PNLogVerbosity

        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        val heartbeatNotificationOptions: PNHeartbeatNotificationOptions

        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         *
         * Also sets the value of [heartbeatInterval]
         */
        val presenceTimeout: Int

        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        val heartbeatInterval: Int

        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        val subscribeTimeout: Int

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        val connectTimeout: Int

        /**
         * For non subscribe operations (publish, herenow, etc),
         * This property relates to a read timeout that is applied from the moment the connection between a client
         * and the server has been successfully established. It defines a maximum time of inactivity between two
         * data packets when waiting for the server’s response.
         *
         * The value is in seconds.
         *
         * Defaults to 10.
         */
        val nonSubscribeRequestTimeout: Int

        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        val cacheBusting: Boolean

        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        val suppressLeaveEvents: Boolean

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
        val maintainPresenceState: Boolean

        /**
         * Feature to subscribe with a custom filter expression.
         */
        val filterExpression: String

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        val includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        val includeRequestIdentifier: Boolean

        /**
         * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
         */
        val maximumConnections: Int?

        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        val googleAppEngineNetworking: Boolean

        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         *
         * @see [Proxy]
         */
        val proxy: Proxy?

        /**
         * @see [ProxySelector]
         */
        val proxySelector: ProxySelector?

        /**
         * @see [Authenticator]
         */
        val proxyAuthenticator: Authenticator?

        /**
         * @see [CertificatePinner]
         */
        val certificatePinner: CertificatePinner?

        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         *
         * @see [HttpLoggingInterceptor]
         */
        val httpLoggingInterceptor: HttpLoggingInterceptor?

        /**
         * @see [SSLSocketFactory]
         */
        val sslSocketFactory: SSLSocketFactory?

        /**
         * @see [X509ExtendedTrustManager]
         */
        val x509ExtendedTrustManager: X509ExtendedTrustManager?

        /**
         * @see [okhttp3.ConnectionSpec]
         */
        val connectionSpec: ConnectionSpec?

        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        val hostnameVerifier: HostnameVerifier?

        /**
         * How many times publishing file message should automatically retry before marking the action as failed
         *
         * Defaults to `5`
         */
        val fileMessagePublishRetryLimit: Int

        val dedupOnSubscribe: Boolean
        val maximumMessagesCacheSize: Int
        val pnsdkSuffixes: Map<String, String>

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        val retryConfiguration: RetryConfiguration

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see BasePNConfiguration.heartbeatInterval
         */
        val managePresenceListManually: Boolean
    }
}

interface BasePNConfigurationOverride {
    /**
     * The subscribe key from the admin panel.
     */
    val subscribeKey: String

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    val publishKey: String

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will valy from provided value by random value.
     */
    val retryConfiguration: RetryConfiguration

    /**
     * The user ID that the PubNub client will use.
     */
    val userId: UserId

    /**
     * Whether to include a [PubNub.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    val includeInstanceIdentifier: Boolean

    /**
     * Whether to include a [PubNub.requestId] with every request.
     *
     * Defaults to `true`.
     */
    val includeRequestIdentifier: Boolean

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    val authKey: String

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    val cryptoModule: CryptoModule?

    interface Builder {
        /**
         * The subscribe key from the admin panel.
         */
        val subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        val publishKey: String

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will valy from provided value by random value.
         */
        val retryConfiguration: RetryConfiguration

        /**
         * The user ID that the PubNub client will use.
         */
        val userId: UserId

        /**
         * Whether to include a [PubNub.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        val includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNub.requestId] with every request.
         *
         * Defaults to `true`.
         */
        val includeRequestIdentifier: Boolean

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        val authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        val cryptoModule: CryptoModule?
    }
}

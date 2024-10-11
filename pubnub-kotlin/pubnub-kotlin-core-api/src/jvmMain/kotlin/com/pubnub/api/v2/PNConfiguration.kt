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

actual interface PNConfiguration {
    /**
     * The user ID that the PubNub client will use.
     */
    actual val userId: UserId

    /**
     * The subscribe key from the admin panel.
     */
    actual val subscribeKey: String

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    actual val publishKey: String

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    actual val secretKey: String

    /**
     * If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests.
     */
    @Deprecated(
        "This setting is deprecated because it relates to deprecated Access Manager (PAM V2). Use method pubnub.setToken(token) instead.",
    )
    actual val authKey: String

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
    actual val logVerbosity: PNLogVerbosity

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
    @Deprecated(
        "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
        replaceWith = ReplaceWith("nonSubscribeReadTimeout")
    )
    val nonSubscribeRequestTimeout: Int
        get() = nonSubscribeReadTimeout

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
    val nonSubscribeReadTimeout: Int

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
     *  Defaults to [RetryConfiguration.Exponential] enabled only for subscription endpoint (other endpoints are excluded).
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
     * @see PNConfiguration.heartbeatInterval
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

        @JvmStatic
        fun builder(
            userId: UserId,
            subscribeKey: String,
            action: Builder.() -> Unit = {},
        ): Builder {
            return (
                Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                    .getConstructor(UserId::class.java, subscribeKey::class.java)
                    .newInstance(userId, subscribeKey) as Builder
            ).apply(action)
        }

        @JvmStatic
        fun builder(initialConfiguration: PNConfiguration): Builder {
            return Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                .getConstructor(PNConfiguration::class.java)
                .newInstance(initialConfiguration) as Builder
        }
    }

    interface Builder {
        /**
         * The user ID that the PubNub client will use.
         */
        var userId: UserId

        /**
         * The subscribe key from the admin panel.
         */
        var subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        var publishKey: String

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        var secretKey: String

        /**
         * If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests.
         */
        @Deprecated(
            "This setting is deprecated because it relates to deprecated Access Manager (PAM V2). Use method pubnub.setToken(token) instead.",
        )
        var authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        var cryptoModule: CryptoModule?

        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        var origin: String

        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        var secure: Boolean

        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        var logVerbosity: PNLogVerbosity

        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        var heartbeatNotificationOptions: PNHeartbeatNotificationOptions

        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         *
         * Also sets the value of [heartbeatInterval]
         */
        var presenceTimeout: Int

        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        var heartbeatInterval: Int

        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        var subscribeTimeout: Int

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        var connectTimeout: Int

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
        @Deprecated(
            "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
            replaceWith = ReplaceWith("nonSubscribeReadTimeout")
        )
        var nonSubscribeRequestTimeout: Int
            get() = nonSubscribeReadTimeout
            set(value) {
                nonSubscribeReadTimeout = value
            }

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
        var nonSubscribeReadTimeout: Int

        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        var cacheBusting: Boolean

        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        var suppressLeaveEvents: Boolean

        /**
         * When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState]
         * for the current [userId] with every automatic heartbeat (if [heartbeatInterval] is greater than 0)
         * and initial subscribe connection (also after e.g. loss of network).
         *
         * Defaults to `true`.
         *
         * Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups.
         * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState]
         * otherwise that state may be overwritten by individual channel states.
         */
        var maintainPresenceState: Boolean

        /**
         * Feature to subscribe with a custom filter expression.
         */
        var filterExpression: String

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        var includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        var includeRequestIdentifier: Boolean

        /**
         * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
         */
        var maximumConnections: Int?

        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        var googleAppEngineNetworking: Boolean

        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         *
         * @see [Proxy]
         */
        var proxy: Proxy?

        /**
         * @see [ProxySelector]
         */
        var proxySelector: ProxySelector?

        /**
         * @see [Authenticator]
         */
        var proxyAuthenticator: Authenticator?

        /**
         * @see [CertificatePinner]
         */
        var certificatePinner: CertificatePinner?

        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         *
         * @see [HttpLoggingInterceptor]
         */
        var httpLoggingInterceptor: HttpLoggingInterceptor?

        /**
         * @see [SSLSocketFactory]
         */
        var sslSocketFactory: SSLSocketFactory?

        /**
         * @see [X509ExtendedTrustManager]
         */
        var x509ExtendedTrustManager: X509ExtendedTrustManager?

        /**
         * @see [okhttp3.ConnectionSpec]
         */
        var connectionSpec: ConnectionSpec?

        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        var hostnameVerifier: HostnameVerifier?

        /**
         * How many times publishing file message should automatically retry before marking the action as failed
         *
         * Defaults to `5`
         */
        var fileMessagePublishRetryLimit: Int

        var dedupOnSubscribe: Boolean
        var maximumMessagesCacheSize: Int
        var pnsdkSuffixes: Map<String, String>

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.Exponential] enabled only for subscription endpoint (other endpoints are excluded).
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay intervar
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        var retryConfiguration: RetryConfiguration

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see PNConfiguration.heartbeatInterval
         */
        var managePresenceListManually: Boolean

        /**
         * Create a [PNConfiguration] object with values from this builder.
         */
        fun build(): PNConfiguration
    }
}

interface PNConfigurationOverride {
    companion object {
        @JvmStatic
        fun from(configuration: PNConfiguration): Builder {
            return Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                .getConstructor(PNConfiguration::class.java)
                .newInstance(configuration) as Builder
        }
    }

    interface Builder {
        /**
         * The subscribe key from the admin panel.
         */
        var subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        var publishKey: String

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        var secretKey: String

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.Exponential] enabled only for subscription endpoint (other endpoints are excluded).
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        var retryConfiguration: RetryConfiguration

        /**
         * The user ID that the PubNub client will use.
         */
        var userId: UserId

        /**
         * Whether to include a [PubNub.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        var includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNub.requestId] with every request.
         *
         * Defaults to `true`.
         */
        var includeRequestIdentifier: Boolean

        /**
         * If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests.
         */
        @Deprecated(
            "This setting is deprecated because it relates to deprecated Access Manager (PAM V2). Use method pubnub.setToken(token) instead.",
        )
        var authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        var cryptoModule: CryptoModule?

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        var connectTimeout: Int

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
        var nonSubscribeReadTimeout: Int

        /**
         * Create a [PNConfiguration] object with values from this builder.
         */
        fun build(): PNConfiguration
    }
}

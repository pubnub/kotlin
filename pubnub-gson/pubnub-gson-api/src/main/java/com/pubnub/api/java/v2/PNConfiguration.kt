package com.pubnub.api.java.v2

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

interface PNConfiguration : com.pubnub.api.v2.PNConfiguration {
    companion object {
        @JvmStatic
        fun builder(
            userId: UserId,
            subscribeKey: String,
        ): Builder {
            return Class.forName("com.pubnub.internal.java.v2.PNConfigurationImpl\$Builder")
                .getConstructor(UserId::class.java, subscribeKey::class.java)
                .newInstance(userId, subscribeKey) as Builder
        }

        @JvmStatic
        fun builder(initialConfiguration: com.pubnub.api.v2.PNConfiguration): Builder {
            return Class.forName("com.pubnub.internal.java.v2.PNConfigurationImpl\$Builder")
                .getConstructor(com.pubnub.api.v2.PNConfiguration::class.java)
                .newInstance(initialConfiguration) as Builder
        }
    }

    interface Builder : PNConfigurationOverride.Builder {
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
         * If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests.
         */
        @Deprecated(
            "This setting is deprecated because it relates to deprecated Access Manager (PAM V2). Use method pubnub.setToken(token) instead.",
        )
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
         * Whether to include a `instanceId` with every request.
         *
         * Defaults to `false`.
         */
        val includeInstanceIdentifier: Boolean

        /**
         * Whether to include a `requestId` with every request.
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

        override fun build(): PNConfiguration

        override fun setUserId(userId: UserId): Builder

        override fun subscribeKey(subscribeKey: String): Builder

        override fun publishKey(publishKey: String): Builder

        override fun secretKey(secretKey: String): Builder

        @Deprecated(
            message = "To set auth token use method pubnub.setToken(token)",
        )
        override fun authKey(authKey: String): Builder

        override fun cryptoModule(cryptoModule: CryptoModule?): Builder

        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        fun origin(origin: String): Builder

        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        fun secure(secure: Boolean): Builder

        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        fun logVerbosity(logVerbosity: PNLogVerbosity): Builder

        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        fun heartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): Builder

        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         *
         * Also sets the value of [heartbeatInterval]
         */
        fun presenceTimeout(presenceTimeout: Int): Builder

        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        fun heartbeatInterval(heartbeatInterval: Int): Builder

        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        fun subscribeTimeout(subscribeTimeout: Int): Builder

        override fun connectTimeout(connectTimeout: Int): Builder

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
        fun nonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): Builder {
            nonSubscribeReadTimeout(nonSubscribeRequestTimeout)
            return this
        }

        override fun nonSubscribeReadTimeout(nonSubscribeReadTimeout: Int): Builder

        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        fun cacheBusting(cacheBusting: Boolean): Builder

        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        fun suppressLeaveEvents(suppressLeaveEvents: Boolean): Builder

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
        fun maintainPresenceState(maintainPresenceState: Boolean): Builder

        /**
         * Feature to subscribe with a custom filter expression.
         */
        fun filterExpression(filterExpression: String): Builder

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        override fun includeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        override fun includeRequestIdentifier(includeRequestIdentifier: Boolean): Builder

        /**
         * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
         */
        fun maximumConnections(maximumConnections: Int?): Builder

        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        fun googleAppEngineNetworking(googleAppEngineNetworking: Boolean): Builder

        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         *
         * @see [Proxy]
         */
        fun proxy(proxy: Proxy?): Builder

        /**
         * @see [ProxySelector]
         */
        fun proxySelector(proxySelector: ProxySelector?): Builder

        /**
         * @see [Authenticator]
         */
        fun proxyAuthenticator(proxyAuthenticator: Authenticator?): Builder

        /**
         * @see [CertificatePinner]
         */
        fun certificatePinner(certificatePinner: CertificatePinner?): Builder

        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         *
         * @see [HttpLoggingInterceptor]
         */
        fun httpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): Builder

        /**
         * @see [SSLSocketFactory]
         */
        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder

        /**
         * @see [X509ExtendedTrustManager]
         */
        fun x509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): Builder

        /**
         * @see [okhttp3.ConnectionSpec]
         */
        fun connectionSpec(connectionSpec: ConnectionSpec?): Builder

        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        fun hostnameVerifier(hostnameVerifier: HostnameVerifier?): Builder

        /**
         * How many times publishing file message should automatically retry before marking the action as failed
         *
         * Defaults to `5`
         */
        fun fileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): Builder

        fun dedupOnSubscribe(dedupOnSubscribe: Boolean): Builder

        fun maximumMessagesCacheSize(maximumMessagesCacheSize: Int): Builder

        fun pnsdkSuffixes(pnsdkSuffixes: Map<String, String>): Builder

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.Exponential] enabled only for subscription endpoint (other endpoints are excluded).
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        override fun retryConfiguration(retryConfiguration: RetryConfiguration): Builder

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see PNConfigurationImpl.heartbeatInterval
         */
        fun managePresenceListManually(managePresenceListManually: Boolean): Builder
    }
}

interface PNConfigurationOverride : com.pubnub.api.v2.PNConfigurationOverride {
    companion object {
        @JvmStatic
        fun from(configuration: com.pubnub.api.v2.PNConfiguration): Builder {
            return Class.forName("com.pubnub.internal.java.v2.PNConfigurationImpl\$Builder")
                .getConstructor(com.pubnub.api.v2.PNConfiguration::class.java)
                .newInstance(configuration) as Builder
        }
    }

    interface Builder {
        fun setUserId(userId: UserId): Builder

        fun subscribeKey(subscribeKey: String): Builder

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        fun publishKey(publishKey: String): Builder

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.Exponential] enabled only for subscription endpoint (other endpoints are excluded).
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        fun retryConfiguration(retryConfiguration: RetryConfiguration): Builder

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        fun includeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        fun includeRequestIdentifier(includeRequestIdentifier: Boolean): Builder

        /**
         * If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests.
         */
        @Deprecated(
            "This setting is deprecated because it relates to deprecated Access Manager (PAM V2). Use method pubnub.setToken(token) instead.",
        )
        fun authKey(authKey: String): Builder

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        fun cryptoModule(cryptoModule: CryptoModule?): Builder

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        fun secretKey(secretKey: String): Builder

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        fun connectTimeout(connectTimeout: Int): Builder

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
        fun nonSubscribeReadTimeout(nonSubscribeReadTimeout: Int): Builder

        /**
         * Create a [PNConfiguration] object with values from this builder.
         */
        fun build(): PNConfiguration
    }
}

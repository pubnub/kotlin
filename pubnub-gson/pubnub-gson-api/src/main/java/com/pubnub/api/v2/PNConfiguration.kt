package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.v2.BasePNConfigurationImpl
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

interface PNConfiguration : BasePNConfiguration {
    companion object {
        @JvmStatic
        fun builder(
            userId: UserId,
            subscribeKey: String,
        ): Builder {
            return Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                .getConstructor(BasePNConfiguration::class.java)
                .newInstance(object : BasePNConfigurationImpl(userId) {
                    override val subscribeKey: String = subscribeKey
                }) as Builder
        }
    }

    interface Builder : BasePNConfiguration.Builder {
        fun build(): PNConfiguration

        fun setUserId(userId: UserId): Builder

        fun subscribeKey(subscribeKey: String): Builder

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        fun publishKey(publishKey: String): Builder

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        fun secretKey(secretKey: String): Builder

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        fun authKey(authKey: String): Builder

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        fun cryptoModule(cryptoModule: CryptoModule?): Builder

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
        fun nonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): Builder

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
        fun includeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        fun includeRequestIdentifier(includeRequestIdentifier: Boolean): Builder

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
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will valy from provided value by random value.
         */
        fun retryConfiguration(retryConfiguration: RetryConfiguration): Builder

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see BasePNConfigurationImpl.heartbeatInterval
         */
        fun managePresenceListManually(managePresenceListManually: Boolean): Builder
    }
}

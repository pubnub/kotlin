package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.v2.BasePNConfigurationImpl
import com.pubnub.internal.v2.PNConfigurationImpl
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
            return PNConfigurationImpl.Builder(object : BasePNConfigurationImpl(userId) {
                override val subscribeKey: String = subscribeKey
            })
        }
    }

    interface Builder : BasePNConfiguration.Builder {
        fun build(): PNConfiguration

        fun setUserId(userId: UserId): Builder

        fun setSubscribeKey(subscribeKey: String): Builder

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        fun setPublishKey(publishKey: String): Builder

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        fun setSecretKey(secretKey: String): Builder

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        fun setAuthKey(authKey: String): Builder

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        fun setCryptoModule(cryptoModule: CryptoModule?): Builder

        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        fun setOrigin(origin: String): Builder

        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        fun setSecure(secure: Boolean): Builder

        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        fun setLogVerbosity(logVerbosity: PNLogVerbosity): Builder

        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        fun setHeartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): Builder

        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         *
         * Also sets the value of [heartbeatInterval]
         */
        fun setPresenceTimeout(presenceTimeout: Int): Builder

        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        fun setHeartbeatInterval(heartbeatInterval: Int): Builder

        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        fun setSubscribeTimeout(subscribeTimeout: Int): Builder

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        fun setConnectTimeout(connectTimeout: Int): Builder

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
        fun setNonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): Builder

        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        fun setCacheBusting(cacheBusting: Boolean): Builder

        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        fun setSuppressLeaveEvents(suppressLeaveEvents: Boolean): Builder

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
        fun setMaintainPresenceState(maintainPresenceState: Boolean): Builder

        /**
         * Feature to subscribe with a custom filter expression.
         */
        fun setFilterExpression(filterExpression: String): Builder

        /**
         * Whether to include a [PubNubCore.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        fun setIncludeInstanceIdentifier(includeInstanceIdentifier: Boolean): Builder

        /**
         * Whether to include a [PubNubCore.requestId] with every request.
         *
         * Defaults to `true`.
         */
        fun setIncludeRequestIdentifier(includeRequestIdentifier: Boolean): Builder

        /**
         * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
         */
        fun setMaximumConnections(maximumConnections: Int?): Builder

        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        fun setGoogleAppEngineNetworking(googleAppEngineNetworking: Boolean): Builder

        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         *
         * @see [Proxy]
         */
        fun setProxy(proxy: Proxy?): Builder

        /**
         * @see [ProxySelector]
         */
        fun setProxySelector(proxySelector: ProxySelector?): Builder

        /**
         * @see [Authenticator]
         */
        fun setProxyAuthenticator(proxyAuthenticator: Authenticator?): Builder

        /**
         * @see [CertificatePinner]
         */
        fun setCertificatePinner(certificatePinner: CertificatePinner?): Builder

        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         *
         * @see [HttpLoggingInterceptor]
         */
        fun setHttpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): Builder

        /**
         * @see [SSLSocketFactory]
         */
        fun setSslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder

        /**
         * @see [X509ExtendedTrustManager]
         */
        fun setX509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): Builder

        /**
         * @see [okhttp3.ConnectionSpec]
         */
        fun setConnectionSpec(connectionSpec: ConnectionSpec?): Builder

        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?): Builder

        /**
         * How many times publishing file message should automatically retry before marking the action as failed
         *
         * Defaults to `5`
         */
        fun setFileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): Builder

        fun setDedupOnSubscribe(dedupOnSubscribe: Boolean): Builder

        fun setMaximumMessagesCacheSize(maximumMessagesCacheSize: Int): Builder

        fun setPnsdkSuffixes(pnsdkSuffixes: Map<String, String>): Builder

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will valy from provided value by random value.
         */
        fun setRetryConfiguration(retryConfiguration: RetryConfiguration): Builder

        /**
         * Enables explicit presence control.
         * When set to true heartbeat calls will contain only channels and groups added explicitly
         * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
         * For more information please contact PubNub support
         * @see PubNubCore.presence
         * @see BasePNConfigurationImpl.heartbeatInterval
         */
        fun setManagePresenceListManually(managePresenceListManually: Boolean): Builder
    }
}

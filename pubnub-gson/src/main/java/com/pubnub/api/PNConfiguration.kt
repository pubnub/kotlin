package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.PNConfigurationCore
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.net.Proxy
import java.net.ProxySelector
import java.util.concurrent.ConcurrentMap
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

class PNConfiguration(userId: UserId) {
    internal val pnConfigurationCore: PNConfigurationCore = PNConfigurationCore(userId)

    val origin: String
        /**
         * Custom origin if needed.
         *
         * Defaults to `ps.pndsn.com`
         */
        get() = pnConfigurationCore.origin

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    fun setOrigin(s: String): PNConfiguration {
        pnConfigurationCore.origin = s
        return this
    }

    val secure: Boolean
        /**
         * If set to `true`,  requests will be made over HTTPS.
         *
         * Deafults to `true`.
         */
        get() = pnConfigurationCore.secure

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    fun setSecure(b: Boolean): PNConfiguration {
        pnConfigurationCore.secure = b
        return this
    }

    val logVerbosity: PNLogVerbosity
        /**
         * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
         */
        get() = pnConfigurationCore.logVerbosity

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    fun setLogVerbosity(pnLogVerbosity: PNLogVerbosity): PNConfiguration {
        pnConfigurationCore.logVerbosity = pnLogVerbosity
        return this
    }

    val heartbeatNotificationOptions: PNHeartbeatNotificationOptions
        /**
         * Set Heartbeat notification options.
         *
         * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
         */
        get() = pnConfigurationCore.heartbeatNotificationOptions

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    fun setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions: PNHeartbeatNotificationOptions): PNConfiguration {
        pnConfigurationCore.heartbeatNotificationOptions = pnHeartbeatNotificationOptions
        return this
    }

    @get:Deprecated("use {@link #setRetryConfiguration(RetryConfiguration)} instead")
    val reconnectionPolicy: PNReconnectionPolicy
        /**
         * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
         *
         * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
         *
         * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
         *
         * Defaults to [PNReconnectionPolicy.NONE].
         */
        get() = pnConfigurationCore.reconnectionPolicy

    /**
     * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
     *
     * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
     *
     * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
     *
     * Defaults to [PNReconnectionPolicy.NONE].
     */
    @Deprecated("use {@link #setRetryConfiguration(RetryConfiguration)} instead")
    fun setReconnectionPolicy(pnReconnectionPolicy: PNReconnectionPolicy): PNConfiguration {
        pnConfigurationCore.reconnectionPolicy = pnReconnectionPolicy
        return this
    }

    val presenceTimeout: Int
        /**
         * Sets the custom presence server timeout.
         *
         * The value is in seconds, and the minimum value is 20 seconds.
         */
        get() = pnConfigurationCore.presenceTimeout

    @Deprecated("Use {@link #setUserId(UserId)} instead.")
    fun setUuid(uuid: String): PNConfiguration {
        pnConfigurationCore.uuid = uuid
        return this
    }

    val subscribeKey: String
        /**
         * The subscribe key from the admin panel.
         */
        get() = pnConfigurationCore.subscribeKey

    /**
     * The subscribe key from the admin panel.
     */
    fun setSubscribeKey(s: String): PNConfiguration {
        pnConfigurationCore.subscribeKey = s
        return this
    }

    val publishKey: String
        /**
         * The publish key from the admin panel (only required if publishing).
         */
        get() = pnConfigurationCore.publishKey

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    fun setPublishKey(s: String): PNConfiguration {
        pnConfigurationCore.publishKey = s
        return this
    }

    val secretKey: String
        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        get() = pnConfigurationCore.secretKey

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    fun setSecretKey(s: String): PNConfiguration {
        pnConfigurationCore.secretKey = s
        return this
    }

    val authKey: String
        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        get() = pnConfigurationCore.authKey

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    fun setAuthKey(s: String): PNConfiguration {
        pnConfigurationCore.authKey = s
        return this
    }

    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    val cipherKey: String
        /**
         * If set, all communications to and from PubNub will be encrypted.
         */
        get() = pnConfigurationCore.cipherKey

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */
    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    fun setCipherKey(s: String?): PNConfiguration {
        pnConfigurationCore.cipherKey = s ?: ""
        return this
    }

    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    val useRandomInitializationVector: Boolean
        /**
         * Should initialization vector for encrypted messages be random.
         *
         * Defaults to `false`.
         */
        get() = pnConfigurationCore.useRandomInitializationVector

    /**
     * Should initialization vector for encrypted messages be random.
     *
     * Defaults to `false`.
     */
    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    fun setUseRandomInitializationVector(b: Boolean): PNConfiguration {
        pnConfigurationCore.useRandomInitializationVector = b
        return this
    }

    val cryptoModule: CryptoModule?
        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        get() = pnConfigurationCore.cryptoModule

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    fun setCryptoModule(cryptoModule: CryptoModule?): PNConfiguration {
        pnConfigurationCore.cryptoModule = cryptoModule
        return this
    }

    val userId: UserId
        get() = pnConfigurationCore.userId

    fun setUserId(userId: UserId): PNConfiguration {
        pnConfigurationCore.userId = userId
        return this
    }

    val uuid: String
        get() = pnConfigurationCore.uuid

    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using [PubNub.presence]. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     * @see PubNub.presence
     * @see PNConfiguration.heartbeatInterval
     */
    @Deprecated("Not recommended for use.")
    private val managePresenceListManually
        get() = pnConfigurationCore.managePresenceListManually

    @Deprecated("Not recommended for use.")
    fun setManagePresenceListManually(managePresenceListManually: Boolean): PNConfiguration {
        pnConfigurationCore.managePresenceListManually = managePresenceListManually
        return this
    }

    /**
     * Initialize the PNConfiguration with default values
     * @param uuid
     */
    @Deprecated("Use {@link com.pubnub.internal.PNConfigurationCore (UserId)} instead.")
    constructor(uuid: String) : this(UserId(uuid))

    /**
     * set presence configurations for timeout and announce interval.
     * @param timeout  presence timeout; how long before the server considers this client to be gone.
     * @param interval presence announce interval, how often the client should announce itself.
     * @return returns itself.
     */
    fun setPresenceTimeoutWithCustomInterval(
        timeout: Int,
        interval: Int,
    ): PNConfiguration {
        var timeout = timeout
        timeout = validatePresenceTimeout(timeout)
        pnConfigurationCore.presenceTimeout = timeout
        pnConfigurationCore.heartbeatInterval = interval

        return this
    }

    /**
     * Set presence configurations for timeout and allow the client to pick the best presence interval
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @return returns itself.
     */
    fun setPresenceTimeout(timeout: Int): PNConfiguration {
        var timeout = timeout
        timeout = validatePresenceTimeout(timeout)
        pnConfigurationCore.presenceTimeout = timeout
        return this
    }

    val heartbeatInterval: Int
        /**
         * How often the client will announce itself to server.
         *
         * The value is in seconds.
         */
        get() = pnConfigurationCore.heartbeatInterval

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    fun setHeartbeatInterval(interval: Int): PNConfiguration {
        pnConfigurationCore.heartbeatInterval = interval
        return this
    }

    val subscribeTimeout: Int
        /**
         * The subscribe request timeout.
         *
         * The value is in seconds.
         *
         * Defaults to 310.
         */
        get() = pnConfigurationCore.subscribeTimeout

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    fun setSubscribeTimeout(i: Int): PNConfiguration {
        pnConfigurationCore.subscribeTimeout = i
        return this
    }

    val connectTimeout: Int
        /**
         * How long before the client gives up trying to connect with a subscribe call.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        get() = pnConfigurationCore.connectTimeout

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    fun setConnectTimeout(i: Int): PNConfiguration {
        pnConfigurationCore.connectTimeout = i
        return this
    }

    val nonSubscribeRequestTimeout: Int
        /**
         * For non subscribe operations (publish, herenow, etc),
         * how long to wait to connect to PubNub before giving up with a connection timeout error.
         *
         * The value is in seconds.
         *
         * Defaults to 10.
         */
        get() = pnConfigurationCore.nonSubscribeRequestTimeout

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    fun setNonSubscribeRequestTimeout(i: Int): PNConfiguration {
        pnConfigurationCore.nonSubscribeRequestTimeout = i
        return this
    }

    val cacheBusting: Boolean
        /**
         * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
         *
         * Defaults to `false`.
         */
        get() = pnConfigurationCore.cacheBusting

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    fun setCacheBusting(b: Boolean): PNConfiguration {
        pnConfigurationCore.cacheBusting = b
        return this
    }

    val suppressLeaveEvents: Boolean
        /**
         * When `true` the SDK doesn't send out the leave requests.
         *
         * Defaults to `false`.
         */
        get() = pnConfigurationCore.suppressLeaveEvents

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    fun setSuppressLeaveEvents(b: Boolean): PNConfiguration {
        pnConfigurationCore.suppressLeaveEvents = b
        return this
    }

    val maintainPresenceState: Boolean
        /**
         * When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState]
         * for the current [.getUserId] with every automatic heartbeat (if [.getHeartbeatInterval] is greater than 0)
         * and initial subscribe connection (also after e.g. loss of network).
         *
         * Defaults to `true`.
         *
         * Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups.
         * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState],
         * otherwise that state may be overwritten by individual channel states.
         */
        get() = pnConfigurationCore.maintainPresenceState

    /**
     * When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState]
     * for the current [.getUserId] with every automatic heartbeat (if [.getHeartbeatInterval] is greater than 0)
     * and initial subscribe connection (also after e.g. loss of network).
     *
     * Defaults to `true`.
     *
     * Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups.
     * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState],
     * otherwise that state may be overwritten by individual channel states.
     */
    fun setMaintainPresenceState(b: Boolean): PNConfiguration {
        pnConfigurationCore.maintainPresenceState = b
        return this
    }

    val filterExpression: String
        /**
         * Feature to subscribe with a custom filter expression.
         */
        get() = pnConfigurationCore.filterExpression

    /**
     * Feature to subscribe with a custom filter expression.
     */
    fun setFilterExpression(s: String): PNConfiguration {
        pnConfigurationCore.filterExpression = s
        return this
    }

    val includeInstanceIdentifier: Boolean
        /**
         * Whether to include a [InternalPubNubClient.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        get() = pnConfigurationCore.includeInstanceIdentifier

    /**
     * Whether to include a [InternalPubNubClient.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    fun setIncludeInstanceIdentifier(b: Boolean): PNConfiguration {
        pnConfigurationCore.includeInstanceIdentifier = b
        return this
    }

    val includeRequestIdentifier: Boolean
        /**
         * Whether to include a requestId with every request.
         *
         * Defaults to `true`.
         */
        get() = pnConfigurationCore.includeRequestIdentifier

    /**
     * Whether to include a requestId with every request.
     *
     * Defaults to `true`.
     */
    fun setIncludeRequestIdentifier(b: Boolean): PNConfiguration {
        pnConfigurationCore.includeRequestIdentifier = b
        return this
    }

    @get:Deprecated("Instead of reconnectionPolicy and maximumReconnectionRetries use `setRetryConfiguration`")
    val maximumReconnectionRetries: Int
        /**
         * Sets how many times to retry to reconnect before giving up.
         * Must be used in combination with [reconnectionPolicy].
         *
         * The default value is `-1` which means unlimited retries.
         */
        get() = pnConfigurationCore.maximumReconnectionRetries

    /**
     * Sets how many times to retry to reconnect before giving up.
     * Must be used in combination with [reconnectionPolicy].
     *
     * The default value is `-1` which means unlimited retries.
     */
    @Deprecated("Instead of reconnectionPolicy and maximumReconnectionRetries use `setRetryConfiguration`")
    fun setMaximumReconnectionRetries(i: Int): PNConfiguration {
        pnConfigurationCore.maximumReconnectionRetries = i
        return this
    }

    val maximumConnections: Int?
        /**
         * @see okhttp3.Dispatcher.setMaxRequestsPerHost
         */
        get() = pnConfigurationCore.maximumConnections

    /**
     * @see okhttp3.Dispatcher.setMaxRequestsPerHost
     */
    fun setMaximumConnections(integer: Int?): PNConfiguration {
        pnConfigurationCore.maximumConnections = integer
        return this
    }

    val googleAppEngineNetworking: Boolean
        /**
         * Enable Google App Engine networking.
         *
         * Defaults to `false`.
         */
        get() = pnConfigurationCore.googleAppEngineNetworking

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    fun setGoogleAppEngineNetworking(b: Boolean): PNConfiguration {
        pnConfigurationCore.googleAppEngineNetworking = b
        return this
    }

    val proxy: Proxy?
        /**
         * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
         * @see Proxy
         */
        get() = pnConfigurationCore.proxy

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     * @see Proxy
     */
    fun setProxy(proxy: Proxy?): PNConfiguration {
        pnConfigurationCore.proxy = proxy
        return this
    }

    val proxySelector: ProxySelector?
        /**
         * @see ProxySelector
         */
        get() = pnConfigurationCore.proxySelector

    /**
     * @see ProxySelector
     */
    fun setProxySelector(proxySelector: ProxySelector?): PNConfiguration {
        pnConfigurationCore.proxySelector = proxySelector
        return this
    }

    val proxyAuthenticator: Authenticator?
        /**
         * @see Authenticator
         */
        get() = pnConfigurationCore.proxyAuthenticator

    /**
     * @see Authenticator
     */
    fun setProxyAuthenticator(authenticator: Authenticator?): PNConfiguration {
        pnConfigurationCore.proxyAuthenticator = authenticator
        return this
    }

    val certificatePinner: CertificatePinner?
        /**
         * @see CertificatePinner
         */
        get() = pnConfigurationCore.certificatePinner

    /**
     * @see CertificatePinner
     */
    fun setCertificatePinner(certificatePinner: CertificatePinner?): PNConfiguration {
        pnConfigurationCore.certificatePinner = certificatePinner
        return this
    }

    val httpLoggingInterceptor: HttpLoggingInterceptor?
        /**
         * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
         * @see HttpLoggingInterceptor
         */
        get() = pnConfigurationCore.httpLoggingInterceptor

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     * @see HttpLoggingInterceptor
     */
    fun setHttpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): PNConfiguration {
        pnConfigurationCore.httpLoggingInterceptor = httpLoggingInterceptor
        return this
    }

    val sslSocketFactory: SSLSocketFactory?
        /**
         * @see SSLSocketFactory
         */
        get() = pnConfigurationCore.sslSocketFactory

    /**
     * @see SSLSocketFactory
     */
    fun setSslSocketFactory(sslSocketFactory: SSLSocketFactory?): PNConfiguration {
        pnConfigurationCore.sslSocketFactory = sslSocketFactory
        return this
    }

    val x509ExtendedTrustManager: X509ExtendedTrustManager?
        /**
         * @see [X509ExtendedTrustManager]
         */
        get() = pnConfigurationCore.x509ExtendedTrustManager

    /**
     * @see [X509ExtendedTrustManager]
     */
    fun setX509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): PNConfiguration {
        pnConfigurationCore.x509ExtendedTrustManager = x509ExtendedTrustManager
        return this
    }

    val connectionSpec: ConnectionSpec?
        /**
         * @see [okhttp3.ConnectionSpec]
         */
        get() = pnConfigurationCore.connectionSpec

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    fun setConnectionSpec(connectionSpec: ConnectionSpec?): PNConfiguration {
        pnConfigurationCore.connectionSpec = connectionSpec
        return this
    }

    val hostnameVerifier: HostnameVerifier?
        /**
         * @see [javax.net.ssl.HostnameVerifier]
         */
        get() = pnConfigurationCore.hostnameVerifier

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?): PNConfiguration {
        pnConfigurationCore.hostnameVerifier = hostnameVerifier
        return this
    }

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    val fileMessagePublishRetryLimit: Int
        get() = pnConfigurationCore.fileMessagePublishRetryLimit

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    fun setFileMessagePublishRetryLimit(i: Int): PNConfiguration {
        pnConfigurationCore.fileMessagePublishRetryLimit = i
        return this
    }

    val dedupOnSubscribe: Boolean
        get() = pnConfigurationCore.dedupOnSubscribe

    fun setDedupOnSubscribe(b: Boolean): PNConfiguration {
        pnConfigurationCore.dedupOnSubscribe = b
        return this
    }

    val maximumMessagesCacheSize: Int
        get() = pnConfigurationCore.maximumMessagesCacheSize

    fun setMaximumMessagesCacheSize(i: Int): PNConfiguration {
        pnConfigurationCore.maximumMessagesCacheSize = i
        return this
    }

    val pnsdkSuffixes: ConcurrentMap<String, String>
        get() = pnConfigurationCore.pnsdkSuffixes

    fun generatePnsdk(version: String): String {
        return pnConfigurationCore.generatePnsdk(version)
    }

    @Deprecated("")
    fun addPnsdkSuffix(vararg nameToSuffixes: Pair<String, String>): PNConfiguration {
        pnConfigurationCore.addPnsdkSuffix(*nameToSuffixes)
        return this
    }

    @Deprecated("")
    fun addPnsdkSuffix(nameToSuffixes: Map<String, String>): PNConfiguration {
        pnConfigurationCore.addPnsdkSuffix(nameToSuffixes)
        return this
    }

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will vary from provided value by random value.
     */
    fun setRetryConfiguration(configuration: RetryConfiguration): PNConfiguration {
        pnConfigurationCore.retryConfiguration = configuration
        return this
    }

    val retryConfiguration: RetryConfiguration
        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        get() = pnConfigurationCore.retryConfiguration

    companion object {
        private const val MINIMUM_PRESENCE_TIMEOUT = 20
        private val log = LoggerFactory.getLogger("PNConfiguration")

        private fun validatePresenceTimeout(timeout: Int): Int {
            var validTimeout = timeout
            if (timeout < MINIMUM_PRESENCE_TIMEOUT) {
                validTimeout = MINIMUM_PRESENCE_TIMEOUT
                log.warn("Presence timeout is too low. Defaulting to: " + MINIMUM_PRESENCE_TIMEOUT)
            }
            return validTimeout
        }
    }
}

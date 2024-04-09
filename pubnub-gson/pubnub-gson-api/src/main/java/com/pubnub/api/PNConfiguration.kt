package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.internal.v2.BasePNConfigurationImpl
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

@Deprecated(
    message = "Use `com.pubnub.api.v2.PNConfiguration.builder()` instead.",
    replaceWith = ReplaceWith("PNConfiguration.builder(userId, subscribeKey = )", "com.pubnub.api.v2.PNConfiguration"),
)
class PNConfiguration(userId: UserId) : BasePNConfiguration {
    @Deprecated(
        replaceWith = ReplaceWith(
            "PNConfiguration(userId = UserId(uuid))",
            "com.pubnub.api.PNConfiguration",
        ),
        level = DeprecationLevel.WARNING,
        message = "Use PNConfiguration(UserId) instead.",
    )
    @Throws(PubNubException::class)
    constructor(uuid: String) : this(UserId(uuid))

    private val defaultConfiguration = BasePNConfigurationImpl(userId)

    override var userId: UserId = userId
        private set

    fun setUserId(userId: UserId): PNConfiguration {
        this.userId = userId
        return this
    }

    @Deprecated(
        "Use UserId instead e.g. config.userId.value",
        replaceWith = ReplaceWith("userId.value"),
        level = DeprecationLevel.WARNING,
    )
    override val uuid
        get() = userId.value

    @Throws(PubNubException::class)
    fun setUuid(uuid: String) {
        userId = UserId(uuid)
    }

    /**
     * The subscribe key from the admin panel.
     */
    fun setSubscribeKey(subscribeKey: String): PNConfiguration {
        this.subscribeKey = subscribeKey
        return this
    }

    override var subscribeKey: String = defaultConfiguration.subscribeKey
        private set

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    fun setPublishKey(publishKey: String): PNConfiguration {
        this.publishKey = publishKey
        return this
    }

    override var publishKey: String = defaultConfiguration.publishKey
        private set

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    fun setSecretKey(secretKey: String): PNConfiguration {
        this.secretKey = secretKey
        return this
    }

    override var secretKey: String = defaultConfiguration.secretKey
        private set

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    fun setAuthKey(authKey: String): PNConfiguration {
        this.authKey = authKey
        return this
    }

    override var authKey: String = defaultConfiguration.authKey
        private set

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */
    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    var cipherKey: String? = null
        private set

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */
    fun setCipherKey(cipherKey: String?): PNConfiguration {
        this.cipherKey = cipherKey
        return this
    }

    /**
     * Should initialization vector for encrypted messages be random.
     *
     * Defaults to `true`.
     */
    @Deprecated(
        """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
        level = DeprecationLevel.WARNING,
    )
    var useRandomInitializationVector: Boolean = true
        private set

    /**
     * Should initialization vector for encrypted messages be random.
     *
     * Defaults to `true`.
     */
    fun setUseRandomInitializationVector(useRandomInitializationVector: Boolean): PNConfiguration {
        this.useRandomInitializationVector = useRandomInitializationVector
        return this
    }

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    fun setCryptoModule(cryptoModule: CryptoModule?): PNConfiguration {
        this.cryptoModule = cryptoModule
        return this
    }

    override var cryptoModule: CryptoModule? = defaultConfiguration.cryptoModule
        get() = field ?: cipherKey?.let { cipherKey ->
            if (cipherKey.isNotBlank()) {
                log.warn("cipherKey is deprecated. Use CryptoModule instead")
                field =
                    CryptoModule.createLegacyCryptoModule(
                        cipherKey = cipherKey,
                        randomIv = useRandomInitializationVector,
                    )
                field
            } else {
                null
            }
        }
        private set

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    fun setOrigin(origin: String): PNConfiguration {
        this.origin = origin
        return this
    }

    override var origin: String = defaultConfiguration.origin
        private set

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    fun setSecure(secure: Boolean): PNConfiguration {
        this.secure = secure
        return this
    }

    override var secure: Boolean = defaultConfiguration.secure
        private set

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    fun setLogVerbosity(logVerbosity: PNLogVerbosity): PNConfiguration {
        this.logVerbosity = logVerbosity
        return this
    }

    override var logVerbosity: PNLogVerbosity = defaultConfiguration.logVerbosity
        private set

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    fun setHeartbeatNotificationOptions(heartbeatNotificationOptions: PNHeartbeatNotificationOptions): PNConfiguration {
        this.heartbeatNotificationOptions = heartbeatNotificationOptions
        return this
    }

    override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions =
        defaultConfiguration.heartbeatNotificationOptions
        private set

    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    fun setPresenceTimeout(presenceTimeout: Int): PNConfiguration {
        val timeout = validatePresenceTimeout(presenceTimeout)
        setPresenceTimeoutWithCustomInterval(timeout, (timeout / 2) - 1)
        return this
    }

    /**
     * Set presence configurations for timeout and announce interval.
     *
     * @param timeout  presence timeout; how long before the server considers this client to be gone.
     * @param interval presence announce interval, how often the client should announce itself.
     * @return returns itself.
     */
    fun setPresenceTimeoutWithCustomInterval(
        timeout: Int,
        interval: Int,
    ): PNConfiguration {
        val newTimeout = validatePresenceTimeout(timeout)
        presenceTimeout = newTimeout
        heartbeatInterval = interval
        return this
    }

    override var presenceTimeout: Int = defaultConfiguration.presenceTimeout
        private set

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    fun setHeartbeatInterval(heartbeatInterval: Int): PNConfiguration {
        this.heartbeatInterval = heartbeatInterval
        return this
    }

    override var heartbeatInterval: Int = defaultConfiguration.heartbeatInterval
        private set

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    fun setSubscribeTimeout(subscribeTimeout: Int): PNConfiguration {
        this.subscribeTimeout = subscribeTimeout
        return this
    }

    override var subscribeTimeout: Int = defaultConfiguration.subscribeTimeout
        private set

    /**
     * How long before the client gives up trying to connect with the server.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    fun setConnectTimeout(connectTimeout: Int): PNConfiguration {
        this.connectTimeout = connectTimeout
        return this
    }

    override var connectTimeout: Int = defaultConfiguration.connectTimeout
        private set

    override var nonSubscribeReadTimeout: Int = defaultConfiguration.nonSubscribeReadTimeout
        private set

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
    fun setNonSubscribeReadTimeout(nonSubscribeReadTimeout: Int): PNConfiguration {
        this.nonSubscribeReadTimeout = nonSubscribeReadTimeout
        return this
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
    @Deprecated(
        "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
        replaceWith = ReplaceWith("nonSubscribeReadTimeout")
    )
    fun setNonSubscribeRequestTimeout(nonSubscribeRequestTimeout: Int): PNConfiguration {
        return this.setNonSubscribeReadTimeout(nonSubscribeRequestTimeout)
    }

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    fun setCacheBusting(cacheBusting: Boolean): PNConfiguration {
        this.cacheBusting = cacheBusting
        return this
    }

    override var cacheBusting: Boolean = defaultConfiguration.cacheBusting
        private set

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    fun setSuppressLeaveEvents(suppressLeaveEvents: Boolean): PNConfiguration {
        this.suppressLeaveEvents = suppressLeaveEvents
        return this
    }

    override var suppressLeaveEvents: Boolean = defaultConfiguration.suppressLeaveEvents
        private set

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
    fun setMaintainPresenceState(maintainPresenceState: Boolean): PNConfiguration {
        this.maintainPresenceState = maintainPresenceState
        return this
    }

    override var maintainPresenceState: Boolean = defaultConfiguration.maintainPresenceState
        private set

    /**
     * Feature to subscribe with a custom filter expression.
     */
    fun setFilterExpression(filterExpression: String): PNConfiguration {
        this.filterExpression = filterExpression
        return this
    }

    override var filterExpression: String = defaultConfiguration.filterExpression
        private set

    /**
     * Whether to include a [PubNubCore.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    fun setIncludeInstanceIdentifier(includeInstanceIdentifier: Boolean): PNConfiguration {
        this.includeInstanceIdentifier = includeInstanceIdentifier
        return this
    }

    override var includeInstanceIdentifier: Boolean = defaultConfiguration.includeInstanceIdentifier
        private set

    /**
     * Whether to include a [PubNubCore.requestId] with every request.
     *
     * Defaults to `true`.
     */
    fun setIncludeRequestIdentifier(includeRequestIdentifier: Boolean): PNConfiguration {
        this.includeRequestIdentifier = includeRequestIdentifier
        return this
    }

    override var includeRequestIdentifier: Boolean = defaultConfiguration.includeRequestIdentifier
        private set

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    fun setMaximumConnections(maximumConnections: Int?): PNConfiguration {
        this.maximumConnections = maximumConnections
        return this
    }

    override var maximumConnections: Int? = defaultConfiguration.maximumConnections
        private set

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    fun setGoogleAppEngineNetworking(googleAppEngineNetworking: Boolean): PNConfiguration {
        this.googleAppEngineNetworking = googleAppEngineNetworking
        return this
    }

    override var googleAppEngineNetworking: Boolean = defaultConfiguration.googleAppEngineNetworking
        private set

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    fun setProxy(proxy: Proxy?): PNConfiguration {
        this.proxy = proxy
        return this
    }

    override var proxy: Proxy? = defaultConfiguration.proxy
        private set

    /**
     * @see [ProxySelector]
     */
    fun setProxySelector(proxySelector: ProxySelector?): PNConfiguration {
        this.proxySelector = proxySelector
        return this
    }

    override var proxySelector: ProxySelector? = defaultConfiguration.proxySelector
        private set

    /**
     * @see [Authenticator]
     */
    fun setProxyAuthenticator(proxyAuthenticator: Authenticator?): PNConfiguration {
        this.proxyAuthenticator = proxyAuthenticator
        return this
    }

    override var proxyAuthenticator: Authenticator? = defaultConfiguration.proxyAuthenticator
        private set

    /**
     * @see [CertificatePinner]
     */
    fun setCertificatePinner(certificatePinner: CertificatePinner?): PNConfiguration {
        this.certificatePinner = certificatePinner
        return this
    }

    override var certificatePinner: CertificatePinner? = defaultConfiguration.certificatePinner
        private set

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    fun setHttpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor?): PNConfiguration {
        this.httpLoggingInterceptor = httpLoggingInterceptor
        return this
    }

    override var httpLoggingInterceptor: HttpLoggingInterceptor? = defaultConfiguration.httpLoggingInterceptor
        private set

    /**
     * @see [SSLSocketFactory]
     */
    fun setSslSocketFactory(sslSocketFactory: SSLSocketFactory?): PNConfiguration {
        this.sslSocketFactory = sslSocketFactory
        return this
    }

    override var sslSocketFactory: SSLSocketFactory? = defaultConfiguration.sslSocketFactory
        private set

    /**
     * @see [X509ExtendedTrustManager]
     */
    fun setX509ExtendedTrustManager(x509ExtendedTrustManager: X509ExtendedTrustManager?): PNConfiguration {
        this.x509ExtendedTrustManager = x509ExtendedTrustManager
        return this
    }

    override var x509ExtendedTrustManager: X509ExtendedTrustManager? = defaultConfiguration.x509ExtendedTrustManager
        private set

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    fun setConnectionSpec(connectionSpec: ConnectionSpec?): PNConfiguration {
        this.connectionSpec = connectionSpec
        return this
    }

    override var connectionSpec: ConnectionSpec? = defaultConfiguration.connectionSpec
        private set

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?): PNConfiguration {
        this.hostnameVerifier = hostnameVerifier
        return this
    }

    override var hostnameVerifier: HostnameVerifier? = defaultConfiguration.hostnameVerifier
        private set

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    fun setFileMessagePublishRetryLimit(fileMessagePublishRetryLimit: Int): PNConfiguration {
        this.fileMessagePublishRetryLimit = fileMessagePublishRetryLimit
        return this
    }

    override var fileMessagePublishRetryLimit: Int = defaultConfiguration.fileMessagePublishRetryLimit
        private set

    fun setDedupOnSubscribe(dedupOnSubscribe: Boolean): PNConfiguration {
        this.dedupOnSubscribe = dedupOnSubscribe
        return this
    }

    override var dedupOnSubscribe: Boolean = defaultConfiguration.dedupOnSubscribe
        private set

    fun setMaximumMessagesCacheSize(maximumMessagesCacheSize: Int): PNConfiguration {
        this.maximumMessagesCacheSize = maximumMessagesCacheSize
        return this
    }

    override var maximumMessagesCacheSize: Int = defaultConfiguration.maximumMessagesCacheSize
        private set

    fun setPnsdkSuffixes(pnSdkSuffixes: Map<String, String>): PNConfiguration {
        this.pnsdkSuffixes = pnSdkSuffixes
        return this
    }

    override var pnsdkSuffixes: Map<String, String> = defaultConfiguration.pnsdkSuffixes
        private set

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will valy from provided value by random value.
     */
    fun setRetryConfiguration(retryConfiguration: RetryConfiguration): PNConfiguration {
        this.retryConfiguration = retryConfiguration
        return this
    }

    override var retryConfiguration: RetryConfiguration = defaultConfiguration.retryConfiguration
        private set

    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using [PubNubCore.presence]. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     * @see PubNubCore.presence
     * @see BasePNConfigurationImpl.heartbeatInterval
     */
    fun setManagePresenceListManually(managePresenceListManually: Boolean): PNConfiguration {
        this.managePresenceListManually = managePresenceListManually
        return this
    }

    override var managePresenceListManually: Boolean = defaultConfiguration.managePresenceListManually
        private set

    /**
     * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
     *
     * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
     *
     * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
     *
     * Defaults to [PNReconnectionPolicy.NONE].
     */
    @Deprecated(
        """Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)""",
        level = DeprecationLevel.WARNING,
    )
    var reconnectionPolicy: PNReconnectionPolicy = PNReconnectionPolicy.NONE
        private set

    fun setReconnectionPolicy(reconnectionPolicy: PNReconnectionPolicy): PNConfiguration {
        this.reconnectionPolicy = reconnectionPolicy
        calculateRetryConfiguration()
        return this
    }

    private fun calculateRetryConfiguration() {
        retryConfiguration = when (reconnectionPolicy) {
            PNReconnectionPolicy.NONE -> RetryConfiguration.None
            PNReconnectionPolicy.LINEAR -> RetryConfiguration.Linear(
                maxRetryNumber = getMaximumReconnectionRetriesFor(reconnectionPolicy),
            )

            PNReconnectionPolicy.EXPONENTIAL -> RetryConfiguration.Exponential(
                maxRetryNumber = getMaximumReconnectionRetriesFor(reconnectionPolicy),
            )
        }
    }

    private fun getMaximumReconnectionRetriesFor(reconnectionPolicy: PNReconnectionPolicy): Int {
        val maxRetryNumber = if (reconnectionPolicy == PNReconnectionPolicy.LINEAR) {
            RetryConfiguration.Linear.MAX_RETRIES
        } else {
            RetryConfiguration.Exponential.MAX_RETRIES
        }
        return when {
            maximumReconnectionRetries <= -1 -> maxRetryNumber
            maximumReconnectionRetries > maxRetryNumber -> maxRetryNumber
            else -> maximumReconnectionRetries
        }
    }

    /**
     * Sets how many times to retry to reconnect before giving up.
     * Must be used in combination with [reconnectionPolicy].
     *
     * The default value is `-1` which means unlimited retries.
     */
    @Deprecated(
        """Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)""",
        level = DeprecationLevel.WARNING,
    )
    var maximumReconnectionRetries = -1
        private set

    fun setMaximumReconnectionRetries(maximumReconnectionRetries: Int): PNConfiguration {
        this.maximumReconnectionRetries = maximumReconnectionRetries
        calculateRetryConfiguration()
        return this
    }

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

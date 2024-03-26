package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.internal.v2.BasePNConfigurationImpl
import com.pubnub.internal.v2.BasePNConfigurationImpl.Companion.MINIMUM_PRESENCE_TIMEOUT
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

@Deprecated(
    message = "Use `com.pubnub.api.v2.PNConfiguration.builder` instead.",
    replaceWith = ReplaceWith("com.pubnub.api.v2.PNConfiguration.builder(userId, subscribeKey)"),
)
class PNConfiguration(override var userId: UserId) : BasePNConfiguration {
    private val configuration = BasePNConfigurationImpl(userId)
    private val log = LoggerFactory.getLogger("PNConfiguration")

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

    @Deprecated(
        "Use UserId instead e.g. config.userId.value",
        replaceWith = ReplaceWith("userId.value"),
        level = DeprecationLevel.WARNING,
    )
    override var uuid
        get() = userId.value
        set(value) {
            userId = UserId(value)
        }

    /**
     * The subscribe key from the admin panel.
     */
    override var subscribeKey = ""

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    override var publishKey = configuration.publishKey

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    override var secretKey = configuration.secretKey

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    override var authKey = configuration.authKey

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
    var useRandomInitializationVector = true

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    override var cryptoModule: CryptoModule? = configuration.cryptoModule
        get() = field ?: cipherKey?.let { cipherKey ->
            if (cipherKey.isNotBlank()) {
                log.warn("cipherKey is deprecated. Use CryptoModule instead")
                field =
                    CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = useRandomInitializationVector)
                field
            } else {
                null
            }
        }

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    override var origin = configuration.origin

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    override var secure = configuration.secure

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    override var logVerbosity = configuration.logVerbosity

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    override var heartbeatNotificationOptions = configuration.heartbeatNotificationOptions

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
        set(value) {
            field = value
            calculateRetryConfiguration()
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
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    override var presenceTimeout = configuration.presenceTimeout
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
     *
     * Defaults to 0 (disabled).
     */
    override var heartbeatInterval = configuration.heartbeatInterval

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    override var subscribeTimeout = configuration.subscribeTimeout

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    override var connectTimeout = configuration.connectTimeout

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    override var nonSubscribeRequestTimeout = configuration.nonSubscribeRequestTimeout

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    override var cacheBusting = configuration.cacheBusting

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    override var suppressLeaveEvents = configuration.suppressLeaveEvents

    /**
     * When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState]
     * for the current [userId] with every automatic heartbeat (if [heartbeatInterval] is greater than 0)
     * and initial subscribe connection (also after e.g. loss of network).
     *
     * Defaults to `true`.
     *
     * Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups.
     * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState],
     * otherwise that state may be overwritten by individual channel states.
     */
    override var maintainPresenceState = configuration.maintainPresenceState

    /**
     * Feature to subscribe with a custom filter expression.
     */
    override var filterExpression = configuration.filterExpression

    /**
     * Whether to include a instanceId with every request.
     *
     * Defaults to `false`.
     */
    override var includeInstanceIdentifier = configuration.includeInstanceIdentifier

    /**
     * Whether to include a requestId with every request.
     *
     * Defaults to `true`.
     */
    override var includeRequestIdentifier = configuration.includeRequestIdentifier

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
        set(value) {
            field = value
            calculateRetryConfiguration()
        }

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    override var maximumConnections = configuration.maximumConnections

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    override var googleAppEngineNetworking = configuration.googleAppEngineNetworking

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    override var proxy = configuration.proxy

    /**
     * @see [ProxySelector]
     */
    override var proxySelector = configuration.proxySelector

    /**
     * @see [Authenticator]
     */
    override var proxyAuthenticator = configuration.proxyAuthenticator

    /**
     * @see [CertificatePinner]
     */
    override var certificatePinner = configuration.certificatePinner

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    override var httpLoggingInterceptor = configuration.httpLoggingInterceptor

    /**
     * @see [SSLSocketFactory]
     */
    override var sslSocketFactory = configuration.sslSocketFactory

    /**
     * @see [X509ExtendedTrustManager]
     */
    override var x509ExtendedTrustManager = configuration.x509ExtendedTrustManager

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    override var connectionSpec = configuration.connectionSpec

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    override var hostnameVerifier = configuration.hostnameVerifier

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    override var fileMessagePublishRetryLimit = configuration.fileMessagePublishRetryLimit

    override var dedupOnSubscribe = configuration.dedupOnSubscribe

    override var maximumMessagesCacheSize = configuration.maximumMessagesCacheSize

    override val pnsdkSuffixes: MutableMap<String, String> = configuration.pnsdkSuffixes.toMutableMap()

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("To be used by components", level = DeprecationLevel.WARNING)
    fun addPnsdkSuffix(vararg nameToSuffixes: Pair<String, String>) {
        @Suppress("DEPRECATION")
        addPnsdkSuffix(nameToSuffixes.toMap())
    }

    @Deprecated("To be used by components", level = DeprecationLevel.WARNING)
    fun addPnsdkSuffix(nameToSuffixes: Map<String, String>) = pnsdkSuffixes.putAll(nameToSuffixes)

    override var retryConfiguration = configuration.retryConfiguration

    override var managePresenceListManually: Boolean = configuration.managePresenceListManually
}

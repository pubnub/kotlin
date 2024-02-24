package com.pubnub.internal

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.net.Proxy
import java.net.ProxySelector
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

/**
 * A storage for user-provided information which describe further PubNub client behaviour.
 * Configuration instance contains additional set of properties which
 * allow performing precise PubNub client configuration.
 *
 */
open class PNConfiguration @Throws(PubNubException::class) constructor(
    var userId: UserId
) {
    @Deprecated(
        replaceWith = ReplaceWith(
            "PNConfiguration(userId = UserId(uuid))",
            "com.pubnub.api.PNConfiguration"
        ),
        level = DeprecationLevel.WARNING,
        message = "Use PNConfiguration(UserId) instead."
    )
    constructor(uuid: String) : this(UserId(uuid))

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """Use UserId instead e.g. config.userId.value""",
        replaceWith = ReplaceWith("userId.value")
    )
    var uuid: String
        get() = userId.value
        set(value) {
            userId = UserId(value)
        }

    val log = LoggerFactory.getLogger("PNConfiguration")

    companion object {
        internal fun String.isValid() = isNotBlank()
        private const val DEFAULT_DEDUPE_SIZE = 100
        private const val PRESENCE_TIMEOUT = 300
        private const val MINIMUM_PRESENCE_TIMEOUT = 20
        private const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        private const val SUBSCRIBE_TIMEOUT = 310
        private const val CONNECT_TIMEOUT = 5
    }

    /**
     * The subscribe key from the admin panel.
     */
    var subscribeKey: String = ""

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    var publishKey: String = ""

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    var secretKey: String = ""

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    var authKey: String = ""

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
    )
    var cipherKey: String = ""

    /**
     * Should initialization vector for encrypted messages be random.
     *
     * Defaults to `false`.
     */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)""",
    )
    var useRandomInitializationVector = true

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    var cryptoModule: CryptoModule? = null
        get() = field ?: if (cipherKey.isNotBlank()) {
            log.warn("cipherKey is deprecated. Use CryptoModule instead")
            field =
                CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = useRandomInitializationVector)
            field
        } else null

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    var origin: String = ""

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    var secure = true

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    var logVerbosity = PNLogVerbosity.NONE

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    var heartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES

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
        level = DeprecationLevel.WARNING,
        message = """Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)""",
    )
    var reconnectionPolicy = PNReconnectionPolicy.NONE

    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    var presenceTimeout = PRESENCE_TIMEOUT
        set(value) {
            field =
                if (value < MINIMUM_PRESENCE_TIMEOUT) {
                    log.warn("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                    MINIMUM_PRESENCE_TIMEOUT
                } else value
            heartbeatInterval = (presenceTimeout / 2) - 1
        }

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    var heartbeatInterval = 0

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    var subscribeTimeout = SUBSCRIBE_TIMEOUT

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    var connectTimeout = CONNECT_TIMEOUT

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    var nonSubscribeRequestTimeout = NON_SUBSCRIBE_REQUEST_TIMEOUT

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    var cacheBusting = false

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    var suppressLeaveEvents = false

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
     * It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState],
     * otherwise that state may be overwritten by individual channel states.
     */
    var maintainPresenceState = true

    /**
     * Feature to subscribe with a custom filter expression.
     */
    var filterExpression: String = ""

    /**
     * Whether to include a [InternalPubNubClient.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    var includeInstanceIdentifier = false

    /**
     * Whether to include a [InternalPubNubClient.requestId] with every request.
     *
     * Defaults to `true`.
     */
    var includeRequestIdentifier = true

    /**
     * Sets how many times to retry to reconnect before giving up.
     * Must be used in combination with [reconnectionPolicy].
     *
     * The default value is `-1` which means unlimited retries.
     */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)""",
    )
    var maximumReconnectionRetries = -1

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    var maximumConnections: Int? = null

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    var googleAppEngineNetworking = false

    /**
     * Whether to start a separate subscriber thread when creating the instance.
     *
     * Defaults to `true`.
     */
    var startSubscriberThread = true

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    var proxy: Proxy? = null

    /**
     * @see [ProxySelector]
     */
    var proxySelector: ProxySelector? = null

    /**
     * @see [Authenticator]
     */
    var proxyAuthenticator: Authenticator? = null

    /**
     * @see [CertificatePinner]
     */
    var certificatePinner: CertificatePinner? = null

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    var httpLoggingInterceptor: HttpLoggingInterceptor? = null

    /**
     * @see [SSLSocketFactory]
     */
    var sslSocketFactory: SSLSocketFactory? = null

    /**
     * @see [X509ExtendedTrustManager]
     */
    var x509ExtendedTrustManager: X509ExtendedTrustManager? = null

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    var connectionSpec: ConnectionSpec? = null

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    var hostnameVerifier: HostnameVerifier? = null

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    var fileMessagePublishRetryLimit = 5

    var dedupOnSubscribe = false
    var maximumMessagesCacheSize = DEFAULT_DEDUPE_SIZE

    val pnsdkSuffixes: ConcurrentMap<String, String> = ConcurrentHashMap(mutableMapOf())

    fun generatePnsdk(version: String): String {
        val joinedSuffixes = pnsdkSuffixes.toSortedMap().values.joinToString(" ")
        return "PubNub-Kotlin/$version" + if (joinedSuffixes.isNotBlank()) " $joinedSuffixes" else ""
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("To be used by components", level = DeprecationLevel.WARNING)
    fun addPnsdkSuffix(vararg nameToSuffixes: Pair<String, String>) {
        @Suppress("DEPRECATION")
        addPnsdkSuffix(nameToSuffixes.toMap())
    }

    @Deprecated("To be used by components", level = DeprecationLevel.WARNING)
    fun addPnsdkSuffix(nameToSuffixes: Map<String, String>) = pnsdkSuffixes.putAll(nameToSuffixes)

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will vary from provided value by random value.
     */
    var retryConfiguration: RetryConfiguration = RetryConfiguration.None

    internal val retryConfForOldSubscribeLoop: RetryConfiguration by lazy {
        if (retryConfiguration != RetryConfiguration.None) {
            retryConfiguration
        } else {
            val defaultRetryNumberForLinear = 10
            val defaultRetryNumberForExponential = 6
            when (reconnectionPolicy) {
                PNReconnectionPolicy.NONE -> RetryConfiguration.None
                PNReconnectionPolicy.LINEAR -> RetryConfiguration.Linear(
                    maxRetryNumber = calculateMaximumReconnectionRetries(defaultRetryNumberForLinear),
                )

                PNReconnectionPolicy.EXPONENTIAL -> RetryConfiguration.Exponential(
                    maxRetryNumber = calculateMaximumReconnectionRetries(defaultRetryNumberForExponential),
                )
            }
        }
    }

    private fun calculateMaximumReconnectionRetries(maxRetryNumber: Int): Int {
        return when {
            maximumReconnectionRetries <= -1 -> maxRetryNumber
            maximumReconnectionRetries > maxRetryNumber -> maxRetryNumber
            else -> maximumReconnectionRetries
        }
    }
}

package com.pubnub.api

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.CorePNConfiguration
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

class PNConfiguration(userId: UserId) {
    internal val configuration = CorePNConfiguration(userId)

    var userId by configuration::userId

    @Deprecated(
        "Use UserId instead e.g. config.userId.value",
        replaceWith = ReplaceWith("userId.value"),
        level = DeprecationLevel.WARNING,
    )
    var uuid by configuration::uuid

    /**
     * The subscribe key from the admin panel.
     */
    var subscribeKey by configuration::subscribeKey

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    var publishKey by configuration::publishKey

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    var secretKey by configuration::secretKey

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    var authKey by configuration::authKey

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */
    @Deprecated(
        "Instead of cipherKey and useRandomInitializationVector use CryptoModule instead \n            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) \n            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)",
        level = DeprecationLevel.WARNING,
    )
    var cipherKey by configuration::cipherKey

    /**
     * Should initialization vector for encrypted messages be random.
     *
     * Defaults to `false`.
     */
    @Deprecated(
        "Instead of cipherKey and useRandomInitializationVector use CryptoModule instead \n            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) \n            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)",
        level = DeprecationLevel.WARNING,
    )
    var useRandomInitializationVector by configuration::useRandomInitializationVector

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    var cryptoModule by configuration::cryptoModule

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    var origin by configuration::origin

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    var secure by configuration::secure

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    var logVerbosity by configuration::logVerbosity

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    var heartbeatNotificationOptions by configuration::heartbeatNotificationOptions

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
        "Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration \n            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) \n            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)",
        level = DeprecationLevel.WARNING,
    )
    var reconnectionPolicy by configuration::reconnectionPolicy

    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    var presenceTimeout by configuration::presenceTimeout

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    var heartbeatInterval by configuration::heartbeatInterval

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    var subscribeTimeout by configuration::subscribeTimeout

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    var connectTimeout by configuration::connectTimeout

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    var nonSubscribeRequestTimeout by configuration::nonSubscribeRequestTimeout

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    var cacheBusting by configuration::cacheBusting

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    var suppressLeaveEvents by configuration::suppressLeaveEvents

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
    var maintainPresenceState by configuration::maintainPresenceState

    /**
     * Feature to subscribe with a custom filter expression.
     */
    var filterExpression by configuration::filterExpression

    /**
     * Whether to include a [InternalPubNubClient.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    var includeInstanceIdentifier by configuration::includeInstanceIdentifier

    /**
     * Whether to include a [InternalPubNubClient.requestId] with every request.
     *
     * Defaults to `true`.
     */
    var includeRequestIdentifier by configuration::includeRequestIdentifier

    /**
     * Sets how many times to retry to reconnect before giving up.
     * Must be used in combination with [reconnectionPolicy].
     *
     * The default value is `-1` which means unlimited retries.
     */
    @Deprecated(
        "Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration \n            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) \n            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)",
        level = DeprecationLevel.WARNING,
    )
    var maximumReconnectionRetries by configuration::maximumReconnectionRetries

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    var maximumConnections by configuration::maximumConnections

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    var googleAppEngineNetworking by configuration::googleAppEngineNetworking

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    var proxy by configuration::proxy

    /**
     * @see [ProxySelector]
     */
    var proxySelector by configuration::proxySelector

    /**
     * @see [Authenticator]
     */
    var proxyAuthenticator by configuration::proxyAuthenticator

    /**
     * @see [CertificatePinner]
     */
    var certificatePinner by configuration::certificatePinner

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    var httpLoggingInterceptor by configuration::httpLoggingInterceptor

    /**
     * @see [SSLSocketFactory]
     */
    var sslSocketFactory by configuration::sslSocketFactory

    /**
     * @see [X509ExtendedTrustManager]
     */
    var x509ExtendedTrustManager by configuration::x509ExtendedTrustManager

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    var connectionSpec by configuration::connectionSpec

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    var hostnameVerifier by configuration::hostnameVerifier

    /**
     * How many times publishing file message should automatically retry before marking the action as failed
     *
     * Defaults to `5`
     */
    var fileMessagePublishRetryLimit by configuration::fileMessagePublishRetryLimit

    var dedupOnSubscribe by configuration::dedupOnSubscribe

    var maximumMessagesCacheSize by configuration::maximumMessagesCacheSize

    /**
     * Retry configuration for requests.
     *  Defaults to [RetryConfiguration.None].
     *
     *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
     *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
     *  Delay will vary from provided value by random value.
     */
    var retryConfiguration by configuration::retryConfiguration
}

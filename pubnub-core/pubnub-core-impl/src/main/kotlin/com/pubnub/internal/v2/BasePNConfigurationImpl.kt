package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.BasePNConfigurationOverride
import com.pubnub.api.v2.subscriptions.ConversationContext
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
open class BasePNConfigurationImpl internal constructor(
    override val userId: UserId,
    override val subscribeKey: String = "",
    override val publishKey: String = "",
    override val secretKey: String = "",
    override val authKey: String = "",
    override val cryptoModule: CryptoModule? = null,
    override val origin: String = "",
    override val secure: Boolean = true,
    override val logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
    override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES,
    override val presenceTimeout: Int = PRESENCE_TIMEOUT,
    override val heartbeatInterval: Int = 0,
    override val subscribeTimeout: Int = SUBSCRIBE_TIMEOUT,
    override val connectTimeout: Int = CONNECT_TIMEOUT,
    override val nonSubscribeReadTimeout: Int = NON_SUBSCRIBE_REQUEST_TIMEOUT,
    override val cacheBusting: Boolean = false,
    override val suppressLeaveEvents: Boolean = false,
    override val maintainPresenceState: Boolean = true,
    override val filterExpression: String = "",
    override val includeInstanceIdentifier: Boolean = false,
    override val includeRequestIdentifier: Boolean = true,
    override val maximumConnections: Int? = null,
    override val googleAppEngineNetworking: Boolean = false,
    override val proxy: Proxy? = null,
    override val proxySelector: ProxySelector? = null,
    override val proxyAuthenticator: Authenticator? = null,
    override val certificatePinner: CertificatePinner? = null,
    override val httpLoggingInterceptor: HttpLoggingInterceptor? = null,
    override val sslSocketFactory: SSLSocketFactory? = null,
    override val x509ExtendedTrustManager: X509ExtendedTrustManager? = null,
    override val connectionSpec: ConnectionSpec? = null,
    override val hostnameVerifier: HostnameVerifier? = null,
    override val fileMessagePublishRetryLimit: Int = 5,
    override val dedupOnSubscribe: Boolean = false,
    override val maximumMessagesCacheSize: Int = DEFAULT_DEDUPE_SIZE,
    override val pnsdkSuffixes: Map<String, String> = emptyMap(),
    override val retryConfiguration: RetryConfiguration = RetryConfiguration.None,
    override val managePresenceListManually: Boolean = false,
    override var conversationContext: ConversationContext = ConversationContext.NONE,
    override var apiKey: String? = null,
    override var aiProvider: String? = null,
    override var webHookUrl: String? = null,
) : BasePNConfiguration {
    companion object {
        const val DEFAULT_DEDUPE_SIZE = 100
        const val PRESENCE_TIMEOUT = 300
        const val MINIMUM_PRESENCE_TIMEOUT = 20
        const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        const val SUBSCRIBE_TIMEOUT = 310
        const val CONNECT_TIMEOUT = 5
    }

    constructor(
        userId: UserId,
    ) : this(userId, "")

    abstract class Builder(defaultConfiguration: BasePNConfiguration) : BasePNConfiguration.Builder, BasePNConfigurationOverride.Builder {
        override val userId: UserId = defaultConfiguration.userId

        override val subscribeKey: String = defaultConfiguration.subscribeKey

        override val publishKey: String = defaultConfiguration.publishKey

        override val secretKey: String = defaultConfiguration.secretKey

        override val authKey: String = defaultConfiguration.authKey

        override val cryptoModule: CryptoModule? = defaultConfiguration.cryptoModule

        override val origin: String = defaultConfiguration.origin

        override val secure: Boolean = defaultConfiguration.secure

        override val logVerbosity: PNLogVerbosity = defaultConfiguration.logVerbosity

        override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions =
            defaultConfiguration.heartbeatNotificationOptions

        override val presenceTimeout: Int = defaultConfiguration.presenceTimeout

        override val heartbeatInterval: Int = defaultConfiguration.heartbeatInterval

        override val subscribeTimeout: Int = defaultConfiguration.subscribeTimeout

        override val connectTimeout: Int = defaultConfiguration.connectTimeout

        override val nonSubscribeReadTimeout: Int = defaultConfiguration.nonSubscribeReadTimeout

        override val cacheBusting: Boolean = defaultConfiguration.cacheBusting

        override val suppressLeaveEvents: Boolean = defaultConfiguration.suppressLeaveEvents

        override val maintainPresenceState: Boolean = defaultConfiguration.maintainPresenceState

        override val filterExpression: String = defaultConfiguration.filterExpression

        override val includeInstanceIdentifier: Boolean = defaultConfiguration.includeInstanceIdentifier

        override val includeRequestIdentifier: Boolean = defaultConfiguration.includeRequestIdentifier

        override val maximumConnections: Int? = defaultConfiguration.maximumConnections

        override val googleAppEngineNetworking: Boolean = defaultConfiguration.googleAppEngineNetworking

        override val proxy: Proxy? = defaultConfiguration.proxy

        override val proxySelector: ProxySelector? = defaultConfiguration.proxySelector

        override val proxyAuthenticator: Authenticator? = defaultConfiguration.proxyAuthenticator

        override val certificatePinner: CertificatePinner? = defaultConfiguration.certificatePinner

        override val httpLoggingInterceptor: HttpLoggingInterceptor? = defaultConfiguration.httpLoggingInterceptor

        override val sslSocketFactory: SSLSocketFactory? = defaultConfiguration.sslSocketFactory

        override val x509ExtendedTrustManager: X509ExtendedTrustManager? = defaultConfiguration.x509ExtendedTrustManager

        override val connectionSpec: ConnectionSpec? = defaultConfiguration.connectionSpec

        override val hostnameVerifier: HostnameVerifier? = defaultConfiguration.hostnameVerifier

        override val fileMessagePublishRetryLimit: Int = defaultConfiguration.fileMessagePublishRetryLimit
        override val dedupOnSubscribe: Boolean = defaultConfiguration.dedupOnSubscribe
        override val maximumMessagesCacheSize: Int = defaultConfiguration.maximumMessagesCacheSize
        override val pnsdkSuffixes: Map<String, String> = defaultConfiguration.pnsdkSuffixes

        override val retryConfiguration: RetryConfiguration = defaultConfiguration.retryConfiguration

        override val managePresenceListManually: Boolean = defaultConfiguration.managePresenceListManually
        override var conversationContext: ConversationContext = defaultConfiguration.conversationContext
        override var apiKey: String? = defaultConfiguration.apiKey
        override var aiProvider: String? = defaultConfiguration.aiProvider
        override var webHookUrl: String? = defaultConfiguration.webHookUrl
    }
}

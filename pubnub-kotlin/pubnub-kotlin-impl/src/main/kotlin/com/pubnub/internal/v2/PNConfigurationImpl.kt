package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
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

class PNConfigurationImpl(
    override val userId: UserId,
    override val subscribeKey: String,
    override val publishKey: String,
    override val secretKey: String,
    override val authKey: String,
    override val cryptoModule: CryptoModule?,
    override val origin: String,
    override val secure: Boolean,
    override val logVerbosity: PNLogVerbosity,
    override val heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
    override val presenceTimeout: Int,
    override val heartbeatInterval: Int,
    override val subscribeTimeout: Int,
    override val connectTimeout: Int,
    override val nonSubscribeReadTimeout: Int,
    override val cacheBusting: Boolean,
    override val suppressLeaveEvents: Boolean,
    override val maintainPresenceState: Boolean,
    override val filterExpression: String,
    override val includeInstanceIdentifier: Boolean,
    override val includeRequestIdentifier: Boolean,
    override val maximumConnections: Int?,
    override val googleAppEngineNetworking: Boolean,
    override val proxy: Proxy?,
    override val proxySelector: ProxySelector?,
    override val proxyAuthenticator: Authenticator?,
    override val certificatePinner: CertificatePinner?,
    override val httpLoggingInterceptor: HttpLoggingInterceptor?,
    override val sslSocketFactory: SSLSocketFactory?,
    override val x509ExtendedTrustManager: X509ExtendedTrustManager?,
    override val connectionSpec: ConnectionSpec?,
    override val hostnameVerifier: HostnameVerifier?,
    override val fileMessagePublishRetryLimit: Int,
    override val dedupOnSubscribe: Boolean,
    override val maximumMessagesCacheSize: Int,
    override val pnsdkSuffixes: Map<String, String>,
    override val retryConfiguration: RetryConfiguration,
    override val managePresenceListManually: Boolean,
) : BasePNConfigurationImpl(userId), PNConfiguration, PNConfigurationOverride {
    class Builder internal constructor(defaultConfiguration: BasePNConfiguration) :
        BasePNConfigurationImpl.Builder(defaultConfiguration),
        PNConfiguration.Builder,
        PNConfigurationOverride.Builder {
            private val log = LoggerFactory.getLogger(this::class.simpleName)

            override var userId: UserId = super.userId

            override var subscribeKey: String = super.subscribeKey

            override var publishKey: String = super.publishKey

            override var secretKey: String = super.secretKey

            override var authKey: String = super.authKey

            override var cryptoModule: CryptoModule? = super.cryptoModule

            override var origin: String = super.origin

            override var secure: Boolean = super.secure

            override var logVerbosity: PNLogVerbosity = super.logVerbosity

            override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions = super.heartbeatNotificationOptions

            override var presenceTimeout: Int = super.presenceTimeout
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

            override var heartbeatInterval: Int = super.heartbeatInterval

            override var subscribeTimeout: Int = super.subscribeTimeout

            override var connectTimeout: Int = super.connectTimeout

            @Deprecated(
                "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
                replaceWith = ReplaceWith("nonSubscribeReadTimeout")
            )
            override var nonSubscribeRequestTimeout: Int
                get() = nonSubscribeReadTimeout
                set(value) {
                    nonSubscribeReadTimeout = value
                }

            override var nonSubscribeReadTimeout: Int = super.nonSubscribeReadTimeout

            override var cacheBusting: Boolean = super.cacheBusting

            override var suppressLeaveEvents: Boolean = super.suppressLeaveEvents

            override var maintainPresenceState: Boolean = super.maintainPresenceState

            override var filterExpression: String = super.filterExpression

            override var includeInstanceIdentifier: Boolean = super.includeInstanceIdentifier

            override var includeRequestIdentifier: Boolean = super.includeRequestIdentifier

            override var maximumConnections: Int? = super.maximumConnections

            override var googleAppEngineNetworking: Boolean = super.googleAppEngineNetworking

            override var proxy: Proxy? = super.proxy

            override var proxySelector: ProxySelector? = super.proxySelector

            override var proxyAuthenticator: Authenticator? = super.proxyAuthenticator

            override var certificatePinner: CertificatePinner? = super.certificatePinner

            override var httpLoggingInterceptor: HttpLoggingInterceptor? = super.httpLoggingInterceptor

            override var sslSocketFactory: SSLSocketFactory? = super.sslSocketFactory

            override var x509ExtendedTrustManager: X509ExtendedTrustManager? = super.x509ExtendedTrustManager

            override var connectionSpec: ConnectionSpec? = super.connectionSpec

            override var hostnameVerifier: HostnameVerifier? = super.hostnameVerifier

            override var fileMessagePublishRetryLimit: Int = super.fileMessagePublishRetryLimit
            override var dedupOnSubscribe: Boolean = super.dedupOnSubscribe
            override var maximumMessagesCacheSize: Int = super.maximumMessagesCacheSize
            override var pnsdkSuffixes: Map<String, String> = super.pnsdkSuffixes

            override var retryConfiguration: RetryConfiguration = super.retryConfiguration

            override var managePresenceListManually: Boolean = super.managePresenceListManually

            override fun build(): PNConfiguration {
                return PNConfigurationImpl(
                    userId = userId,
                    subscribeKey = subscribeKey,
                    publishKey = publishKey,
                    secretKey = secretKey,
                    authKey = authKey,
                    cryptoModule = cryptoModule,
                    origin = origin,
                    secure = secure,
                    logVerbosity = logVerbosity,
                    heartbeatNotificationOptions = heartbeatNotificationOptions,
                    presenceTimeout = presenceTimeout,
                    heartbeatInterval = heartbeatInterval,
                    subscribeTimeout = subscribeTimeout,
                    connectTimeout = connectTimeout,
                    nonSubscribeReadTimeout = nonSubscribeReadTimeout,
                    cacheBusting = cacheBusting,
                    suppressLeaveEvents = suppressLeaveEvents,
                    maintainPresenceState = maintainPresenceState,
                    filterExpression = filterExpression,
                    includeInstanceIdentifier = includeInstanceIdentifier,
                    includeRequestIdentifier = includeRequestIdentifier,
                    maximumConnections = maximumConnections,
                    googleAppEngineNetworking = googleAppEngineNetworking,
                    proxy = proxy,
                    proxySelector = proxySelector,
                    proxyAuthenticator = proxyAuthenticator,
                    certificatePinner = certificatePinner,
                    httpLoggingInterceptor = httpLoggingInterceptor,
                    sslSocketFactory = sslSocketFactory,
                    x509ExtendedTrustManager = x509ExtendedTrustManager,
                    connectionSpec = connectionSpec,
                    hostnameVerifier = hostnameVerifier,
                    fileMessagePublishRetryLimit = fileMessagePublishRetryLimit,
                    dedupOnSubscribe = dedupOnSubscribe,
                    maximumMessagesCacheSize = maximumMessagesCacheSize,
                    pnsdkSuffixes = pnsdkSuffixes,
                    retryConfiguration = retryConfiguration,
                    managePresenceListManually = managePresenceListManually,
                )
            }
        }
}

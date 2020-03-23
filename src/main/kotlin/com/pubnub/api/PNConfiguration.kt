package com.pubnub.api

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import java.util.UUID
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager


class PNConfiguration {

    private companion object Constants {
        private const val DEFAULT_DEDUPE_SIZE = 100
        private const val PRESENCE_TIMEOUT = 300
        private const val MINIMUM_PRESENCE_TIMEOUT = 20
        private const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        private const val SUBSCRIBE_TIMEOUT = 310
        private const val CONNECT_TIMEOUT = 5
        private const val DEFAULT_BASE_PATH = "ps.pndsn.com"
    }

    lateinit var subscribeKey: String
    lateinit var publishKey: String
    lateinit var secretKey: String
    lateinit var authKey: String
    lateinit var cipherKey: String

    var uuid: String = "pn-${UUID.randomUUID()}"

    var origin = DEFAULT_BASE_PATH
    var secure = true

    var logVerbosity = PNLogVerbosity.NONE
    var heartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES
    var reconnectionPolicy = PNReconnectionPolicy.NONE

    var presenceTimeout = PRESENCE_TIMEOUT
        set(value) {
            field =
                if (value < MINIMUM_PRESENCE_TIMEOUT) {
                    println("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                    MINIMUM_PRESENCE_TIMEOUT
                } else value
            heartbeatInterval = (presenceTimeout / 2) - 1
        }

    var heartbeatInterval = 0

    var subscribeTimeout = SUBSCRIBE_TIMEOUT
    var connectTimeout = CONNECT_TIMEOUT
    var nonSubscribeRequestTimeout =
        NON_SUBSCRIBE_REQUEST_TIMEOUT
    var maximumMessagesCacheSize = DEFAULT_DEDUPE_SIZE

    var suppressLeaveEvents = false
    var disableTokenManager = false
    lateinit var filterExpression: String
    var includeInstanceIdentifier = false
    var includeRequestIdentifier = true
    var maximumReconnectionRetries = -1
    var maximumConnections: Int? = null
    var requestMessageCountThreshold: Int? = null
    var googleAppEngineNetworking = false
    var startSubscriberThread = true
    var dedupOnSubscribe = true

    var proxy: Proxy? = null
    var proxySelector: ProxySelector? = null
    var proxyAuthenticator: Authenticator? = null
    var certificatePinner: CertificatePinner? = null
    var httpLoggingInterceptor: HttpLoggingInterceptor? = null
    var sslSocketFactory: SSLSocketFactory? = null
    var x509ExtendedTrustManager: X509ExtendedTrustManager? = null
    var connectionSpec: ConnectionSpec? = null
    var hostnameVerifier: HostnameVerifier? = null

    internal fun isSubscribeKeyValid() = ::subscribeKey.isInitialized && !subscribeKey.isBlank()
    internal fun isAuthKeyValid() = ::authKey.isInitialized && !authKey.isBlank()
    internal fun isCipherKeyValid() = ::cipherKey.isInitialized && !cipherKey.isBlank()
    internal fun isPublishKeyValid() = ::publishKey.isInitialized && !publishKey.isBlank()
    internal fun isSecretKeyValid() = ::secretKey.isInitialized && !secretKey.isBlank()
    internal fun isFilterExpressionKeyValid(function: String.() -> Unit) {
        if (::filterExpression.isInitialized && !filterExpression.isBlank()) {
            function.invoke(filterExpression)
        }
    }
}

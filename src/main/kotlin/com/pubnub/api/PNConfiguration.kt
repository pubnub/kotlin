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
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager


class PNConfiguration {

    companion object Constants {
        private const val DEFAULT_DEDUPE_SIZE = 100
        private const val PRESENCE_TIMEOUT = 300
        private const val MINIMUM_PRESENCE_TIMEOUT = 20
        private const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        private const val SUBSCRIBE_TIMEOUT = 310
        private const val CONNECT_TIMEOUT = 5
        private const val DEFAULT_BASE_PATH = "ps.pndsn.com"
    }

    var subscribeKey: String? = null
    var publishKey: String? = null
    var secretKey: String? = null
    var uuid: String = "pn-${UUID.randomUUID()}"
    var authKey: String? = null
    var cipherKey: String? = null

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
    var nonSubscribeRequestTimeout = NON_SUBSCRIBE_REQUEST_TIMEOUT
    var maximumMessagesCacheSize = DEFAULT_DEDUPE_SIZE

    var suppressLeaveEvents = false
    var disableTokenManager = false
    var filterExpression: String? = null
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
}
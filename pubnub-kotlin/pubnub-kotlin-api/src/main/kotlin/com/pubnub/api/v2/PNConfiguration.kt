package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.v2.BasePNConfigurationImpl
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.net.ProxySelector
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

interface PNConfiguration : BasePNConfiguration, PNConfigurationOverride {
    companion object {
        @JvmStatic
        fun builder(
            userId: UserId,
            subscribeKey: String,
            action: Builder.() -> Unit = {},
        ): Builder {
            return (
                Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                    .getConstructor(BasePNConfiguration::class.java)
                    .newInstance(object : BasePNConfigurationImpl(userId) {
                        override val subscribeKey: String = subscribeKey
                    }) as Builder
            ).apply(action)
        }
    }

    interface Builder : BasePNConfiguration.Builder {
        override var userId: UserId
        override var subscribeKey: String
        override var publishKey: String
        override var secretKey: String
        override var authKey: String
        override var cryptoModule: CryptoModule?
        override var origin: String
        override var secure: Boolean
        override var logVerbosity: PNLogVerbosity
        override var heartbeatNotificationOptions: PNHeartbeatNotificationOptions
        override var presenceTimeout: Int
        override var heartbeatInterval: Int
        override var subscribeTimeout: Int
        override var connectTimeout: Int
        override var nonSubscribeRequestTimeout: Int
        override var cacheBusting: Boolean
        override var suppressLeaveEvents: Boolean
        override var maintainPresenceState: Boolean
        override var filterExpression: String
        override var includeInstanceIdentifier: Boolean
        override var includeRequestIdentifier: Boolean
        override var maximumConnections: Int?
        override var googleAppEngineNetworking: Boolean
        override var proxy: Proxy?
        override var proxySelector: ProxySelector?
        override var proxyAuthenticator: Authenticator?
        override var certificatePinner: CertificatePinner?
        override var httpLoggingInterceptor: HttpLoggingInterceptor?
        override var sslSocketFactory: SSLSocketFactory?
        override var x509ExtendedTrustManager: X509ExtendedTrustManager?
        override var connectionSpec: ConnectionSpec?
        override var hostnameVerifier: HostnameVerifier?
        override var fileMessagePublishRetryLimit: Int
        override var dedupOnSubscribe: Boolean
        override var maximumMessagesCacheSize: Int
        override var pnsdkSuffixes: Map<String, String>
        override var retryConfiguration: RetryConfiguration
        override var managePresenceListManually: Boolean

        fun build(): PNConfiguration
    }
}

interface PNConfigurationOverride {
    interface Builder : BasePNConfigurationOverride.Builder {
        /**
         * The subscribe key from the admin panel.
         */
        override var subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        override var publishKey: String

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will valy from provided value by random value.
         */
        override var retryConfiguration: RetryConfiguration

        /**
         * The user ID that the PubNub client will use.
         */
        override var userId: UserId

        /**
         * Whether to include a [PubNub.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        override var includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNub.requestId] with every request.
         *
         * Defaults to `true`.
         */
        override var includeRequestIdentifier: Boolean

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        override var authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        override var cryptoModule: CryptoModule?

        fun build(): PNConfigurationOverride
    }
}
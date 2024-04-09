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

interface PNConfiguration : BasePNConfiguration {
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

        @Deprecated(
            "This setting relates to *read* timeout and was renamed to `nonSubscribeReadTimeout`",
            replaceWith = ReplaceWith("nonSubscribeReadTimeout")
        )
        override var nonSubscribeRequestTimeout: Int
            get() = nonSubscribeReadTimeout
            set(value) {
                nonSubscribeReadTimeout = value
            }
        override var nonSubscribeReadTimeout: Int
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

        /**
         * Create a [PNConfiguration] object with values from this builder.
         */
        fun build(): PNConfiguration
    }
}

interface PNConfigurationOverride : BasePNConfigurationOverride {
    companion object {
        @JvmStatic
        fun from(configuration: BasePNConfiguration): Builder {
            return Class.forName("com.pubnub.internal.v2.PNConfigurationImpl\$Builder")
                .getConstructor(BasePNConfiguration::class.java)
                .newInstance(configuration) as Builder
        }
    }

    interface Builder : BasePNConfigurationOverride.Builder {
        override var subscribeKey: String
        override var publishKey: String
        override var secretKey: String
        override var retryConfiguration: RetryConfiguration
        override var userId: UserId
        override var includeInstanceIdentifier: Boolean
        override var includeRequestIdentifier: Boolean
        override var authKey: String
        override var cryptoModule: CryptoModule?
        override var connectTimeout: Int
        override var nonSubscribeReadTimeout: Int

        /**
         * Create a [PNConfiguration] object with values from this builder.
         */
        fun build(): PNConfiguration
    }
}

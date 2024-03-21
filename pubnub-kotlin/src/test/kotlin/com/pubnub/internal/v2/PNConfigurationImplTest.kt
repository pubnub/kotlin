package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.BasePubNubImpl
import io.mockk.mockk
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.internal.tls.OkHostnameVerifier
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Test
import sun.net.spi.DefaultProxySelector
import sun.security.ssl.SSLSocketFactoryImpl
import java.net.InetSocketAddress
import java.net.Proxy
import javax.net.ssl.X509ExtendedTrustManager

class PNConfigurationImplTest {
    @Test
    fun testDefaultTimeoutValues() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        assertEquals(300, config.presenceTimeout)
        assertEquals(0, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues1() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.presenceTimeout = 100
        assertEquals(100, config.presenceTimeout)
        assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.heartbeatInterval = 100
        assertEquals(300, config.presenceTimeout)
        assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.heartbeatInterval = 40
        config.presenceTimeout = 50
        assertEquals(50, config.presenceTimeout)
        assertEquals(24, config.heartbeatInterval)
    }

    @Test
    fun buildUsesAllValues() {
        val expectedUserId = UserId(BasePubNubImpl.generateUUID())
        val expectedCryptoModule = CryptoModule.createAesCbcCryptoModule("cipher")
        val expectedProxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(80))
        val expectedProxySelector = DefaultProxySelector()
        val expectedAuthenticator = Authenticator { route, response -> null }
        val expectedPinner = CertificatePinner.DEFAULT
        val expectedInterceptor = HttpLoggingInterceptor()
        val expectedSslSocketFactory = SSLSocketFactoryImpl()
        val expectedTrustManager: X509ExtendedTrustManager = mockk()
        val expectedConnSpec = ConnectionSpec.RESTRICTED_TLS
        val expectedVerifier = OkHostnameVerifier
        val expectedSuffixes = mapOf("abc" to "def")
        val expectedConfiguration = RetryConfiguration.Exponential()
        val config = PNConfiguration.builder(expectedUserId, "subKey") {
            publishKey = "publishKey"
            secretKey = "secretKey"
            authKey = "authKey"
            cryptoModule = expectedCryptoModule
            origin = "origin"
            secure = false
            logVerbosity = PNLogVerbosity.BODY
            heartbeatNotificationOptions = PNHeartbeatNotificationOptions.NONE
            presenceTimeout = 1
            heartbeatInterval = 2
            subscribeTimeout = 3
            connectTimeout = 4
            nonSubscribeRequestTimeout = 5
            cacheBusting = true
            suppressLeaveEvents = true
            maintainPresenceState = false
            filterExpression = "filter"
            includeInstanceIdentifier = true
            includeRequestIdentifier = false
            maximumConnections = 1
            googleAppEngineNetworking = true
            proxy = expectedProxy
            proxySelector = expectedProxySelector
            proxyAuthenticator = expectedAuthenticator
            certificatePinner = expectedPinner
            httpLoggingInterceptor = expectedInterceptor
            sslSocketFactory = expectedSslSocketFactory
            x509ExtendedTrustManager = expectedTrustManager
            connectionSpec = expectedConnSpec
            hostnameVerifier = expectedVerifier
            fileMessagePublishRetryLimit = 1
            dedupOnSubscribe = true
            maximumMessagesCacheSize = 2
            pnsdkSuffixes = expectedSuffixes
            retryConfiguration = expectedConfiguration
            managePresenceListManually = true
        }

        val configuration = config.build()
        assertEquals("publishKey", configuration.publishKey)
        assertEquals("secretKey", configuration.secretKey)
        assertEquals("authKey", configuration.authKey)
        assertEquals(expectedCryptoModule, configuration.cryptoModule)
        assertEquals("origin", configuration.origin)
        assertEquals(false, configuration.secure)
        assertEquals(PNLogVerbosity.BODY, configuration.logVerbosity)
        assertEquals(PNHeartbeatNotificationOptions.NONE, configuration.heartbeatNotificationOptions)
        assertEquals(20, configuration.presenceTimeout)
        assertEquals(2, configuration.heartbeatInterval)
        assertEquals(3, configuration.subscribeTimeout)
        assertEquals(4, configuration.connectTimeout)
        assertEquals(5, configuration.nonSubscribeRequestTimeout)
        assertEquals(true, configuration.cacheBusting)
        assertEquals(true, configuration.suppressLeaveEvents)
        assertEquals(false, configuration.maintainPresenceState)
        assertEquals("filter", configuration.filterExpression)
        assertEquals(true, configuration.includeInstanceIdentifier)
        assertEquals(false, configuration.includeRequestIdentifier)
        assertEquals(1, configuration.maximumConnections)
        assertEquals(true, configuration.googleAppEngineNetworking)
        assertEquals(expectedProxy, configuration.proxy)
        assertEquals(expectedProxySelector, configuration.proxySelector)
        assertEquals(expectedAuthenticator, configuration.proxyAuthenticator)
        assertEquals(expectedPinner, configuration.certificatePinner)
        assertEquals(expectedInterceptor, configuration.httpLoggingInterceptor)
        assertEquals(expectedSslSocketFactory, configuration.sslSocketFactory)
        assertEquals(expectedTrustManager, configuration.x509ExtendedTrustManager)
        assertEquals(expectedConnSpec, configuration.connectionSpec)
        assertEquals(expectedVerifier, configuration.hostnameVerifier)
        assertEquals(1, configuration.fileMessagePublishRetryLimit)
        assertEquals(true, configuration.dedupOnSubscribe)
        assertEquals(2, configuration.maximumMessagesCacheSize)
        assertEquals(expectedSuffixes, configuration.pnsdkSuffixes)
        assertEquals(expectedConfiguration, configuration.retryConfiguration)
        assertEquals(true, configuration.managePresenceListManually)
    }
}

package com.pubnub.internal.v2

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.internal.PubNubImpl
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
    @Test(expected = PubNubException::class)
    fun setUUIDToEmptyString() {
        PNConfiguration.builder(UserId(""), "")
    }

    @Test(expected = PubNubException::class)
    fun resetUUIDToEmptyString() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "")
        config.userId = UserId("")
    }

    @Test
    fun resetUUIDToNonEmptyString() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "")
        val newUUID = PubNubImpl.generateUUID()
        config.userId = UserId(newUUID)

        assertEquals(newUUID, config.userId.value)
    }

    @Test
    fun `create config override from existing config`() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "expectedSubscribe").build()
        val override = PNConfigurationOverride.from(config).apply {
            userId = UserId("override")
        }.build()
        assertEquals("override", override.userId.value)
        assertEquals("expectedSubscribe", override.subscribeKey)
    }

    @Test
    fun testDefaultTimeoutValues() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "demo")
        assertEquals(300, config.presenceTimeout)
        assertEquals(0, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues1() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "demo")
        config.presenceTimeout = 100
        assertEquals(100, config.presenceTimeout)
        assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "demo")
        config.heartbeatInterval = 100
        assertEquals(300, config.presenceTimeout)
        assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration.builder(UserId(PubNubImpl.generateUUID()), "demo")
        config.heartbeatInterval = 40
        config.presenceTimeout = 50
        assertEquals(50, config.presenceTimeout)
        assertEquals(24, config.heartbeatInterval)
    }

    @Test
    fun `build uses all values from Builder`() {
        val expectedUserId = UserId(PubNubImpl.generateUUID())
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
        assertEquals(expectedUserId, configuration.userId)
        assertEquals("subKey", configuration.subscribeKey)
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

    @Test
    fun `builder has all default values from PNConfiguration`() {
        val expectedUserId = UserId(PubNubImpl.generateUUID())
        val builder = PNConfiguration.builder(expectedUserId, "subKey")
        val expectedDefaults = PNConfigurationImpl(expectedUserId)

        assertEquals(expectedUserId, builder.userId)
        assertEquals("subKey", builder.subscribeKey)
        assertEquals(expectedDefaults.publishKey, builder.publishKey)
        assertEquals(expectedDefaults.secretKey, builder.secretKey)
        assertEquals(expectedDefaults.authKey, builder.authKey)
        assertEquals(expectedDefaults.cryptoModule, builder.cryptoModule)
        assertEquals(expectedDefaults.origin, builder.origin)
        assertEquals(expectedDefaults.secure, builder.secure)
        assertEquals(expectedDefaults.logVerbosity, builder.logVerbosity)
        assertEquals(expectedDefaults.heartbeatNotificationOptions, builder.heartbeatNotificationOptions)
        assertEquals(expectedDefaults.presenceTimeout, builder.presenceTimeout)
        assertEquals(expectedDefaults.heartbeatInterval, builder.heartbeatInterval)
        assertEquals(expectedDefaults.subscribeTimeout, builder.subscribeTimeout)
        assertEquals(expectedDefaults.connectTimeout, builder.connectTimeout)
        assertEquals(expectedDefaults.nonSubscribeRequestTimeout, builder.nonSubscribeRequestTimeout)
        assertEquals(expectedDefaults.cacheBusting, builder.cacheBusting)
        assertEquals(expectedDefaults.suppressLeaveEvents, builder.suppressLeaveEvents)
        assertEquals(expectedDefaults.maintainPresenceState, builder.maintainPresenceState)
        assertEquals(expectedDefaults.filterExpression, builder.filterExpression)
        assertEquals(expectedDefaults.includeInstanceIdentifier, builder.includeInstanceIdentifier)
        assertEquals(expectedDefaults.includeRequestIdentifier, builder.includeRequestIdentifier)
        assertEquals(expectedDefaults.maximumConnections, builder.maximumConnections)
        assertEquals(expectedDefaults.googleAppEngineNetworking, builder.googleAppEngineNetworking)
        assertEquals(expectedDefaults.proxy, builder.proxy)
        assertEquals(expectedDefaults.proxySelector, builder.proxySelector)
        assertEquals(expectedDefaults.proxyAuthenticator, builder.proxyAuthenticator)
        assertEquals(expectedDefaults.certificatePinner, builder.certificatePinner)
        assertEquals(expectedDefaults.httpLoggingInterceptor, builder.httpLoggingInterceptor)
        assertEquals(expectedDefaults.sslSocketFactory, builder.sslSocketFactory)
        assertEquals(expectedDefaults.x509ExtendedTrustManager, builder.x509ExtendedTrustManager)
        assertEquals(expectedDefaults.connectionSpec, builder.connectionSpec)
        assertEquals(expectedDefaults.hostnameVerifier, builder.hostnameVerifier)
        assertEquals(expectedDefaults.fileMessagePublishRetryLimit, builder.fileMessagePublishRetryLimit)
        assertEquals(expectedDefaults.dedupOnSubscribe, builder.dedupOnSubscribe)
        assertEquals(expectedDefaults.maximumMessagesCacheSize, builder.maximumMessagesCacheSize)
        assertEquals(expectedDefaults.pnsdkSuffixes, builder.pnsdkSuffixes)
        assertEquals(expectedDefaults.retryConfiguration, builder.retryConfiguration)
        assertEquals(expectedDefaults.managePresenceListManually, builder.managePresenceListManually)
    }

    @Test
    fun `can reset userId and subscribeKey`() {
        val expectedUserId = UserId(PubNubImpl.generateUUID())
        val expectedSubKey = "expectedSubKey"

        val config = PNConfiguration.builder(UserId("aaa"), "subKey") {
            userId = expectedUserId
            subscribeKey = expectedSubKey
        }

        val configuration = config.build()
        assertEquals(expectedUserId, configuration.userId)
        assertEquals(expectedSubKey, configuration.subscribeKey)
    }

    @Test
    fun canDisableHttpConnectionPool() {
        val config = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // Disable Http pool
            connectionPoolMaxIdleConnections = 0
        }.build()

        val pubNub = PubNub.create(config)

        assertEquals(0, config.connectionPoolMaxIdleConnections)
        assertEquals(0, pubNub.configuration.connectionPoolMaxIdleConnections)

        pubNub.forceDestroy()
    }

    @Test
    fun canSpecifyHowLongConnectionStayInactiveInPool() {
        val config = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // Specify how long connection stay inactive in pool
            connectionPoolKeepAliveDuration = 19
        }.build()

        val pubNub = PubNub.create(config)

        assertEquals(19, config.connectionPoolKeepAliveDuration)
        assertEquals(19, pubNub.configuration.connectionPoolKeepAliveDuration)

        pubNub.forceDestroy()
    }
}

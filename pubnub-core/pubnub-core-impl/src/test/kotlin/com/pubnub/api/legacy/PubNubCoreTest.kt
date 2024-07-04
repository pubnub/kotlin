package com.pubnub.api.legacy

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.managers.ListenerManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PubNubCoreTest : BaseTest() {
    override fun onBefore() {
        clearConfiguration()
    }

    @Test
    fun testCreateSuccess() {
        assertEquals(true, pubnub.configuration.secure)
        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
    }

    @Test
    fun testEncryptCustomKey() {
        assertEquals(
            "iALQtn3PfIXe74CT/wrS7g==",
            pubnub.encrypt("test1", CryptoModule.createLegacyCryptoModule("cipherKey", false)).trim(),
        )
    }

    @Test
    fun testEncryptConfigurationKey() {
        config.cryptoModule = CryptoModule.createLegacyCryptoModule("cipherKey", false)
        // initPubNub()
        assertEquals("iALQtn3PfIXe74CT/wrS7g==", pubnub.encrypt("test1").trim())
    }

    @Test
    fun testDecryptCustomKey() {
        val cryptoModule = CryptoModule.createLegacyCryptoModule("cipherKey", false)
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==", cryptoModule).trim())
    }

    @Test
    fun testDecryptConfigurationKey() {
        config.cryptoModule = CryptoModule.createLegacyCryptoModule("cipherKey", false)
        // initPubNub()
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==").trim())
    }

    @Test
    fun testconfig() {
        config.subscribeTimeout = 3000
        config.connectTimeout = 4000
        config.nonSubscribeRequestTimeout = 5000
        config.retryConfiguration = RetryConfiguration.None
        // initPubNub()

        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
        assertEquals(3000, config.subscribeTimeout)
        assertEquals(4000, config.connectTimeout)
        assertEquals(5000, config.nonSubscribeRequestTimeout)
    }

    @Test
    fun getVersionAndTimeStamp() {
        val version = PubNubCore.SDK_VERSION
        val timeStamp = PubNubCore.timestamp()
        assertEquals("9.2.2", version)
        assertTrue(timeStamp > 0)
    }

    @Test
    fun pnGeneratesPnsdkWithSuffixes() {
        val name1 = "key1"
        val suffix1 = "value1/1.0.0"
        val name2 = "key2"
        val suffix2 = "value2/2.0.0"
        val suffix11 = "value3/2.0.0"

        config.pnsdkSuffixes = buildMap {
            put(name1, suffix1)
            put(name2, suffix2)
            put(name1, suffix11)
        }

        val generatedPnsdk = pubnub.generatePnsdk()
        assertEquals("PubNub-Test/${PubNubCore.SDK_VERSION} $suffix11 $suffix2", generatedPnsdk)
    }

    @Test
    fun pnGeneratesPnsdkWithPnsdkName() {
        val pubNubCore = PubNubCore(
            config.build(),
            ListenerManager(TestPubNub(config.build())),
            pnsdkName = "PubNub-ABC",
        )
        assertEquals(pubNubCore.generatePnsdk(), "PubNub-ABC/${PubNubCore.SDK_VERSION}")
    }
}

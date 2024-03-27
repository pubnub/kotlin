package com.pubnub.api.legacy

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.PubNubCore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PubNubTest : BaseTest() {
    override fun onBefore() {
        clearConfiguration()
        // initPubNub()
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
        val timeStamp = pubnub.timestamp()
        assertEquals("9.0.0-alpha01", version)
        assertTrue(timeStamp > 0)
    }
}

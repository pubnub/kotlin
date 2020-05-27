package com.pubnub.api.legacy

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNReconnectionPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PubNubTest : BaseTest() {

    lateinit var config: PNConfiguration

    override fun onBefore() {
        config = PNConfiguration().apply {
            subscribeKey = "demo"
            publishKey = "demo"
        }
    }

    override fun onAfter() {
        pubnub.destroy()
    }

    @Test
    fun testCreateSuccess() {
        pubnub = PubNub(config)
        assertEquals(true, pubnub.configuration.secure)
        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
    }

    @Test
    @Throws(PubNubException::class)
    fun testEncryptCustomKey() {
        pubnub = PubNub(config)
        assertEquals("iALQtn3PfIXe74CT/wrS7g==", pubnub.encrypt("test1", "cipherKey").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testEncryptConfigurationKey() {
        config.cipherKey = "cipherKey"
        pubnub = PubNub(config)
        assertEquals("iALQtn3PfIXe74CT/wrS7g==", pubnub.encrypt("test1").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testDecryptCustomKey() {
        pubnub = PubNub(config)
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==", "cipherKey").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testDecryptConfigurationKey() {
        config.cipherKey = "cipherKey"
        pubnub = PubNub(config)
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==").trim())
    }

    @Test
    fun testconfig() {
        config.subscribeTimeout = 3000
        config.connectTimeout = 4000
        config.nonSubscribeRequestTimeout = 5000
        config.reconnectionPolicy = PNReconnectionPolicy.NONE
        pubnub = PubNub(config)
        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
        assertEquals(3000, config.subscribeTimeout)
        assertEquals(4000, config.connectTimeout)
        assertEquals(5000, config.nonSubscribeRequestTimeout)
    }

    @Test
    fun getVersionAndTimeStamp() {
        pubnub = PubNub(config)
        val version = pubnub.version
        val timeStamp = pubnub.timestamp()
        assertEquals("0.0.2-dev", version)
        assertTrue(timeStamp > 0)
    }

}
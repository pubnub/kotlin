package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.BasePubNubImpl
import org.junit.Assert
import org.junit.Test

class PNConfigurationTest {
    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun setUUIDToEmptyString() {
        PNConfiguration("")
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun resetUUIDToEmptyString() {
        val config = PNConfiguration(BasePubNubImpl.generateUUID())
        config.uuid = ""
    }

    @Suppress("DEPRECATION")
    @Test
    fun resetUUIDToNonEmptyString() {
        val config = PNConfiguration(BasePubNubImpl.generateUUID())
        val newUUID = BasePubNubImpl.generateUUID()
        config.uuid = newUUID

        Assert.assertEquals(newUUID, config.userId.value)
    }

    @Test
    fun testDefaultTimeoutValues() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        Assert.assertEquals(300, config.presenceTimeout)
        Assert.assertEquals(0, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues1() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.presenceTimeout = 100
        Assert.assertEquals(100, config.presenceTimeout)
        Assert.assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.heartbeatInterval = 100
        Assert.assertEquals(300, config.presenceTimeout)
        Assert.assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.heartbeatInterval = 40
        config.presenceTimeout = 50
        Assert.assertEquals(50, config.presenceTimeout)
        Assert.assertEquals(24, config.heartbeatInterval)
    }

    @Test
    fun `reconnection policy should set retry configuration`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.reconnectionPolicy = PNReconnectionPolicy.NONE
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.None)

        config.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)

        config.reconnectionPolicy = PNReconnectionPolicy.EXPONENTIAL
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Exponential)
    }

    @Test
    fun `maximumReconnectionRetries policy should reset retry configuration`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))

        config.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        config.maximumReconnectionRetries = 5
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)
        Assert.assertEquals(5, (config.retryConfiguration as RetryConfiguration.Linear).maxRetryNumber)

        config.maximumReconnectionRetries = 10
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)
        Assert.assertEquals(10, (config.retryConfiguration as RetryConfiguration.Linear).maxRetryNumber)
    }

    @Test
    fun `cryptomodule uses cipherKey when cryptomodule is not set`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))

        config.cryptoModule = null
        config.cipherKey = "enigma"
        Assert.assertNotNull(config.cryptoModule)
    }

    @Test
    fun `cryptomodule uses cryptomodule when cryptomodule is set`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        val expectedCryptoModule = CryptoModule.createAesCbcCryptoModule("cipher")
        config.cryptoModule = expectedCryptoModule
        config.cipherKey = "enigma"

        Assert.assertEquals(expectedCryptoModule, config.cryptoModule)
    }
}

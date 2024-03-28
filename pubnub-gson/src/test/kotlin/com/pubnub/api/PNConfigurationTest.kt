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
        config.setUuid("")
    }

    @Suppress("DEPRECATION")
    @Test
    fun resetUUIDToNonEmptyString() {
        val config = PNConfiguration(BasePubNubImpl.generateUUID())
        val newUUID = BasePubNubImpl.generateUUID()
        config.setUuid(newUUID)

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
        config.setPresenceTimeout(100)
        Assert.assertEquals(100, config.presenceTimeout)
        Assert.assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setHeartbeatInterval(100)
        Assert.assertEquals(300, config.presenceTimeout)
        Assert.assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setHeartbeatInterval(40)
        config.setPresenceTimeout(50)
        Assert.assertEquals(50, config.presenceTimeout)
        Assert.assertEquals(24, config.heartbeatInterval)
    }

    @Test
    fun `reconnection policy should set retry configuration`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setReconnectionPolicy(PNReconnectionPolicy.NONE)
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.None)

        config.setReconnectionPolicy(PNReconnectionPolicy.LINEAR)
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)

        config.setReconnectionPolicy(PNReconnectionPolicy.EXPONENTIAL)
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Exponential)
    }

    @Test
    fun `maximumReconnectionRetries policy should reset retry configuration`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))

        config.setReconnectionPolicy(PNReconnectionPolicy.LINEAR)
        config.setMaximumReconnectionRetries(5)
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)
        Assert.assertEquals(5, (config.retryConfiguration as RetryConfiguration.Linear).maxRetryNumber)

        config.setMaximumReconnectionRetries(10)
        Assert.assertTrue(config.retryConfiguration is RetryConfiguration.Linear)
        Assert.assertEquals(10, (config.retryConfiguration as RetryConfiguration.Linear).maxRetryNumber)
    }

    @Test
    fun `cryptomodule uses cipherKey when cryptomodule is not set`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))

        config.setCryptoModule(null)
        config.setCipherKey("enigma")
        Assert.assertNotNull(config.cryptoModule)
    }

    @Test
    fun `cryptomodule uses cryptomodule when cryptomodule is set`() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        val expectedCryptoModule = CryptoModule.createAesCbcCryptoModule("cipher")
        config.setCryptoModule(expectedCryptoModule)
        config.setCipherKey("enigma")

        Assert.assertEquals(expectedCryptoModule, config.cryptoModule)
    }
}

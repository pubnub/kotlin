package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.BasePubNubImpl
import com.pubnub.internal.CorePNConfiguration
import org.junit.Assert.assertEquals
import org.junit.Test

class CorePNConfigurationTest {
    @Suppress("DEPRECATION")
    @Test
    fun pnConfigurationGeneratesPnsdkWithSuffixes() {
        val name1 = "key1"
        val suffix1 = "value1/1.0.0"
        val name2 = "key2"
        val suffix2 = "value2/2.0.0"
        val suffix11 = "value3/2.0.0"

        val pnConfiguration =
                CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID())).apply{
                    addPnsdkSuffix(name1 to suffix1, name2 to suffix2)
                    addPnsdkSuffix(mapOf(name1 to suffix11))
                }

        val version = "someVersion"
        val generatedPnsdk = pnConfiguration.generatePnsdk(version)
        assertEquals("PubNub-Kotlin/$version $suffix11 $suffix2", generatedPnsdk)
    }

    @Test(expected = PubNubException::class)
    fun setUserIdToEmptyString() {
        CorePNConfiguration(userId = UserId(""))
    }

    @Test(expected = PubNubException::class)
    fun resetUserIdToEmptyString() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.userId = UserId("")
    }

    @Test
    fun resetUserIdToNonEmptyString() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        val newUserId = UserId(BasePubNubImpl.generateUUID())
        config.userId = newUserId

        assertEquals(newUserId, config.userId)
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun setUUIDToEmptyString() {
        CorePNConfiguration("")
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun resetUUIDToEmptyString() {
        val config = CorePNConfiguration(BasePubNubImpl.generateUUID())
        config.uuid = ""
    }

    @Suppress("DEPRECATION")
    @Test
    fun resetUUIDToNonEmptyString() {
        val config = CorePNConfiguration(BasePubNubImpl.generateUUID())
        val newUUID = BasePubNubImpl.generateUUID()
        config.uuid = newUUID

        assertEquals(newUUID, config.userId.value)
    }

    @Test
    fun `unfortunately should allow to set CryptoModule more twice`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.cryptoModule = CryptoModule.createLegacyCryptoModule("myCipherKey", true)
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey")
    }

    @Test
    fun `should set delay to 3 in RetryConfiguration Linear when user set it lower than 3`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 1, maxRetryNumber = 10)

        assertEquals(2, (config.retryConfiguration as RetryConfiguration.Linear).delayInSec.inWholeSeconds)
    }

    @Test
    fun `should set maxRetry to 10 in RetryConfiguration Linear when user set it above 10`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 11)

        assertEquals(10, (config.retryConfiguration as RetryConfiguration.Linear).maxRetryNumber)
    }

    @Test
    fun `should set minDelayInSec to 2 in RetryConfiguration Exponential when user set it lower than 2`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 1, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(2, (config.retryConfiguration as RetryConfiguration.Exponential).minDelayInSec.inWholeSeconds)
    }

    @Test
    fun `should set maxRetry to 6 in RetryConfiguration Exponential when user set it above 6`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 5, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(6, (config.retryConfiguration as RetryConfiguration.Exponential).maxRetryNumber)
    }

    @Test
    fun `should set maxDelayInSec to 150 in RetryConfiguration Exponential when user set it above 150`() {
        val config = CorePNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
        config.retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 5, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(6, (config.retryConfiguration as RetryConfiguration.Exponential).maxRetryNumber)
    }
}

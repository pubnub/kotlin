package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.policies.RequestRetryPolicy
import org.junit.Assert.assertEquals
import org.junit.Test

class PNConfigurationTest {
    @Suppress("DEPRECATION")
    @Test
    fun pnConfigurationGeneratesPnsdkWithSuffixes() {
        val name1 = "key1"
        val suffix1 = "value1/1.0.0"
        val name2 = "key2"
        val suffix2 = "value2/2.0.0"
        val suffix11 = "value3/2.0.0"

        val pnConfiguration = object :
            PNConfiguration(userId = UserId(PubNub.generateUUID())) {
            init {
                addPnsdkSuffix(name1 to suffix1, name2 to suffix2)
                addPnsdkSuffix(mapOf(name1 to suffix11))
            }
        }
        val version = "someVersion"
        val generatedPnsdk = pnConfiguration.generatePnsdk(version)
        assertEquals("PubNub-Kotlin/$version $suffix11 $suffix2", generatedPnsdk)
    }

    @Test(expected = PubNubException::class)
    fun setUserIdToEmptyString() {
        PNConfiguration(userId = UserId(""))
    }

    @Test(expected = PubNubException::class)
    fun resetUserIdToEmptyString() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.userId = UserId("")
    }

    @Test
    fun resetUserIdToNonEmptyString() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        val newUserId = UserId(PubNub.generateUUID())
        config.userId = newUserId

        assertEquals(newUserId, config.userId)
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun setUUIDToEmptyString() {
        PNConfiguration("")
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun resetUUIDToEmptyString() {
        val config = PNConfiguration(PubNub.generateUUID())
        config.uuid = ""
    }

    @Suppress("DEPRECATION")
    @Test
    fun resetUUIDToNonEmptyString() {
        val config = PNConfiguration(PubNub.generateUUID())
        val newUUID = PubNub.generateUUID()
        config.uuid = newUUID

        assertEquals(newUUID, config.userId.value)
    }

    @Test
    fun `unfortunately should allow to set CryptoModule more twice`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.cryptoModule = CryptoModule.createLegacyCryptoModule("myCipherKey", true)
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey")
    }

    @Test
    fun `should set delay to 3 in RequestRetryPolicy Linear when user set it lower than 3`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.newRetryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 10)

        assertEquals(3, (config.newRetryPolicy as RequestRetryPolicy.Linear).delayInSec)
    }

    @Test
    fun `should set maxRetry to 10 in RequestRetryPolicy Linear when user set it above 10`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.newRetryPolicy = RequestRetryPolicy.Linear(delayInSec = 3, maxRetryNumber = 11)

        assertEquals(10, (config.newRetryPolicy as RequestRetryPolicy.Linear).maxRetryNumber)
    }

    @Test
    fun `should set delay to 3 in RequestRetryPolicy Exponential when user set it lower than 3`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        RequestRetryPolicy.Exponential(minDelayInSec = 2, maxDelayInSec = 10, maxRetryNumber = 10)
        config.newRetryPolicy = RequestRetryPolicy.Exponential(minDelayInSec = 2, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(3, (config.newRetryPolicy as RequestRetryPolicy.Exponential).minDelayInSec)
    }

    @Test
    fun `should set maxRetry to 10 in RequestRetryPolicy Exponential when user set it above 10`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.newRetryPolicy = RequestRetryPolicy.Linear(3, 11)

        assertEquals(10, (config.newRetryPolicy as RequestRetryPolicy.Linear).maxRetryNumber)
    }

}

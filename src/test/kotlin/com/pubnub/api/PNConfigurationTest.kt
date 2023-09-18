package com.pubnub.api

import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import com.pubnub.api.crypto.cryptor.LegacyCryptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.Assertions

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
    fun `should throw exception when you try to set CryptoModule more twice`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey", true)

        val thrown = Assertions.assertThrows(
            IllegalStateException::class.java,
            { config.cryptoModule = CryptoModule.createAesCbcCryptoModule("myNewCipherKey", true) },
            "IllegalStateException was expected"
        )
        Assertions.assertEquals("CryptoModule can only by set once", thrown.message)
    }

    @Test
    fun `can set cryptoModule`() {
        val config = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        config.cryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey", true)
        val cryptoModule = config.cryptoModule

        assertTrue(cryptoModule?.primaryCryptor is AesCbcCryptor)
        assertTrue(cryptoModule?.cryptorsForEncryptionOnly?.first() is LegacyCryptor)
    }
}

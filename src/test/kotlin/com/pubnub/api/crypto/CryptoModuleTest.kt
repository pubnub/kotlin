package com.pubnub.api.crypto

import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.cryptor.LegacyCryptor
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasSize
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.io.InputStream
import org.hamcrest.Matchers.`is` as iz

class CryptoModuleTest {

    @Test
    fun `can createLegacyCryptoModule`() {
        // given
        val cipherKey = "enigma"

        // when
        val legacyCryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey)

        // then
        Assert.assertTrue(legacyCryptoModule.primaryCryptor is LegacyCryptor)
        MatcherAssert.assertThat(legacyCryptoModule.cryptorsForDecryptionOnly, hasSize(2))
        MatcherAssert.assertThat(
            legacyCryptoModule.cryptorsForDecryptionOnly?.first(),
            iz(CoreMatchers.instanceOf(LegacyCryptor::class.java))
        )
        MatcherAssert.assertThat(
            legacyCryptoModule.cryptorsForDecryptionOnly?.get(1),
            iz(CoreMatchers.instanceOf(AesCbcCryptor::class.java))
        )
    }

    @Test
    fun `can createAesCbcCryptoModule`() {
        // given
        val cipherKey = "enigma"

        // when
        val legacyCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey)

        // then
        Assert.assertTrue(legacyCryptoModule.primaryCryptor is AesCbcCryptor)
        MatcherAssert.assertThat(legacyCryptoModule.cryptorsForDecryptionOnly, hasSize(2))
        MatcherAssert.assertThat(
            legacyCryptoModule.cryptorsForDecryptionOnly?.first(),
            iz(CoreMatchers.instanceOf(AesCbcCryptor::class.java))
        )
        MatcherAssert.assertThat(
            legacyCryptoModule.cryptorsForDecryptionOnly?.get(1),
            iz(CoreMatchers.instanceOf(LegacyCryptor::class.java))
        )
    }

    @Test
    fun `can createNewCryptoModule`() {
        // given
        val cipherKey = "enigma"

        // when
        val legacyCryptoModule = CryptoModule.createNewCryptoModule(defaultCryptor = AesCbcCryptor(cipherKey))

        // then
        Assert.assertTrue(legacyCryptoModule.primaryCryptor is AesCbcCryptor)
        MatcherAssert.assertThat(legacyCryptoModule.cryptorsForDecryptionOnly, hasSize(1))
        MatcherAssert.assertThat(
            legacyCryptoModule.cryptorsForDecryptionOnly?.first(),
            iz(CoreMatchers.instanceOf(AesCbcCryptor::class.java))
        )
    }

    @Test
    fun `can decrypt encrypted message using LegacyCryptoModule with randomIV`() {
        // given
        val cipherKey = "enigma"
        val legacyCryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = legacyCryptoModule.encrypt(msgToEncrypt)
        val decryptedMsg = legacyCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `can decrypt encrypted message using LegacyCryptoModule with staticIV`() {
        // given
        val cipherKey = "enigma"
        val legacyCryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey, false)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = legacyCryptoModule.encrypt(msgToEncrypt)
        val decryptedMsg = legacyCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `using LegacyCryptoModule can decrypt message that was encrypted with AesCbcCryptor`() {
        // given
        val cipherKey = "enigma"
        val moduleWithAesCbcCryptorOnly = CryptoModule.createNewCryptoModule(defaultCryptor = AesCbcCryptor(cipherKey))
        val legacyCryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = moduleWithAesCbcCryptorOnly.encrypt(msgToEncrypt)
        val decryptedMsg = legacyCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `using AesCbcCryptoModule can decrypt message that was encrypted with LegacyCryptor with randomIV `() {
        // given
        val cipherKey = "enigma"
        val moduleWithLegacyCryptorOnlyWithRandomIV =
            CryptoModule.createNewCryptoModule(defaultCryptor = LegacyCryptor(cipherKey))
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = moduleWithLegacyCryptorOnlyWithRandomIV.encrypt(msgToEncrypt)
        val decryptedMsg = aesCbcCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `using AesCbcCryptoModule can decrypt message that was encrypted with LegacyCryptor with staticIV `() {
        // given
        val cipherKey = "enigma"
        val moduleWithLegacyCryptorOnlyWithStaticIV =
            CryptoModule.createNewCryptoModule(defaultCryptor = LegacyCryptor(cipherKey, false))
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey, false)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = moduleWithLegacyCryptorOnlyWithStaticIV.encrypt(msgToEncrypt)
        val decryptedMsg = aesCbcCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `can decrypt encrypted message using module with only AesCbcCryptor`() {
        // given
        val cipherKey = "enigma"
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey)
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = aesCbcCryptoModule.encrypt(msgToEncrypt)
        val decryptedMsg = aesCbcCryptoModule.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun `can decrypt encrypted message using custom cryptor `() {
        // given
        val customCryptor = myCustomCryptor()
        val cipherKey = "enigma"
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = customCryptor.encrypt(msgToEncrypt)
        val decryptedMsg = customCryptor.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    private fun myCustomCryptor() = object : Cryptor {
        override fun id(): ByteArray {
            return byteArrayOf('C'.code.toByte(), 'U'.code.toByte(), 'S'.code.toByte(), 'T'.code.toByte())
        }

        override fun encrypt(data: ByteArray): EncryptedData {
            return EncryptedData(metadata = null, data = data)
        }

        override fun decrypt(encryptedData: EncryptedData): ByteArray {
            return encryptedData.data
        }

        override fun encryptStream(stream: InputStream): Result<EncryptedStreamData> {
            TODO("Not yet implemented")
        }

        override fun decryptStream(encryptedData: EncryptedStreamData): Result<InputStream> {
            TODO("Not yet implemented")
        }
    }
}

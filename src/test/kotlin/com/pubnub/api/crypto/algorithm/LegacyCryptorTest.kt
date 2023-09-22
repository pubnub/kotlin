package com.pubnub.api.crypto.algorithm

import com.pubnub.api.crypto.cryptor.LegacyCryptor
import com.pubnub.api.crypto.data.EncryptedData
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class LegacyCryptorTest {

    @Test
    fun canDecryptTextWhatIsEncryptedWithStaticIV() {
        // given
        val cipherKey = "enigma"
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val cryptor = LegacyCryptor(cipherKey = cipherKey, useRandomIv = false)
        val encryptedMsg = cryptor.encrypt(msgToEncrypt)
        val decryptedMsg = cryptor.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun canDecryptTextWhatIsEncryptedWithRandomIV() {
        // given
        val cipherKey = "enigma"
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val cryptor = LegacyCryptor(cipherKey = cipherKey)
        val encryptedMsg = cryptor.encrypt(msgToEncrypt)
        val decryptedMsg = cryptor.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun encryptingWithRandomIVTwoTimesTheSameMessageProducesDifferentOutput() {
        // given
        val cipherKey = "enigma"
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val cryptor = LegacyCryptor(cipherKey = cipherKey)
        val encrypted1: EncryptedData = cryptor.encrypt(msgToEncrypt)
        val encrypted2: EncryptedData = cryptor.encrypt(msgToEncrypt)

        // then
        assertFalse(encrypted1.data.contentEquals(encrypted2.data))
    }

    @Test
    fun encryptingWithRandomIVTwoTimesDecryptedMsgIsTheSame() {
        // given
        val cipherKey = "enigma"
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val cryptor = LegacyCryptor(cipherKey = cipherKey)
        val encrypted1 = cryptor.encrypt(msgToEncrypt)
        val encrypted2 = cryptor.encrypt(msgToEncrypt)

        // then
        assertArrayEquals(msgToEncrypt, cryptor.decrypt(encrypted1))
        assertArrayEquals(msgToEncrypt, cryptor.decrypt(encrypted2))
    }
}

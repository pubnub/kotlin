package com.pubnub.api.crypto.algorithm

import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import com.pubnub.api.crypto.data.EncryptedData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AesCBCCryptorTest {
    private lateinit var objectUnderTest: AesCbcCryptor

    @BeforeEach
    fun setUp() {
        objectUnderTest = AesCbcCryptor("enigma")
    }

    @Test
    fun canDecryptTextWhatIsEncrypted() {
        // given
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encryptedMsg = objectUnderTest.encrypt(msgToEncrypt)
        val decryptedMsg = objectUnderTest.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @Test
    fun encryptingTwoTimesTheSameMessageProducesDifferentOutput() {
        // given
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encrypted1: EncryptedData = objectUnderTest.encrypt(msgToEncrypt)
        val encrypted2: EncryptedData = objectUnderTest.encrypt(msgToEncrypt)

        // then
        Assertions.assertFalse(encrypted1.data.contentEquals(encrypted2.data))
    }

    @Test
    fun encryptingTwoTimesDecryptedMsgIsTheSame() {
        // given
        val msgToEncrypt = "Hello world".toByteArray()

        // when
        val encrypted1 = objectUnderTest.encrypt(msgToEncrypt)
        val encrypted2 = objectUnderTest.encrypt(msgToEncrypt)

        // then
        assertArrayEquals(msgToEncrypt, objectUnderTest.decrypt(encrypted1))
        assertArrayEquals(msgToEncrypt, objectUnderTest.decrypt(encrypted2))
    }
}

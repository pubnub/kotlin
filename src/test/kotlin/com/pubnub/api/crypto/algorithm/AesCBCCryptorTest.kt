package com.pubnub.api.crypto.algorithm

import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import com.pubnub.api.crypto.data.EncryptedData
import org.junit.Assert
import org.junit.jupiter.api.Assertions
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
        Assert.assertTrue(msgToEncrypt.contentEquals(decryptedMsg))
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
        Assert.assertTrue(msgToEncrypt.contentEquals(objectUnderTest.decrypt(encrypted1)))
        Assert.assertTrue(msgToEncrypt.contentEquals(objectUnderTest.decrypt(encrypted2)))
    }
}

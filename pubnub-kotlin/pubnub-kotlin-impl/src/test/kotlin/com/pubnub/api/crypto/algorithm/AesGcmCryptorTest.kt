package com.pubnub.api.crypto.algorithm

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.internal.crypto.cryptor.AesGcmCryptor
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class AesGcmCryptorTest {
    private lateinit var objectUnderTest: AesGcmCryptor

    companion object {
        private const val CIPHER_KEY = "enigma"
        private const val NONCE_SIZE = 12
        private const val TAG_SIZE = 16

        @JvmStatic
        fun messageToBeEncrypted(): List<Arguments> =
            listOf(
                Arguments.of("Hello world"),
                Arguments.of("Zażółć gęślą jaźń"), // Polish
                Arguments.of("हैलो वर्ल्ड"), // Hindi
                Arguments.of("こんにちは世界"), // Japan
                Arguments.of("你好世界"), // Chinese
            )
    }

    @BeforeEach
    fun setUp() {
        objectUnderTest = AesGcmCryptor(CIPHER_KEY)
    }

    @Test
    fun `id is ACGM`() {
        assertArrayEquals("ACGM".toByteArray(), objectUnderTest.id())
    }

    @ParameterizedTest
    @MethodSource("messageToBeEncrypted")
    fun canDecryptTextWhatIsEncrypted(msgToBeEncrypted: String) {
        // given
        val msgToEncrypt = msgToBeEncrypted.toByteArray()

        // when
        val encryptedMsg = objectUnderTest.encrypt(msgToEncrypt)
        val decryptedMsg = objectUnderTest.decrypt(encryptedMsg)

        // then
        assertArrayEquals(msgToEncrypt, decryptedMsg)
    }

    @ParameterizedTest
    @MethodSource("messageToBeEncrypted")
    fun encryptingTwoTimesTheSameMessageProducesDifferentOutput(msgToBeEncrypted: String) {
        // given
        val msgToEncrypt = msgToBeEncrypted.toByteArray()

        // when
        val encrypted1: EncryptedData = objectUnderTest.encrypt(msgToEncrypt)
        val encrypted2: EncryptedData = objectUnderTest.encrypt(msgToEncrypt)

        // then
        Assertions.assertFalse(encrypted1.metadata.contentEquals(encrypted2.metadata))
        Assertions.assertFalse(encrypted1.data.contentEquals(encrypted2.data))
        assertArrayEquals(msgToEncrypt, objectUnderTest.decrypt(encrypted1))
        assertArrayEquals(msgToEncrypt, objectUnderTest.decrypt(encrypted2))
    }

    @ParameterizedTest
    @MethodSource("messageToBeEncrypted")
    fun `encrypted data has 12-byte nonce as metadata and tag appended to ciphertext`(msgToBeEncrypted: String) {
        // given
        val msgToEncrypt = msgToBeEncrypted.toByteArray()

        // when
        val encrypted = objectUnderTest.encrypt(msgToEncrypt)

        // then
        assertEquals(NONCE_SIZE, encrypted.metadata!!.size)
        assertEquals(msgToEncrypt.size + TAG_SIZE, encrypted.data.size)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 15, 16, 17, 64 * 1024 + 1])
    fun `can decrypt stream what is encrypted as stream`(size: Int) {
        // given
        val plaintext = Random.nextBytes(size)

        // when
        val encrypted = objectUnderTest.encryptStream(ByteArrayInputStream(plaintext))
        // read to EOF: the tag is only written when the cipher stream reaches end of input
        val encryptedBytes = encrypted.stream.readBytes()
        val decrypted =
            objectUnderTest.decryptStream(
                EncryptedStreamData(metadata = encrypted.metadata, stream = ByteArrayInputStream(encryptedBytes)),
            ).readBytes()

        // then
        assertEquals(NONCE_SIZE, encrypted.metadata!!.size)
        assertEquals(plaintext.size + TAG_SIZE, encryptedBytes.size)
        assertArrayEquals(plaintext, decrypted)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 15, 16, 17, 64 * 1024 + 1])
    fun `stream and block modes are interchangeable`(size: Int) {
        // given
        val plaintext = Random.nextBytes(size)

        // when
        val encryptedAsBlock = objectUnderTest.encrypt(plaintext)
        val encryptedAsStream = objectUnderTest.encryptStream(ByteArrayInputStream(plaintext))
        val encryptedStreamBytes = encryptedAsStream.stream.readBytes()

        // then
        assertArrayEquals(
            plaintext,
            objectUnderTest.decryptStream(
                EncryptedStreamData(metadata = encryptedAsBlock.metadata, stream = ByteArrayInputStream(encryptedAsBlock.data)),
            ).readBytes(),
        )
        assertArrayEquals(
            plaintext,
            objectUnderTest.decrypt(EncryptedData(metadata = encryptedAsStream.metadata, data = encryptedStreamBytes)),
        )
    }

    @Test
    fun `decryptStream reads from buffered stream when given a plain non-buffered stream`() {
        // given a payload bigger than BufferedInputStream's default buffer
        val plaintext = Random.nextBytes(20 * 1024)
        val encrypted = objectUnderTest.encrypt(plaintext)
        val source = NonBufferedInputStream(encrypted.data)

        // when
        val decrypted = objectUnderTest.decryptStream(EncryptedStreamData(metadata = encrypted.metadata, stream = source)).readBytes()

        // then
        assertArrayEquals(plaintext, decrypted)
    }

    @ParameterizedTest
    @ValueSource(strings = ["nonce", "ciphertext", "tag"])
    fun `tampered payload fails with generic decryption error in block mode`(part: String) {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world, this is a longer payload".toByteArray())
        val tampered = tamper(encrypted, part)

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(tampered)
            }

        // then
        assertDecryptionFailed(exception)
    }

    @ParameterizedTest
    @ValueSource(strings = ["nonce", "ciphertext", "tag"])
    fun `tampered payload fails with generic decryption error thrown by decryptStream itself`(part: String) {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world, this is a longer payload".toByteArray())
        val tampered = tamper(encrypted, part)

        // when no read of the returned stream is performed
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(
                    EncryptedStreamData(metadata = tampered.metadata, stream = ByteArrayInputStream(tampered.data)),
                )
            }

        // then
        assertDecryptionFailed(exception)
    }

    @Test
    fun `wrong key fails with generic decryption error`() {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world".toByteArray())

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                AesGcmCryptor("wrongKey").decrypt(encrypted)
            }

        // then
        assertDecryptionFailed(exception)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 11, 16, 28])
    fun `metadata of wrong size fails with generic decryption error`(metadataSize: Int) {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world".toByteArray())
        val withWrongMetadata = EncryptedData(metadata = ByteArray(metadataSize), data = encrypted.data)

        // when
        val blockException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(withWrongMetadata)
            }
        val streamException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(
                    EncryptedStreamData(metadata = withWrongMetadata.metadata, stream = ByteArrayInputStream(encrypted.data)),
                )
            }

        // then message is not CBC-style "Invalid random IV"
        assertDecryptionFailed(blockException)
        assertDecryptionFailed(streamException)
    }

    @Test
    fun `null metadata fails with generic decryption error`() {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world".toByteArray())

        // when
        val blockException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(EncryptedData(metadata = null, data = encrypted.data))
            }
        val streamException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(EncryptedStreamData(metadata = null, stream = ByteArrayInputStream(encrypted.data)))
            }

        // then
        assertDecryptionFailed(blockException)
        assertDecryptionFailed(streamException)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 15, 16])
    fun `data not longer than the tag fails with generic decryption error`(dataSize: Int) {
        // given
        val encryptedData = EncryptedData(metadata = ByteArray(NONCE_SIZE), data = ByteArray(dataSize))

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(encryptedData)
            }

        // then
        assertDecryptionFailed(exception)
    }

    @Test
    fun `genuine tag-only payload of encrypted empty plaintext is rejected rather than returning empty data`() {
        // given a valid 16-byte tag produced by the JDK for empty plaintext with the same key
        val nonce = Random.nextBytes(NONCE_SIZE)
        val keyBytes = MessageDigest.getInstance("SHA-256").digest(CIPHER_KEY.toByteArray(Charsets.UTF_8))
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, "AES"), GCMParameterSpec(TAG_SIZE * Byte.SIZE_BITS, nonce))
        val tagOnly = cipher.doFinal(ByteArray(0))
        assertEquals(TAG_SIZE, tagOnly.size)

        // when
        val blockException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(EncryptedData(metadata = nonce, data = tagOnly))
            }
        val streamException =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(EncryptedStreamData(metadata = nonce, stream = ByteArrayInputStream(tagOnly)))
            }

        // then
        assertDecryptionFailed(blockException)
        assertDecryptionFailed(streamException)
    }

    @Test
    fun `read failure in decryptStream is reported as parsing error, not decryption error`() {
        // given
        val encrypted = objectUnderTest.encrypt(Random.nextBytes(32 * 1024))
        val readError = IOException("connection reset")
        val failingStream = FailingInputStream(encrypted.data, failAfter = 16 * 1024, error = readError)

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(EncryptedStreamData(metadata = encrypted.metadata, stream = failingStream))
            }

        // then
        assertEquals("Failed to read input stream", exception.errorMessage)
        assertEquals(PubNubError.PARSING_ERROR, exception.pubnubError)
        assertSame(readError, exception.cause)
        assertNotEquals(PubNubError.CRYPTO_ERROR, exception.pubnubError)
        assertNotEquals("Decryption failed", exception.errorMessage)
        assertTrue(failingStream.closed)
    }

    @Test
    fun `decryptStream closes source stream after successful decryption`() {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world".toByteArray())
        val source = CloseTrackingInputStream(encrypted.data)

        // when
        objectUnderTest.decryptStream(EncryptedStreamData(metadata = encrypted.metadata, stream = source))

        // then
        assertTrue(source.closed)
    }

    @Test
    fun `decryptStream closes source stream after failed decryption`() {
        // given
        val encrypted = objectUnderTest.encrypt("Hello world".toByteArray())
        val tampered = tamper(encrypted, "tag")
        val source = CloseTrackingInputStream(tampered.data)

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(EncryptedStreamData(metadata = tampered.metadata, stream = source))
            }

        // then
        assertDecryptionFailed(exception)
        assertTrue(source.closed)
    }

    @Test
    fun `should throw exception when encrypting empty data`() {
        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.encrypt(ByteArray(0))
            }

        // then
        assertEmptyDataNotAllowed(exception)
    }

    @Test
    fun `should throw exception when decrypting empty data`() {
        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decrypt(EncryptedData(metadata = ByteArray(NONCE_SIZE), data = ByteArray(0)))
            }

        // then
        assertEmptyDataNotAllowed(exception)
    }

    @Test
    fun `should throw exception when encrypting empty stream`() {
        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.encryptStream(ByteArrayInputStream(ByteArray(0)))
            }

        // then
        assertEmptyDataNotAllowed(exception)
    }

    @Test
    fun `should throw exception when decrypting empty stream`() {
        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                objectUnderTest.decryptStream(
                    EncryptedStreamData(metadata = ByteArray(NONCE_SIZE), stream = ByteArrayInputStream(ByteArray(0))),
                )
            }

        // then
        assertEmptyDataNotAllowed(exception)
    }

    private fun tamper(
        encrypted: EncryptedData,
        part: String,
    ): EncryptedData {
        val metadata = encrypted.metadata!!.copyOf()
        val data = encrypted.data.copyOf()
        when (part) {
            "nonce" -> metadata.flipByteAt(0)
            "ciphertext" -> data.flipByteAt(0)
            "tag" -> data.flipByteAt(data.size - 1)
            else -> error("unknown part $part")
        }
        return EncryptedData(metadata = metadata, data = data)
    }

    private fun ByteArray.flipByteAt(index: Int) {
        this[index] = (this[index].toInt() xor 0x01).toByte()
    }

    private fun assertDecryptionFailed(exception: PubNubException) {
        assertEquals("Decryption failed", exception.errorMessage)
        assertEquals(PubNubError.CRYPTO_ERROR, exception.pubnubError)
    }

    private fun assertEmptyDataNotAllowed(exception: PubNubException) {
        assertEquals("Encryption/Decryption of empty data not allowed.", exception.errorMessage)
        assertEquals(PubNubError.ENCRYPTION_AND_DECRYPTION_OF_EMPTY_DATA_NOT_ALLOWED, exception.pubnubError)
    }

    private open class CloseTrackingInputStream(data: ByteArray) : InputStream() {
        private val delegate = ByteArrayInputStream(data)
        var closed = false
            private set

        override fun read(): Int = delegate.read()

        override fun read(
            b: ByteArray,
            off: Int,
            len: Int,
        ): Int = delegate.read(b, off, len)

        override fun close() {
            closed = true
            delegate.close()
        }
    }

    // returns at most a few bytes per read so the buffered wrapper fills gradually
    private class NonBufferedInputStream(data: ByteArray) : CloseTrackingInputStream(data) {
        override fun read(
            b: ByteArray,
            off: Int,
            len: Int,
        ): Int = super.read(b, off, minOf(len, 7))
    }

    private class FailingInputStream(
        data: ByteArray,
        private val failAfter: Int,
        private val error: IOException,
    ) : CloseTrackingInputStream(data) {
        private var position = 0

        override fun read(): Int {
            if (position >= failAfter) {
                throw error
            }
            val byte = super.read()
            if (byte != -1) {
                position++
            }
            return byte
        }

        override fun read(
            b: ByteArray,
            off: Int,
            len: Int,
        ): Int {
            if (position >= failAfter) {
                throw error
            }
            val n = super.read(b, off, minOf(len, failAfter - position))
            if (n > 0) {
                position += n
            }
            return n
        }
    }
}

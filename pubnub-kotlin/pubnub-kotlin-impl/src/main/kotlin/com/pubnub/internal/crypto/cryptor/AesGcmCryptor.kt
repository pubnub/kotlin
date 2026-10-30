package com.pubnub.internal.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.internal.crypto.checkMinSize
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
private const val NONCE_SIZE = 12
private const val TAG_SIZE = 16
private const val DECRYPTION_ERROR_MESSAGE = "Decryption failed"
private const val READ_ERROR_MESSAGE = "Failed to read input stream"

/**
 * AES-256-GCM cryptor (id `ACGM`).
 *
 * Layout: the 12-byte nonce is stored as cryptor metadata in the header, and the data is the ciphertext with
 * the 16-byte authentication tag appended (`ciphertext || tag`). The key is `SHA-256(cipherKey)`, AAD is empty.
 *
 * Decryption is supported: the built-in crypto module factories register this cryptor for decryption only.
 * Encryption is internal and not yet part of any factory.
 */
internal class AesGcmCryptor private constructor(cipherKey: String) : Cryptor {
    private val newKey: SecretKeySpec = createNewKey(cipherKey)

    internal companion object {
        // @JvmSynthetic hides this from Java
        @JvmSynthetic
        operator fun invoke(cipherKey: String): AesGcmCryptor = AesGcmCryptor(cipherKey)
    }

    override fun id(): ByteArray {
        return byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'G'.code.toByte(), 'M'.code.toByte())
    }

    override fun encrypt(data: ByteArray): EncryptedData {
        validateData(data)
        return try {
            val nonce: ByteArray = createRandomNonce()
            val cipher = createInitializedCipher(nonce, Cipher.ENCRYPT_MODE)
            val encryptedData: ByteArray = cipher.doFinal(data)
            EncryptedData(metadata = nonce, data = encryptedData)
        } catch (e: GeneralSecurityException) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR, cause = e)
        }
    }

    // the cause is dropped on purpose: every decryption failure must look the same to the caller
    @Suppress("SwallowedException")
    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        validateData(encryptedData.data)
        val nonce: ByteArray? = encryptedData.metadata?.takeIf { it.size == NONCE_SIZE }
        // data of TAG_SIZE bytes or fewer has no ciphertext; a genuine tag-only payload would decrypt to empty data
        if (nonce == null || encryptedData.data.size <= TAG_SIZE) {
            throw decryptionFailed()
        }
        return try {
            val cipher = createInitializedCipher(nonce, Cipher.DECRYPT_MODE)
            cipher.doFinal(encryptedData.data)
        } catch (e: GeneralSecurityException) {
            throw decryptionFailed()
        }
    }

    override fun encryptStream(stream: InputStream): EncryptedStreamData {
        val bufferedInputStream = validateInputStreamAndReturnBuffered(stream)
        try {
            val nonce: ByteArray = createRandomNonce()
            val cipher = createInitializedCipher(nonce, Cipher.ENCRYPT_MODE)
            // the tag is written when the returned stream reaches its end, so it must be read to EOF
            val cipheredStream = CipherInputStream(bufferedInputStream, cipher)

            return EncryptedStreamData(
                metadata = nonce,
                stream = cipheredStream,
            )
        } catch (e: GeneralSecurityException) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR, cause = e)
        }
    }

    /**
     * Reads the whole input stream and decrypts it before returning, so no plaintext is released before the
     * authentication tag is verified. Peak memory is about 2–3× the payload size.
     *
     * The input stream is always closed by this call, on success and on failure.
     *
     * @throws PubNubException with [PubNubError.CRYPTO_ERROR] and message "Decryption failed" if decryption
     * fails (thrown from this call, never from a later `read()` of the returned stream), or with
     * [PubNubError.PARSING_ERROR] if reading the input stream fails.
     */
    override fun decryptStream(encryptedData: EncryptedStreamData): InputStream {
        val bufferedInputStream = validateInputStreamAndReturnBuffered(encryptedData.stream)
        // read from the buffered stream, never the original: checkMinSize may already have buffered its first bytes
        val encryptedBytes: ByteArray =
            try {
                bufferedInputStream.use { it.readBytes() }
            } catch (e: IOException) {
                throw PubNubException(errorMessage = READ_ERROR_MESSAGE, pubnubError = PubNubError.PARSING_ERROR, cause = e)
            }
        return ByteArrayInputStream(decrypt(EncryptedData(metadata = encryptedData.metadata, data = encryptedBytes)))
    }

    private fun decryptionFailed(): PubNubException {
        return PubNubException(errorMessage = DECRYPTION_ERROR_MESSAGE, pubnubError = PubNubError.CRYPTO_ERROR)
    }

    private fun validateData(data: ByteArray) {
        if (data.isEmpty()) {
            throw PubNubException(
                errorMessage = "Encryption/Decryption of empty data not allowed.",
                pubnubError = PubNubError.ENCRYPTION_AND_DECRYPTION_OF_EMPTY_DATA_NOT_ALLOWED,
            )
        }
    }

    private fun createInitializedCipher(
        nonce: ByteArray,
        mode: Int,
    ): Cipher {
        // a new Cipher per operation: GCM refuses to re-init with the same key and nonce, and Cipher isn't thread-safe
        return Cipher.getInstance(CIPHER_TRANSFORMATION).also {
            it.init(mode, newKey, GCMParameterSpec(TAG_SIZE * Byte.SIZE_BITS, nonce))
        }
    }

    private fun createNewKey(cipherKey: String): SecretKeySpec {
        val keyBytes = sha256(cipherKey.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun createRandomNonce(): ByteArray {
        val nonce = ByteArray(NONCE_SIZE)
        SecureRandom().nextBytes(nonce)
        return nonce
    }

    private fun sha256(input: ByteArray): ByteArray {
        return try {
            MessageDigest.getInstance("SHA-256").digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR, cause = e)
        }
    }

    private fun validateInputStreamAndReturnBuffered(stream: InputStream): BufferedInputStream {
        val bufferedInputStream = stream.buffered()
        bufferedInputStream.checkMinSize(1) {
            throw PubNubException(
                errorMessage = "Encryption/Decryption of empty data not allowed.",
                pubnubError = PubNubError.ENCRYPTION_AND_DECRYPTION_OF_EMPTY_DATA_NOT_ALLOWED,
            )
        }
        return bufferedInputStream
    }
}

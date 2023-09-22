package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import java.io.InputStream
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
private const val RANDOM_IV_SIZE = 16

class AesCbcCryptor(val cipherKey: String) : Cryptor {
    private val newKey: SecretKeySpec = createNewKey()
    private val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)

    override fun id(): ByteArray {
        return byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'H'.code.toByte())
    }

    override fun encrypt(data: ByteArray): EncryptedData {
        return try {
            val ivBytes: ByteArray = createRandomIv()
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            val encryptedData: ByteArray = cipher.doFinal(data)
            EncryptedData(metadata = ivBytes, data = encryptedData)
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        return try {
            val ivBytes: ByteArray = encryptedData.metadata?.takeIf { it.size == RANDOM_IV_SIZE } ?: throw PubNubException("Invalid random IV")
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            val decryptedData = cipher.doFinal(encryptedData.data)
            decryptedData
        } catch (e: Exception) {
            throw PubNubException(errorMessage = "Decryption error", pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun encryptStream(stream: InputStream): Result<EncryptedStreamData> {
        TODO("Not yet implemented")
    }

    override fun decryptStream(encryptedData: EncryptedStreamData): Result<InputStream> {
        TODO("Not yet implemented")
    }

    private fun createNewKey(): SecretKeySpec {
        val keyBytes = sha256(cipherKey.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun createRandomIv(): ByteArray {
        val ivBytes = ByteArray(RANDOM_IV_SIZE)
        SecureRandom().nextBytes(ivBytes)
        return ivBytes
    }

    private fun sha256(input: ByteArray): ByteArray {
        val digest: MessageDigest
        return try {
            digest = MessageDigest.getInstance("SHA-256")
            digest.digest(input)
        } catch (e: java.lang.Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }
}

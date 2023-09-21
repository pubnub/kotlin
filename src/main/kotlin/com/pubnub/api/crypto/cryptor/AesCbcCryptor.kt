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

class AesCbcCryptor(val cipherKey: String) : Cryptor {
    private val keyBytes = sha256(cipherKey.toByteArray(Charsets.UTF_8))

    override fun id(): ByteArray {
        return byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'H'.code.toByte())
    }

    override fun encrypt(data: ByteArray): EncryptedData {
        return try {
            val ivBytes: ByteArray = createRandomIv()
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val newKey: SecretKeySpec = SecretKeySpec(keyBytes, "AES")
            val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            val encryptedData: ByteArray = cipher.doFinal(data)
            EncryptedData(metadata = ivBytes, data = encryptedData)
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        return try {
            val ivBytes: ByteArray = getIvBytes(encryptedData.metadata)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            val decryptedData = cipher.doFinal(encryptedData.data)
            decryptedData
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun encryptStream(stream: InputStream): Result<EncryptedStreamData> {
        TODO("Not yet implemented")
    }

    override fun decryptStream(encryptedData: EncryptedStreamData): Result<InputStream> {
        TODO("Not yet implemented")
    }

    private fun createRandomIv(): ByteArray {
        val ivBytes = ByteArray(16)
        SecureRandom().nextBytes(ivBytes)
        return ivBytes
    }

    private fun getIvBytes(ivBytes: ByteArray?): ByteArray {
        return if (ivBytes?.size == 16) {
            ivBytes
        } else {
            throw PubNubException("Invalid random IV")
        }
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

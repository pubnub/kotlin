package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import java.io.InputStream
import java.security.MessageDigest
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val STATIC_IV = "0123456789012345"
private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

class LegacyCryptor(val cipherKey: String, val useRandomIv: Boolean = true) : Cryptor {

    override fun id(): ByteArray {
        return ByteArray(4) { 0.toByte() } // it was agreed that legacy PN Cryptor will have 0 as ID
    }

    // for this Cryptor metadata stored in EncryptedData contains random or static iv
    override fun encrypt(data: ByteArray): EncryptedData {
        return try {
            val ivBytes: ByteArray = getIvBytesForEncryption()
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val keyBytes: ByteArray = getKeyAsShaInHexBytes(cipherKey)
            val newKey: SecretKeySpec = SecretKeySpec(keyBytes, "AES")
            val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            val encrypted: ByteArray = cipher.doFinal(data)
            val encryptedWithIV: ByteArray = ivBytes + encrypted

            EncryptedData(
                encryptedWithIV,
                ivBytes
            ) //todo legacy encrypt method returns String that is Base64. How to handle this?
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        return try {
            val ivBytes: ByteArray = getIvBytesForDecryption(encryptedData)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val keyBytes: ByteArray = getKeyAsShaInHexBytes(cipherKey)
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

    private fun getKeyAsShaInHexBytes(cipherKey: String): ByteArray {
        val cipherKeyBytes = cipherKey.toByteArray(Charsets.UTF_8)
        val cipherKeySha256Bytes: ByteArray = MessageDigest.getInstance("SHA-256").digest(cipherKeyBytes)
        val hexString = cipherKeySha256Bytes.joinToString("") { "%02x".format(it) }
        return hexString.toByteArray(Charsets.UTF_8)
    }

    private fun getIvBytesForEncryption(): ByteArray {
        val ivBytes = if (useRandomIv) {
            createRandomIv()
        } else {
            STATIC_IV.toByteArray()
        }
        return ivBytes
    }

    private fun createRandomIv(): ByteArray {
        val ivBytes = ByteArray(16)
        Random().nextBytes(ivBytes)
        return ivBytes
    }

    private fun getIvBytesForDecryption(encryptedData: EncryptedData): ByteArray {
        val ivBytes: ByteArray
        if (useRandomIv) {
            ivBytes = encryptedData.data.sliceArray(0..15)
        } else {
            ivBytes = STATIC_IV.toByteArray()
        }
        return ivBytes
    }
}

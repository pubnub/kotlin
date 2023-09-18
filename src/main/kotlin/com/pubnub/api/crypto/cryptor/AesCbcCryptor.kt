package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.api.vendor.Crypto
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

class AesCbcCryptor(val cipherKey: String) : Cryptor {
    override fun id(): ByteArray {
        return byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'H'.code.toByte())
    }

    // for this Cryptor metadata stored in EncryptedData contains random iv
    override fun encrypt(data: ByteArray): EncryptedData {
        return try {
            val ivBytes = createRandomIv()
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val keyBytes: ByteArray = getKeyAsShaInHexBytes(cipherKey)
            val newKey: SecretKeySpec = SecretKeySpec(keyBytes, "AES")
            val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            val encrypted: ByteArray = cipher.doFinal(data)
            val encryptedWithIV: ByteArray = ivBytes + encrypted

            EncryptedData(encryptedWithIV, ivBytes) // todo legacy encrypt method retruns String that is Base64
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        return try {
            val ivBytes: ByteArray = getIvBytes(encryptedData.metadata)
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

    private fun createRandomIv(): ByteArray {
        val ivBytes = ByteArray(16)
        Random().nextBytes(ivBytes)
        return ivBytes
    }

    private fun getKeyAsShaInHexBytes(cipherKey: String): ByteArray {

        return String(
            hexEncode(sha256(this.cipherKey.toByteArray(Charsets.UTF_8))),
            Charsets.UTF_8
        ).lowercase(Locale.getDefault()).toByteArray(Charsets.UTF_8)

//        val cipherKeyBytes = cipherKey.toByteArray(Charsets.UTF_8)
//        val cipherKeySha256String: String = String(MessageDigest.getInstance("SHA-256").digest(cipherKeyBytes)).substring(0, 64)
//        val cipherKeySha256Bytes: ByteArray = cipherKeySha256String.toByteArray()
//        val hexString = cipherKeySha256Bytes.joinToString("") { "%02x".format(it) }
//        return hexString.toByteArray(Charsets.UTF_8)
    }

    fun getIvBytes(ivBytes: ByteArray?): ByteArray {
        return if (ivBytes?.size == 16) {
            ivBytes
        } else {
            throw PubNubException("Invalid random IV")
        }
    }

    fun hexEncode(input: ByteArray): ByteArray {
        val result = StringBuilder()
        for (byt in input) {
            result.append(Integer.toString((byt.toInt() and 0xff) + 0x100, 16).substring(1))
        }
        try {
            return result.toString().toByteArray(Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    fun sha256(input: ByteArray?): ByteArray {
        val digest: MessageDigest
        return try {
            digest = MessageDigest.getInstance("SHA-256")
            digest.digest(input)
        } catch (e: Exception) {
            throw Crypto.newCryptoError(0, e)
        }
    }
}

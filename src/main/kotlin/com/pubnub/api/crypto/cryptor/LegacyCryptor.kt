package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val STATIC_IV = "0123456789012345"
private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

private const val IV_SIZE = 16
private const val RANDOM_IV_STARTING_INDEX = 0
private const val RANDOM_IV_ENDING_INDEX = 15
private const val ENCRYPTED_DATA_STARTING_INDEX = 16 // this is when useRandomIv = true

class LegacyCryptor(val cipherKey: String, val useRandomIv: Boolean = true) : Cryptor {
    private val newKey: SecretKeySpec = createNewKey()
    private val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)

    override fun id(): ByteArray {
        return ByteArray(4) { 0.toByte() } // it was agreed that legacy PN Cryptor will have 0 as ID
    }

    override fun encrypt(data: ByteArray): EncryptedData {
        return try {
            val ivBytes: ByteArray = getIvBytesForEncryption()
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            val encryptedData: ByteArray = cipher.doFinal(data)
            val finalEncryptedData: ByteArray = if (useRandomIv) {
                ivBytes + encryptedData
            } else {
                encryptedData
            }

            EncryptedData(
                metadata = null,
                data = finalEncryptedData
            )
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
    }

    override fun decrypt(encryptedData: EncryptedData): ByteArray {
        return try {
            val ivBytes: ByteArray = getIvBytesForDecryption(encryptedData)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            val encryptedDataForProcessing = getEncryptedDataForProcessing(encryptedData)
            val decryptedData = cipher.doFinal(encryptedDataForProcessing)
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

    private fun createNewKey(): SecretKeySpec {
        val keyBytes = String(hexEncode(sha256(cipherKey.toByteArray())), Charsets.UTF_8)
            .substring(0, 32)
            .lowercase(Locale.getDefault()).toByteArray()
        return SecretKeySpec(keyBytes, "AES")
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

    private fun hexEncode(input: ByteArray): ByteArray {
        val result = StringBuilder()
        for (byt in input) {
            result.append(Integer.toString((byt.toInt() and 0xff) + 0x100, 16).substring(1))
        }
        try {
            return result.toString().toByteArray()
        } catch (e: UnsupportedEncodingException) {
            throw PubNubException(errorMessage = e.message, pubnubError = PubNubError.CRYPTO_ERROR)
        }
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
        val ivBytes = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(ivBytes)
        return ivBytes
    }

    private fun getIvBytesForDecryption(encryptedData: EncryptedData): ByteArray {
        val ivBytes: ByteArray = if (useRandomIv) {
            encryptedData.data.sliceArray(RANDOM_IV_STARTING_INDEX..RANDOM_IV_ENDING_INDEX)
        } else {
            STATIC_IV.toByteArray()
        }
        return ivBytes
    }

    private fun getEncryptedDataForProcessing(encryptedData: EncryptedData): ByteArray {
        val encryptedDataForProcessing: ByteArray = if (useRandomIv) {
            encryptedData.data.sliceArray(ENCRYPTED_DATA_STARTING_INDEX until encryptedData.data.size)
        } else {
            // when there is useRandomIv = false then there is no IV in message
            encryptedData.data
        }
        return encryptedDataForProcessing
    }
}

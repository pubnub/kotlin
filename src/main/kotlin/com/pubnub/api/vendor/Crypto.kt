package com.pubnub.api.vendor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

internal class Crypto(internal var cipherKey: String) {

    private var keyBytes: ByteArray? = null
    private var ivBytes: ByteArray? = null
    private var initializationVector = "0123456789012345"
    private var initialized = false


    @Throws(PubNubException::class)
    fun initCiphers() {
        if (initialized)
            return
        try {

            keyBytes = String(hexEncode(sha256(this.cipherKey.toByteArray(charset("UTF-8")))), charset("UTF-8"))
                .substring(0, 32)
                .toLowerCase().toByteArray(charset("UTF-8"))
            ivBytes = initializationVector.toByteArray(charset("UTF-8"))
            initialized = true
        } catch (e: UnsupportedEncodingException) {
            throw throwCryptoError(e, 11)
        }
    }

    @Throws(PubNubException::class)
    fun hexEncode(input: ByteArray): ByteArray {
        val result = StringBuffer()
        input.forEach {
            result.append(((it and 0xff.toByte()) + 0x100).toString(16).substring(1))
        }
        try {
            return result.toString().toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            throw throwCryptoError(e, 12)
        }
    }

    @Throws(PubNubException::class)
    fun encrypt(input: String): String {
        try {
            initCiphers()
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(keyBytes, "AES")
            var cipher: Cipher? = null
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher!!.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            return String(
                Base64.encode(cipher.doFinal(input.toByteArray(charset("UTF-8"))), 0),
                Charset.forName("UTF-8")
            )
        } catch (e: Exception) {
            throw throwCryptoError(e)
        }
    }

    /**
     * Decrypt
     *
     * @param cipher_text
     * @return String
     * @throws PubNubException
     */
    @Throws(PubNubException::class)
    fun decrypt(cipher_text: String): String {
        try {
            initCiphers()
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            return String(cipher.doFinal(Base64.decode(cipher_text, 0)), charset("UTF-8"))
        } catch (e: Exception) {
            throw throwCryptoError(e)
        }
    }

    /**
     * Get MD5
     *
     * @param input
     * @return byte[]
     * @throws PubNubException
     */
    @Throws(PubNubException::class)
    fun md5(input: String): ByteArray {
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("MD5")
            return digest.digest(input.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            throw throwCryptoError(e, 118)
        } catch (e: UnsupportedEncodingException) {
            throw throwCryptoError(e, 119)
        }

    }

    /**
     * Get SHA256
     *
     * @param input
     * @return byte[]
     * @throws PubNubException
     */
    @Throws(PubNubException::class)
    fun sha256(input: ByteArray): ByteArray {
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("SHA-256")
            return digest.digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw throwCryptoError(e, 111)
        }
    }

    private fun throwCryptoError(exception: Exception, code: Int? = null): PubNubException {
        return PubNubException(PubNubError.CRYPTO_ERROR).apply {
            errorMessage += " ${exception.message}, $code"
        }
    }

}
package com.pubnub.api.crypto.legacy

import com.pubnub.api.crypto.Decryptor
import com.pubnub.api.crypto.Encryptor
import com.pubnub.api.crypto.HeadlessEncryptedData
import com.pubnub.api.crypto.IncomingEncryptedData
import com.pubnub.api.crypto.OutgoingEncryptedData
import com.pubnub.api.vendor.Crypto
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DynamicIvAESCBCLegacyCryptor : Encryptor, Decryptor {
    override fun encrypt(input: ByteArray, cipherKey: String): OutgoingEncryptedData {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, cipherKey.legacyKeySpec(), randomIvSpec())
        return HeadlessEncryptedData(cipher.doFinal(input))
    }

    override fun decrypt(input: IncomingEncryptedData, cipherKey: String): ByteArray {
        val content = input.content
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val ivSpec = content.take(IV_SIZE_BYTES).toByteArray().ivSpec()
        cipher.init(Cipher.DECRYPT_MODE, cipherKey.legacyKeySpec(), ivSpec)
        return cipher.doFinal(content, IV_SIZE_BYTES, content.size - IV_SIZE_BYTES)
    }

    companion object {
        private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val IV_SIZE_BYTES = 16
        private val secureRandom = SecureRandom.getInstance("SHA1PRNG")

        private fun randomIvSpec(): IvParameterSpec {
            val randomIv = ByteArray(IV_SIZE_BYTES)
            secureRandom.nextBytes(randomIv)
            return randomIv.ivSpec()
        }

        fun create(): DynamicIvAESCBCLegacyCryptor {
            return DynamicIvAESCBCLegacyCryptor()
        }
    }
}

class StaticIvAESCBCLegacyCryptor(
    private val ivSpec: AlgorithmParameterSpec
) : Encryptor, Decryptor {

    override fun encrypt(input: ByteArray, cipherKey: String): OutgoingEncryptedData {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, cipherKey.legacyKeySpec(), ivSpec)
        return HeadlessEncryptedData(cipher.doFinal(input))
    }

    override fun decrypt(input: IncomingEncryptedData, cipherKey: String): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, cipherKey.legacyKeySpec(), ivSpec)
        return cipher.doFinal(input.content)
    }

    companion object {
        private const val STATIC_IV = "0123456789012345"
        private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

        fun create(customInitializationVector: String?): StaticIvAESCBCLegacyCryptor {
            val ivSpec: AlgorithmParameterSpec =
                IvParameterSpec((customInitializationVector ?: STATIC_IV).toByteArray())

            return StaticIvAESCBCLegacyCryptor(ivSpec)
        }
    }
}

internal fun String.legacyKeySpec(): SecretKeySpec {
    val keyBytes = String(
        Crypto.hexEncode(Crypto.sha256(toByteArray()))
    ).substring(0, 32).lowercase(Locale.getDefault()).toByteArray()

    return SecretKeySpec(keyBytes, "AES")
}

internal fun ByteArray.ivSpec() = IvParameterSpec(this)

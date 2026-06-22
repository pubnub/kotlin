package com.pubnub.api.crypto

import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.logging.LogConfig
import java.io.InputStream

interface CryptoModule {
    fun encrypt(data: ByteArray): ByteArray

    fun decrypt(encryptedData: ByteArray): ByteArray

    companion object {
        /**
         * Creates a [CryptoModule] using the legacy PubNub cryptor for encryption and able to decrypt
         * data produced by both the legacy and the AES-CBC cryptors.
         *
         * @param cipherKey Use a randomly generated key instead of a dictionary word to increase security.
         * @param randomIv whether to prepend a random initialization vector to the ciphertext (recommended).
         */
        @JvmStatic
        fun createLegacyCryptoModule(
            cipherKey: String,
            randomIv: Boolean = true,
        ): CryptoModule {
            return instantiateCryptoModuleImpl(
                primaryCryptor = instantiateLegacyCryptor(cipherKey, randomIv),
                cryptorsForDecryptionOnly = listOf(
                    instantiateLegacyCryptor(cipherKey, randomIv),
                    instantiateAesCbcCryptor(cipherKey)
                ),
            )
        }

        /**
         * Creates a [CryptoModule] using the AES-CBC cryptor for encryption and able to decrypt
         * data produced by both the AES-CBC and the legacy cryptors.
         *
         * @param cipherKey Use a randomly generated key instead of a dictionary word to increase security.
         * @param randomIv applies only to the legacy cryptor kept for decryption: whether data being
         * decrypted in the legacy format is expected to carry a random initialization vector. The AES-CBC
         * cryptor used for encryption always uses a random IV regardless of this flag.
         */
        @JvmStatic
        fun createAesCbcCryptoModule(
            cipherKey: String,
            randomIv: Boolean = true,
        ): CryptoModule {
            return instantiateCryptoModuleImpl(
                primaryCryptor = instantiateAesCbcCryptor(cipherKey),
                cryptorsForDecryptionOnly = listOf(
                    instantiateAesCbcCryptor(cipherKey),
                    instantiateLegacyCryptor(cipherKey, randomIv)
                ),
            )
        }

        /**
         * Creates a [CryptoModule] from custom [Cryptor] implementations: [defaultCryptor] is used for
         * encryption, and any [cryptorsForDecryptionOnly] are additionally available for decryption.
         *
         * @param defaultCryptor the cryptor used to encrypt data and to decrypt data it produced.
         * @param cryptorsForDecryptionOnly additional cryptors used only when decrypting data produced
         * by other cryptors.
         */
        @JvmStatic
        fun createNewCryptoModule(
            defaultCryptor: Cryptor,
            cryptorsForDecryptionOnly: List<Cryptor> = listOf(),
        ): CryptoModule {
            return instantiateCryptoModuleImpl(
                primaryCryptor = defaultCryptor,
                cryptorsForDecryptionOnly = listOf(defaultCryptor) + cryptorsForDecryptionOnly,
            )
        }
    }

    fun encryptStream(stream: InputStream): InputStream

    fun decryptStream(encryptedData: InputStream): InputStream
}

private fun instantiateCryptoModuleImpl(
    primaryCryptor: Cryptor,
    cryptorsForDecryptionOnly: List<Cryptor>,
): CryptoModule {
    return Class.forName("com.pubnub.internal.crypto.CryptoModuleImpl")
        .getConstructor(Cryptor::class.java, List::class.java, LogConfig::class.java)
        .newInstance(
            primaryCryptor,
            cryptorsForDecryptionOnly,
            null
        ) as CryptoModule
}

private fun instantiateLegacyCryptor(
    cipherKey: String,
    randomIv: Boolean = true,
): Cryptor {
    return Class.forName(
        "com.pubnub.internal.crypto.cryptor.LegacyCryptor",
    ).getConstructor(String::class.java, Boolean::class.java).newInstance(
        cipherKey,
        randomIv,
    ) as Cryptor
}

private fun instantiateAesCbcCryptor(cipherKey: String): Cryptor {
    return Class.forName("com.pubnub.internal.crypto.cryptor.AesCbcCryptor").getConstructor(String::class.java)
        .newInstance(
            cipherKey,
        ) as Cryptor
}

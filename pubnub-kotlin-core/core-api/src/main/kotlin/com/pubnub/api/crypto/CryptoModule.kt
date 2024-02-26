package com.pubnub.api.crypto

import com.pubnub.api.crypto.cryptor.Cryptor
import java.io.InputStream

interface CryptoModule {
    fun encrypt(data: ByteArray): ByteArray

    fun decrypt(encryptedData: ByteArray): ByteArray

    companion object {
        @JvmStatic
        fun createLegacyCryptoModule(
            cipherKey: String,
            randomIv: Boolean = true,
        ): CryptoModule {
            return instantiateCryptoModuleImpl(
                primaryCryptor = instantiateLegacyCryptor(cipherKey, randomIv),
                cryptorsForDecryptionOnly = listOf(instantiateLegacyCryptor(cipherKey, randomIv), instantiateAesCbcCryptor(cipherKey)),
            )
        }

        @JvmStatic
        fun createAesCbcCryptoModule(
            cipherKey: String,
            randomIv: Boolean = true,
        ): CryptoModule {
            return instantiateCryptoModuleImpl(
                primaryCryptor = instantiateAesCbcCryptor(cipherKey),
                cryptorsForDecryptionOnly = listOf(instantiateAesCbcCryptor(cipherKey), instantiateLegacyCryptor(cipherKey, randomIv)),
            )
        }

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
    return Class.forName("com.pubnub.internal.crypto.CryptoModuleImpl").getConstructor(Cryptor::class.java, List::class.java).newInstance(
        primaryCryptor,
        cryptorsForDecryptionOnly,
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
    return Class.forName("com.pubnub.internal.crypto.cryptor.AesCbcCryptor").getConstructor(String::class.java).newInstance(
        cipherKey,
    ) as Cryptor
}

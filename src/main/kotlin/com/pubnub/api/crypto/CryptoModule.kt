package com.pubnub.api.crypto

import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.cryptor.HeaderParser
import com.pubnub.api.crypto.cryptor.LegacyCryptor
import com.pubnub.api.crypto.cryptor.ParseResult
import com.pubnub.api.crypto.data.EncryptedData

class CryptoModule internal constructor(
    internal val primaryCryptor: Cryptor,
    internal val cryptorsForDecryptionOnly: List<Cryptor> = listOf(),
    internal val headerParser: HeaderParser = HeaderParser()
) {

    companion object {
        fun createLegacyCryptoModule(cipherKey: String, randomIv: Boolean = true): CryptoModule {
            return CryptoModule(
                primaryCryptor = LegacyCryptor(cipherKey, randomIv),
                cryptorsForDecryptionOnly = listOf(LegacyCryptor(cipherKey, randomIv), AesCbcCryptor(cipherKey))
            )
        }

        fun createAesCbcCryptoModule(cipherKey: String, randomIv: Boolean = true): CryptoModule {
            return CryptoModule(
                primaryCryptor = AesCbcCryptor(cipherKey),
                cryptorsForDecryptionOnly = listOf(AesCbcCryptor(cipherKey), LegacyCryptor(cipherKey, randomIv))
            )
        }

        fun createNewCryptoModule(
            defaultCryptor: Cryptor,
            cryptorsForDecryptionOnly: List<Cryptor> = listOf()
        ): CryptoModule {
            return CryptoModule(
                primaryCryptor = defaultCryptor,
                cryptorsForDecryptionOnly = listOf(defaultCryptor) + cryptorsForDecryptionOnly
            )
        }
    }

    fun encrypt(data: ByteArray): ByteArray {
        val (metadata, encryptedData) = primaryCryptor.encrypt(data)
        val encryptionResult: ByteArray = if (primaryCryptorIsLegacyCryptor()) {
            encryptedData
        } else {
            val cryptorHeader = headerParser.createCryptorHeader(primaryCryptor.id(), metadata)
            cryptorHeader + encryptedData
        }
        return encryptionResult
    }

    fun decrypt(encryptedData: ByteArray): ByteArray {
        val parsedData: ParseResult = headerParser.parseDataWithHeader(encryptedData)
        val decryptedData: ByteArray = when (parsedData) {
            is ParseResult.NoHeader -> {
                getDecryptedDataForLegacyCryptor(encryptedData)
            }
            is ParseResult.Success -> {
                getDecryptedDataForCryptorWithHeader(parsedData)
            }
        }
        return decryptedData
    }

    private fun primaryCryptorIsLegacyCryptor() = primaryCryptor.id().contentEquals(ByteArray(4) { 0.toByte() })

    private fun getDecryptedDataForLegacyCryptor(encryptedData: ByteArray): ByteArray {
        return getLegacyCryptor()?.decrypt(EncryptedData(data = encryptedData))
            ?: throw PubNubException("LegacyCryptor not available")
    }

    private fun getDecryptedDataForCryptorWithHeader(parsedHeader: ParseResult.Success): ByteArray {
        val decryptedData: ByteArray
        val cryptorId = parsedHeader.cryptoId
        val cryptorData = parsedHeader.cryptorData
        val pureEncryptedData = parsedHeader.encryptedData
        val cryptor = getCryptorById(cryptorId)
        decryptedData =
            cryptor?.decrypt(EncryptedData(cryptorData, pureEncryptedData)) ?: throw PubNubException("No cryptor found")
        return decryptedData
    }

    private fun getLegacyCryptor(): Cryptor? {
        val idOfLegacyCryptor = ByteArray(4) { 0.toByte() }
        return getCryptorById(idOfLegacyCryptor)
    }

    private fun getCryptorById(cryptorId: ByteArray): Cryptor? {
        return cryptorsForDecryptionOnly.firstOrNull { it.id().contentEquals(cryptorId) }
    }
}

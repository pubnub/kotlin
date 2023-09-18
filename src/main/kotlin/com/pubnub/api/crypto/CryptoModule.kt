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
    internal val cryptorsForEncryptionOnly: List<Cryptor> = emptyList(),
    internal val headerParser: HeaderParser = HeaderParser()
) {

    constructor(primaryCryptor: Cryptor, cryptorsForEncryptionOnly: List<Cryptor> = emptyList()) : this(
        primaryCryptor,
        cryptorsForEncryptionOnly,
        HeaderParser()
    )

    companion object {
        fun createLegacyCryptoModule(cipherKey: String, randomIv: Boolean = true): CryptoModule {
            return CryptoModule(
                primaryCryptor = LegacyCryptor(cipherKey, randomIv),
                cryptorsForEncryptionOnly = listOf(AesCbcCryptor(cipherKey))
            )
        }

        fun createAesCbcCryptoModule(cipherKey: String, randomIvForLegacyCryptor: Boolean): CryptoModule {
            return CryptoModule(
                primaryCryptor = AesCbcCryptor(cipherKey),
                cryptorsForEncryptionOnly = listOf(LegacyCryptor(cipherKey, randomIvForLegacyCryptor))
            )
        }

        fun createNewCryptoModule(defaultCryptor: Cryptor, decryptors: List<Cryptor>): CryptoModule {
            return CryptoModule(
                primaryCryptor = defaultCryptor,
                cryptorsForEncryptionOnly = decryptors
            )
        }
    }

    fun encrypt(data: ByteArray): ByteArray {
        val (metadata, encryptedData) = primaryCryptor.encrypt(data)
        val cryptorHeader = headerParser.createCryptorHeader(primaryCryptor.id(), metadata)
        val encryptedDataWithHeader = cryptorHeader + encryptedData
        return encryptedDataWithHeader
    }

    fun decrypt(encryptedData: ByteArray): ByteArray {
        val parsedHeader = headerParser.parseHeader(encryptedData)
        val decryptedData: ByteArray
        when (parsedHeader) {
            is ParseResult.NoHeader -> {
                decryptedData = getDecryptedDataForLegacyCryptor(encryptedData)
            }
            is ParseResult.Success -> {
                decryptedData = getDecryptedDataForCryptorWithHeader(parsedHeader)
            }
        }
        return decryptedData
    }

    private fun getDecryptedDataForLegacyCryptor(encryptedData: ByteArray): ByteArray {
        val decryptedData: ByteArray
        val legacyCryptor = getLegacyCryptor()
        decryptedData = legacyCryptor?.decrypt(EncryptedData(null, encryptedData))
            ?: throw PubNubException("LegacyCryptor not available")
        return decryptedData
    }

    private fun getDecryptedDataForCryptorWithHeader(parsedHeader: ParseResult.Success): ByteArray {
        val decryptedData: ByteArray
        val cryptoId = parsedHeader.cryptoId
        val cryptorData = parsedHeader.cryptorData
        val pureEncryptedData = parsedHeader.encryptedData
        val potentialDecryptionCryptors = listOf(primaryCryptor) + cryptorsForEncryptionOnly
        val cryptoIdByteArray = cryptoId.toByteArray(Charsets.UTF_8)
        val cryptor = potentialDecryptionCryptors.filter { it.id().contentEquals(cryptoIdByteArray) }.first()
        decryptedData = cryptor.decrypt(EncryptedData(cryptorData, pureEncryptedData))
        return decryptedData
    }

    private fun getLegacyCryptor(): Cryptor? {
        val idOfLegacyCryptor = ByteArray(4) { 0.toByte() }
        val decryptionCryptors = listOf(primaryCryptor) + cryptorsForEncryptionOnly
        val legacyCryptors = decryptionCryptors.filter { it.id().contentEquals(idOfLegacyCryptor) }
        return legacyCryptors.firstOrNull()
    }
}

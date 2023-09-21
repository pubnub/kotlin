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
    internal val cryptorsForEncryptionOnly: MutableList<Cryptor> = mutableListOf(),
    internal val headerParser: HeaderParser = HeaderParser()
) {

    init {
        setUpDecryptorsList()
    }

    constructor(primaryCryptor: Cryptor, cryptorsForEncryptionOnly: MutableList<Cryptor> = mutableListOf()) : this(
        primaryCryptor,
        cryptorsForEncryptionOnly,
        HeaderParser()
    )

    companion object {
        fun createLegacyCryptoModule(cipherKey: String, randomIv: Boolean = true): CryptoModule {
            return CryptoModule(
                primaryCryptor = LegacyCryptor(cipherKey, randomIv),
                cryptorsForEncryptionOnly = mutableListOf(AesCbcCryptor(cipherKey))
            )
        }

        fun createAesCbcCryptoModule(cipherKey: String, randomIv: Boolean = true): CryptoModule {
            return CryptoModule(
                primaryCryptor = AesCbcCryptor(cipherKey),
                cryptorsForEncryptionOnly = mutableListOf(LegacyCryptor(cipherKey, randomIv))
            )
        }

        fun createNewCryptoModule(
            defaultCryptor: Cryptor,
            cryptorsForEncryptionOnly: List<Cryptor> = listOf()
        ): CryptoModule {
            return CryptoModule(
                primaryCryptor = defaultCryptor,
                cryptorsForEncryptionOnly = cryptorsForEncryptionOnly.toMutableList()
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
        val parsedHeader = headerParser.parseHeader(encryptedData)
        val decryptedData: ByteArray = when (parsedHeader) {
            is ParseResult.NoHeader -> {
                getDecryptedDataForLegacyCryptor(encryptedData)
            }
            is ParseResult.Success -> {
                getDecryptedDataForCryptorWithHeader(parsedHeader)
            }
        }
        return decryptedData
    }

    private fun setUpDecryptorsList() {
        cryptorsForEncryptionOnly.add(primaryCryptor)
    }

    private fun primaryCryptorIsLegacyCryptor() = primaryCryptor.id().contentEquals(ByteArray(4) { 0.toByte() })

    private fun getDecryptedDataForLegacyCryptor(encryptedData: ByteArray): ByteArray {
        val decryptedData: ByteArray
        val legacyCryptor = getLegacyCryptor()
        decryptedData = legacyCryptor?.decrypt(EncryptedData(null, encryptedData))
            ?: throw PubNubException("LegacyCryptor not available")
        return decryptedData
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
        return cryptorsForEncryptionOnly.firstOrNull { it.id().contentEquals(cryptorId) }
    }
}

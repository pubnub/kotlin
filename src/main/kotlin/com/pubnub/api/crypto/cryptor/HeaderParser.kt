package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubException
import org.slf4j.LoggerFactory

private const val SENTINEL = "PNED"
private const val STARTING_INDEX_OF_CRYPTOR_DATA_FIRST_OPTION = 10
private const val STARTING_INDEX_OF_CRYPTOR_DATA_SECOND_OPTION = 12
private const val MINIMAL_SIZE_OF_DATA_HAVING_CRYPTOR_HEADER = 10
private const val INDICATOR_THAT_CRYPTOR_DATA_SIZE_IS_STORED_ON_2_BYTES = 255

class HeaderParser {
    private val log = LoggerFactory.getLogger(HeaderParser::class.java)

    fun parseHeader(data: ByteArray): ParseResult {

        val sentinel = data.sliceArray(0..3)
        if (SENTINEL != String(sentinel)) {
            return ParseResult.NoHeader
        }

        if (data.size < MINIMAL_SIZE_OF_DATA_HAVING_CRYPTOR_HEADER) { //minimal size of Cryptor Data Header
            throw PubNubException(errorMessage = "Minimal size of Cryptor Data Header is: $MINIMAL_SIZE_OF_DATA_HAVING_CRYPTOR_HEADER")
        }

        validateCryptorHeaderVersion(data)

        val cryptoId = data.sliceArray(5..8)
        val cryptoIdAsString = String(cryptoId, Charsets.US_ASCII)
        log.trace("CryptoId: $cryptoIdAsString")

        val cryptorDataSizeFirstByte = data[9]
        val (startingIndexOfCryptorData, cryptorDataSize) = getCryptorDataSizeAndStartingIndex(
            data,
            cryptorDataSizeFirstByte
        )

        if (startingIndexOfCryptorData + cryptorDataSize > data.size) {
            throw PubNubException(errorMessage = "Input data size: ${data.size} is to small to fit header of size $startingIndexOfCryptorData and cryptorData of size: $cryptorDataSize")
        }
        val cryptorData =
            data.sliceArray(startingIndexOfCryptorData until (startingIndexOfCryptorData + cryptorDataSize))
        val sizeOfCryptorHeader = startingIndexOfCryptorData + cryptorDataSize
        val encryptedData = data.sliceArray(sizeOfCryptorHeader until data.size)

        return ParseResult.Success(cryptoIdAsString, cryptorData, encryptedData)
    }

    private fun returnSize(data: ByteArray?): Int {
        val size: Int
        if (data != null) {
            return data.size
        } else {
            return 0
        }
    }

    fun createCryptorHeader(cryptorId: ByteArray, cryptorData: ByteArray?): ByteArray {
        val sentinel: ByteArray = SENTINEL.toByteArray(Charsets.US_ASCII)
        val cryptorHeaderVersion: Byte = getCurrentCryptoHeaderVersion()
        val cryptorDataSize: Int = cryptorData?.size ?: 0
        val finalCryptorDataSize: ByteArray =
            if (cryptorDataSize < INDICATOR_THAT_CRYPTOR_DATA_SIZE_IS_STORED_ON_2_BYTES) {
                byteArrayOf(cryptorDataSize.toByte()) //cryptorDataSize will be stored on 1 byte
            } else if (cryptorDataSize in INDICATOR_THAT_CRYPTOR_DATA_SIZE_IS_STORED_ON_2_BYTES..65535) {
                byteArrayOf(cryptorDataSize.toByte()) + writeNumberOnTwoBytes(cryptorDataSize) //cryptorDataSize will be stored on 3 byte
            } else {
                throw PubNubException(errorMessage = "Something is wrong with Cryptor Data Size: $cryptorDataSize")
            }

        val cryptorHeader =
            CryptorHeader(sentinel, cryptorHeaderVersion, cryptorId, finalCryptorDataSize, cryptorData ?: byteArrayOf())
        return cryptorHeader.toByteArray()
    }

    private fun getCurrentCryptoHeaderVersion(): Byte {
        return CryptorHeaderVersion.One.value.toByte()
    }

    private fun getCryptorDataSizeAndStartingIndex(data: ByteArray, cryptorDataSizeFirstByte: Byte): Pair<Int, Int> {
        val startingIndexOfCryptorData: Int
        val cryptorDataSize: Int
        val cryptoDataFirstByteAsString = convertByteValueToInt(cryptorDataSizeFirstByte)
        if (cryptoDataFirstByteAsString < INDICATOR_THAT_CRYPTOR_DATA_SIZE_IS_STORED_ON_2_BYTES) {
            startingIndexOfCryptorData = STARTING_INDEX_OF_CRYPTOR_DATA_FIRST_OPTION
            cryptorDataSize = cryptoDataFirstByteAsString
            log.trace("\"Cryptor data size\" is 1 byte long and its value is: $cryptorDataSize")
        } else if (cryptoDataFirstByteAsString == INDICATOR_THAT_CRYPTOR_DATA_SIZE_IS_STORED_ON_2_BYTES) {
            startingIndexOfCryptorData = STARTING_INDEX_OF_CRYPTOR_DATA_SECOND_OPTION
            log.trace("\"Cryptor data size\" first byte's value is 255 that mean that size is stored on two next bytes")
            val cryptorDataSizeSecondByte = data[10]
            val cryptorDataSizeThirdByte = data[11]
            cryptorDataSize = convertTwoBytesToIntBigEndian(cryptorDataSizeSecondByte, cryptorDataSizeThirdByte)
        } else {
            throw PubNubException(errorMessage = "Something is wrong with Cryptor header length")
        }

        return Pair(startingIndexOfCryptorData, cryptorDataSize)
    }

    private fun validateCryptorHeaderVersion(data: ByteArray) {
        val version = data[4] //5th byte
        val versionAsInt = convertByteValueToInt(version)
        log.trace("Cryptor header version is: $versionAsInt")
        //check if version exist in this SDK version
        CryptorHeaderVersion.fromValue(versionAsInt)
            ?: PubNubException(errorMessage = "Cryptor version unknown. Please, update SDK")//toDo
    }

    private fun convertTwoBytesToIntBigEndian(byte1: Byte, byte2: Byte): Int {
        return ((byte1.toInt() and 0xFF) shl 8) or (byte2.toInt() and 0xFF)
    }

    private fun writeNumberOnTwoBytes(number: Int): ByteArray {
        val result = ByteArray(2)

        result[0] = (number shr 8).toByte()
        result[1] = number.toByte()

        return result
    }

    // byte is -128 to 127 to have it 0 to 255 perform unsigned conversion
    private fun convertByteValueToInt(cryptorDataSizeFirstByte: Byte): Int = cryptorDataSizeFirstByte.toInt() and 0xFF

}

sealed class ParseResult { //toDo modify
    data class Success(val cryptoId: String, val cryptorData: ByteArray, val encryptedData: ByteArray) :
        ParseResult()

    object NoHeader : ParseResult() //todo think about different name NoHeader
}

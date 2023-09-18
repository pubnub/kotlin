package com.pubnub.api.crypto.cryptor

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HeaderParserTest {
    private lateinit var objectUnderTest: HeaderParser

    @BeforeEach
    fun setUp() {
        objectUnderTest = HeaderParser()
    }

    @Test
    fun `check return type`() {
        val dataWithCryptorHeader: ByteArray =
            byteArrayOf(0x50, 0x4E, 0x45, 0x44, 0x01, 0x43, 0x52, 0x49, 0x56, 0x10, 0x10)
//        val parseHeader = objectUnderTest.parseHeader(dataWithCryptorHeader)
        println("")

        println(dataWithCryptorHeader)
    }

    @Test
    fun `can create and parse header when cryptorDataSize is 1`() {
        val dataToBeEncrypted: ByteArray = byteArrayOf(
            0x56,
            0x7E,
            0x45,
            0x44,
            0x01,
            0x43,
            0x52,
            0x49,
            0x56,
            0x10,
            0x10
        ) // 0x56 = P 0x7E = N 0x45 = E 0x44 = D
        val cryptorId: ByteArray =
            byteArrayOf('C'.code.toByte(), 'R'.code.toByte(), 'I'.code.toByte(), 'V'.code.toByte()) // "CRIV"

        val cryptorData = byteArrayOf(0x50)
        val cryptorHeader =
            objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        val headerWithData: ByteArray = cryptorHeader + dataToBeEncrypted
        val parseResult = objectUnderTest.parseHeader(headerWithData)

        when (parseResult) {
            is ParseResult.NoHeader -> println("No valid header")
            is ParseResult.Success -> {
                assertTrue(cryptorId.contentEquals(parseResult.cryptoId.toByteArray()))
                assertTrue(cryptorData.contentEquals(parseResult.cryptorData))
                assertTrue(dataToBeEncrypted.contentEquals(parseResult.encryptedData))
            }
        }
    }

    @Test
    fun `should return InvalidSentinel`() {
        val cryptorHeaderWithInvalidSentinel =
            byteArrayOf(0x56, 0x56, 0x56, 0x56, 0x01, 0x43, 0x52, 0x49, 0x56, 0x10, 0x10)
        val parseResult = objectUnderTest.parseHeader(cryptorHeaderWithInvalidSentinel)

        when (parseResult) {
            is ParseResult.NoHeader -> {
                println("Should print that")
            }
            is ParseResult.Success -> {
                throw Exception("Should not return Success")
            }
        }
    }
}

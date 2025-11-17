package com.pubnub.api.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.logging.LogConfig
import com.pubnub.internal.crypto.cryptor.HeaderParser
import com.pubnub.internal.crypto.cryptor.ParseResult
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HeaderParserTest {
    private lateinit var objectUnderTest: HeaderParser

    @BeforeEach
    fun setUp() {
        objectUnderTest = HeaderParser(LogConfig("defaultInstanceId", "user01"))
    }

    @Test
    fun `can create and parse data with header when cryptorDataSize is 1`() {
        val cryptorId: ByteArray =
            byteArrayOf('C'.code.toByte(), 'R'.code.toByte(), 'I'.code.toByte(), 'V'.code.toByte()) // "CRIV"

        val cipherKey = "enigma"
        val cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey, false)
        val cryptorData =
            byteArrayOf(0x50, 0x56, 0x56, 0x56, 0x56, 0x01, 0x43, 0x52, 0x49, 0x56, 0x10, 0x10, 0x56, 0x56, 0x56, 0x10)
        val cryptorHeader = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        val dataToBeEncrypted = byteArrayOf('D'.code.toByte(), 'A'.code.toByte())
        val encryptedData = cryptoModule.encrypt(dataToBeEncrypted)
        val headerWithData: ByteArray = cryptorHeader + encryptedData
        val parseResult = objectUnderTest.parseDataWithHeader(headerWithData)

        when (parseResult) {
            is ParseResult.NoHeader -> fail("Expected header")
            is ParseResult.Success -> {
                assertTrue(cryptorId.contentEquals(parseResult.cryptoId))
                assertTrue(cryptorData.contentEquals(parseResult.cryptorData))
                assertTrue(encryptedData.contentEquals(parseResult.encryptedData))
            }
        }
    }

    @Test
    fun `can create and parse data with header when cryptorDataSize is 3`() {
        val cryptorId: ByteArray =
            byteArrayOf('C'.code.toByte(), 'R'.code.toByte(), 'I'.code.toByte(), 'V'.code.toByte()) // "CRIV"
        val cryptorData = createByteArrayThatHas255Elements()
        val cryptorHeader = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        val dataToBeEncrypted = byteArrayOf('D'.code.toByte(), 'A'.code.toByte())
        val headerWithData: ByteArray = cryptorHeader + dataToBeEncrypted
        val parseResult = objectUnderTest.parseDataWithHeader(headerWithData)

        when (parseResult) {
            is ParseResult.NoHeader -> fail("Expected header")
            is ParseResult.Success -> {
                assertTrue(cryptorId.contentEquals(parseResult.cryptoId))
                assertTrue(cryptorData.contentEquals(parseResult.cryptorData))
                assertTrue(dataToBeEncrypted.contentEquals(parseResult.encryptedData))
            }
        }
    }

    @Test
    fun `should return NoHeader when there is no sentinel`() {
        val cryptorHeaderWithInvalidSentinel =
            byteArrayOf(
                0x56,
                0x56,
                0x56,
                0x56,
                0x01,
                0x43,
                0x52,
                0x49,
                0x56,
                0x10,
                0x10,
                0x56,
                0x56,
                0x56,
                0x56,
                0x01,
                0x43,
                0x52,
                0x49,
                0x56,
                0x10,
                0x10,
            )
        val parseResult = objectUnderTest.parseDataWithHeader(cryptorHeaderWithInvalidSentinel)

        assertThat(parseResult, `is`(ParseResult.NoHeader))
    }

    @Test
    fun `should throw exception when input data are to short`() {
        val cryptorHeaderWithToShortData =
            byteArrayOf(80, 78, 69, 68, 1, 43, 52, 49, 56)

        val exception: PubNubException =
            assertThrows(PubNubException::class.java) {
                objectUnderTest.parseDataWithHeader(cryptorHeaderWithToShortData)
            }

        assertEquals("Minimal size of encrypted data having Cryptor Data Header is: 10", exception.errorMessage)
        assertEquals(PubNubError.CRYPTOR_DATA_HEADER_SIZE_TO_SMALL, exception.pubnubError)
    }

    @Test
    fun `createCryptorHeader should use 255 indicator for size 256`() {
        val cryptorId = byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'V'.code.toByte())
        val cryptorData = ByteArray(256) { it.toByte() }

        val header = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        // Header structure: PNED(4) + version(1) + cryptorId(4) + sizeField(3) + data(256)
        // Total: 268 bytes
        assertEquals(268, header.size, "Header size should be 268 bytes")

        // Byte 9 should be 255 (0xFF) - the indicator
        assertEquals(255.toByte(), header[9], "Byte 9 should be 255 (0xFF) indicator")

        // Bytes 10-11 should be 256 in big-endian (0x01, 0x00)
        assertEquals(1, header[10].toInt() and 0xFF, "Byte 10 should be 1 (high byte of 256)")
        assertEquals(0, header[11].toInt() and 0xFF, "Byte 11 should be 0 (low byte of 256)")
    }

    @Test
    fun `createCryptorHeader should use 255 indicator for size 300`() {
        val cryptorId = byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'V'.code.toByte())
        val cryptorData = ByteArray(300) { it.toByte() }

        val header = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        assertEquals(312, header.size, "Header size should be 312 bytes") // 4+1+4+3+300
        assertEquals(255.toByte(), header[9], "Byte 9 should be 255 (0xFF) indicator")
        assertEquals(1, header[10].toInt() and 0xFF, "Byte 10 should be 1 (300 >> 8 = 1)")
        assertEquals(44, header[11].toInt() and 0xFF, "Byte 11 should be 44 (300 & 0xFF = 44)")
    }

    @Test
    fun `createCryptorHeader should use 255 indicator for size 512`() {
        val cryptorId = byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'V'.code.toByte())
        val cryptorData = ByteArray(512) { it.toByte() }

        val header = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        assertEquals(524, header.size, "Header size should be 524 bytes") // 4+1+4+3+512
        assertEquals(255.toByte(), header[9], "Byte 9 should be 255 (0xFF) indicator")
        assertEquals(2, header[10].toInt() and 0xFF, "Byte 10 should be 2 (512 >> 8 = 2)")
        assertEquals(0, header[11].toInt() and 0xFF, "Byte 11 should be 0 (512 & 0xFF = 0)")
    }

    @Test
    fun `createCryptorHeader should use 255 indicator for size 65535`() {
        val cryptorId = byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'V'.code.toByte())
        val cryptorData = ByteArray(65535) { 0 }

        val header = objectUnderTest.createCryptorHeader(cryptorId, cryptorData)

        assertEquals(65547, header.size, "Header size should be 65547 bytes") // 4+1+4+3+65535
        assertEquals(255.toByte(), header[9], "Byte 9 should be 255 (0xFF) indicator")
        assertEquals(255.toByte(), header[10], "Byte 10 should be 255 (65535 >> 8 = 255)")
        assertEquals(255.toByte(), header[11], "Byte 11 should be 255 (65535 & 0xFF = 255)")
    }

    @Test
    fun `createCryptorHeader should round-trip with parseDataWithHeader for size 256`() {
        val cryptorId = byteArrayOf('A'.code.toByte(), 'C'.code.toByte(), 'R'.code.toByte(), 'V'.code.toByte())
        val originalData = ByteArray(256) { it.toByte() }
        val encryptedPayload = ByteArray(100) { 0xFF.toByte() }

        val header = objectUnderTest.createCryptorHeader(cryptorId, originalData)
        val fullData = header + encryptedPayload

        val result = objectUnderTest.parseDataWithHeader(fullData)

        assertTrue(result is ParseResult.Success, "Should parse successfully")
        result as ParseResult.Success
        assertTrue(cryptorId.contentEquals(result.cryptoId), "Cryptor ID should match")
        assertTrue(originalData.contentEquals(result.cryptorData), "Cryptor data should match")
        assertTrue(encryptedPayload.contentEquals(result.encryptedData), "Encrypted data should match")
    }

    private fun createByteArrayThatHas255Elements(): ByteArray {
        var byteArray: ByteArray = byteArrayOf()
        for (i in 1..255) {
            byteArray += byteArrayOf(i.toByte())
        }
        return byteArray
    }
}

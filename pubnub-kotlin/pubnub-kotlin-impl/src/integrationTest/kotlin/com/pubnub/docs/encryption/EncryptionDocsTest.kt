@file:Suppress("ktlint")

package com.pubnub.docs.encryption

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.InputStream

/**
 * Live tests of the Kotlin docs page encryption (docs/sdks/kotlin/api-reference/encryption.md) and of the two encryption
 * samples of `MiscellaneousOthers` that no page embeds (`createCryptoModuleBasic`, `customCryptor`). The embedded
 * `configurationWithCryptoModule` sample sits with the configuration tests. One test per snippet. The text between a start
 * marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it.
 */
@ExtendWith(LiveExtension::class)
class EncryptionDocsTest {
    private val OPEN = "open"
    private val CIPHER_KEY = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ"

    // the header of data that the crypto module encrypted with AES-CBC: "PNED", the format version and the cryptor id "ACRH"
    private val HEADER = byteArrayOf(80, 78, 69, 68, 1, 65, 67, 82, 72)

    private fun startsWithHeader(data: ByteArray) = data.size > HEADER.size && data.copyOfRange(0, HEADER.size).contentEquals(HEADER)

    @Test
    fun encryptString() {
        // snippet.encryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        // snippet.end

        assertFalse(encryptedData.contentEquals(stringToBeEncrypted.toByteArray()), "the data was not encrypted")
        assertTrue(startsWithHeader(encryptedData), "the encrypted data does not carry the AES-CBC header")
        assertFalse(String(encryptedData, Charsets.ISO_8859_1).contains(stringToBeEncrypted), "the text is still in the data")
        // a random IV: encrypting twice gives two different results, both decrypt
        val again = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        assertFalse(again.contentEquals(encryptedData), "two encryptions of the same text are equal")
        assertEquals(stringToBeEncrypted, String(aesCbcCryptoModule.decrypt(again)))
        assertEquals(stringToBeEncrypted, String(aesCbcCryptoModule.decrypt(encryptedData)))
        // another cipher key cannot read it
        val other = CryptoModule.createAesCbcCryptoModule("another-cipher-key-of-this-test")
        assertThrows(PubNubException::class.java) { other.decrypt(encryptedData) }
    }

    @Test
    fun encryptInputStream() {
        // snippet.encryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        // snippet.end

        val encryptedBytes = encryptedStream.readBytes()
        assertTrue(startsWithHeader(encryptedBytes), "the encrypted stream does not carry the AES-CBC header")
        assertFalse(String(encryptedBytes, Charsets.ISO_8859_1).contains(stringToBeEncrypted), "the text is still in the stream")
        assertEquals(stringToBeEncrypted, String(aesCbcCryptoModule.decryptStream(encryptedBytes.inputStream()).readBytes()))
    }

    @Test
    fun decryptString() {
        // snippet.decryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        val decryptedData = aesCbcCryptoModule.decrypt(encryptedData)
        // snippet.end

        assertArrayEquals(stringToBeEncrypted.toByteArray(), decryptedData)
        assertEquals(stringToBeEncrypted, String(decryptedData))
        // the legacy module reads the data of the recommended one, as the docs say, and the other way round
        val legacy = CryptoModule.createLegacyCryptoModule(CIPHER_KEY)
        assertEquals(stringToBeEncrypted, String(legacy.decrypt(encryptedData)))
        assertEquals(stringToBeEncrypted, String(aesCbcCryptoModule.decrypt(legacy.encrypt(stringToBeEncrypted.toByteArray()))))
    }

    @Test
    fun decryptInputStream() {
        // snippet.decryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"

        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        val decryptedStream = aesCbcCryptoModule.decryptStream(encryptedStream)
        // snippet.end

        assertEquals(stringToBeEncrypted, String(decryptedStream.readBytes()))
    }

    @Test
    fun createCryptoModuleBasic(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-config-receiver") {
            cryptoModule = CryptoModule.createAesCbcCryptoModule(CIPHER_KEY)
        }
        val message = probe("crypto-basic")
        val arrived = armMessage(receiver, "docs-config-crypto", message)
        subscribeAndConnect(receiver, listOf("docs-config-crypto"))
        Cleanup.record("history", "docs-config-crypto")

        // snippet.createCryptoModuleBasic
        val config = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            cryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ")
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }.build()

        val pubnub = PubNub.create(config)
        // snippet.end
        clients.track(pubnub)

        assertNotNull(pubnub.configuration.cryptoModule)
        val published = pubnub.publish("docs-config-crypto", message).sync()
        val received = arrived.await() as PNMessageResult
        assertEquals(message, received.message.asString)
        assertEquals("myUserId", received.publisher)
        assertEquals(published.timetoken, received.timetoken)
        // the history keeps the cipher text, the reader with the key gets the text back
        val stored = storedItem(receiver, "docs-config-crypto", published.timetoken)
        assertEquals(message, stored.message.asString)
        // the documented client methods use the configured module too
        val encrypted = pubnub.encrypt(message)
        assertNotEquals(message, encrypted)
        assertEquals(message, pubnub.decrypt(encrypted))
    }

    @Test
    fun customCryptor(clients: LiveClients) {
        val message = probe("custom-cryptor")

        // snippet.customCryptor
        fun myCustomCryptor() = object : Cryptor {
            override fun id(): ByteArray {
                // Should return a ByteArray of exactly 4 bytes.
                return byteArrayOf('C'.code.toByte(), 'U'.code.toByte(), 'S'.code.toByte(), 'T'.code.toByte())
            }

            override fun encrypt(data: ByteArray): EncryptedData {
                // implement your crypto logic
                return EncryptedData(metadata = null, data = data)
            }

            override fun decrypt(encryptedData: EncryptedData): ByteArray {
                // implement your crypto logic
                return encryptedData.data
            }

            override fun encryptStream(stream: InputStream): EncryptedStreamData {
                // implement your crypto logic
                return EncryptedStreamData(metadata = null, stream = stream)
            }

            override fun decryptStream(encryptedData: EncryptedStreamData): InputStream {
                // implement your crypto logic
                return encryptedData.stream
            }
        }
        // snippet.end

        val cryptor = myCustomCryptor()
        assertEquals("CUST", String(cryptor.id()))
        assertEquals(4, cryptor.id().size)
        // the module puts the id of the cryptor in the header, the pass-through logic keeps the data readable
        val module = CryptoModule.createNewCryptoModule(cryptor)
        val encrypted = module.encrypt(message.toByteArray())
        assertTrue(String(encrypted, Charsets.ISO_8859_1).contains("CUST"), "the header lacks the id of the custom cryptor")
        assertEquals(message, String(module.decrypt(encrypted)))
        assertEquals(message, String(module.decryptStream(module.encryptStream(message.byteInputStream())).readBytes()))
        // a client with that module publishes, a second one with the same module reads, one without it sees the header
        val plainReader = clients.kotlinClient(OPEN, "docs-config-sender")
        val moduleReader = clients.kotlinClient(OPEN, "docs-config-receiver") { cryptoModule = module }
        val seenPlain = armMessage(plainReader, "docs-config-crypto", "")
        val seenByModule = armMessage(moduleReader, "docs-config-crypto", message)
        subscribeAndConnect(plainReader, listOf("docs-config-crypto"))
        subscribeAndConnect(moduleReader, listOf("docs-config-crypto"))
        Cleanup.record("history", "docs-config-crypto")
        val writer = clients.kotlinClient(OPEN, "docs-config-writer") { cryptoModule = module }
        val published = writer.publish("docs-config-crypto", message).sync()
        assertEquals(message, (seenByModule.await() as PNMessageResult).message.asString)
        val raw = seenPlain.await() as PNMessageResult
        assertEquals(published.timetoken, raw.timetoken)
        assertNotEquals(message, raw.message.asString)
    }

    @Test
    fun documentedMethodsUseTheConfiguredModule(clients: LiveClients) {
        // The page documents pubnub.encrypt(inputString) and pubnub.decrypt(inputString) next to the crypto module samples.
        val client = clients.kotlinClient(OPEN, "docs-config-sender") { cryptoModule = CryptoModule.createAesCbcCryptoModule(CIPHER_KEY) }
        val encrypted = client.encrypt("string to be encrypted")
        assertNotEquals("string to be encrypted", encrypted)
        assertEquals("string to be encrypted", client.decrypt(encrypted))
        val withoutModule = clients.kotlinClient(OPEN, "docs-config-receiver")
        assertNull(withoutModule.configuration.cryptoModule)
        assertThrows(PubNubException::class.java) { withoutModule.encrypt("string to be encrypted") }
    }
}

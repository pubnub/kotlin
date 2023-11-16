package com.pubnub.extension

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.encryptString
import com.pubnub.api.managers.MapperManager
import com.pubnub.contract.gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class JsonElementTest {

    companion object {
        val gson = Gson()

        @JvmStatic
        fun cryptoModuleConfiguration(): List<Arguments> {
            val cipherKey = "enigma"
            return listOf(
                Arguments.of(
                    JsonPrimitive("Hello world."),
                    false,
                    "X/wLZqR/I+4eU7WwtKycTwy9+L9e7m2BhklK9ZQWtwI=",
                    CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true),
                    generatePayload()
                ),
                Arguments.of(
                    JsonPrimitive("Hello world."),
                    false,
                    "bk8x+ZEg+Roq8ngUo7lfFg==",
                    CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = false),
                    generatePayload()
                ),
                Arguments.of(
                    JsonPrimitive("Hello world."),
                    false,
                    "UE5FRAFBQ1JIEK8FoITMdCOcG1mPQrtf31N5ZtA2MY/6QPLJ1DURjnwo",
                    CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true),
                    generatePayload()
                ),
                Arguments.of(
                    gson.fromJson("{\"name\":\"joe\",\"age\":48}", JsonObject::class.java),
                    true,
                    "4vKzF7L8TWPwtnipqZZo4UGSbpjQi5mX4jkX29Rr/Cc306fe2kBLzhNm820UBQ1+",
                    CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true),
                    generatePayload()
                ),
                Arguments.of(
                    gson.fromJson("{\"name\":\"joe\",\"age\":48}", JsonObject::class.java),
                    true,
                    "Pjwel1wltV2CipfQrQWulh526ZdMr49BpF0Z8A5+Vms=",
                    CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = false),
                    generatePayload()
                ),
                Arguments.of(
                    gson.fromJson("{\"name\":\"joe\",\"age\":48}", JsonObject::class.java),
                    true,
                    "UE5FRAFBQ1JIEDFd1QNYJ9lygFSiK54LohgnsVgmb1YhPS5R5px8KvtUe/NbnYBjqGnslADAgNbOtQ==",
                    CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true),
                    generatePayload()
                ),
                Arguments.of(
                    gson.fromJson("[{\"name\":\"joe\",\"age\":48}]", JsonArray::class.java),
                    true,
                    "UE5FRAFBQ1JIEIKCMD5/p4GpVKFOe+OR1HhF2VFIGxHxOUixt/eAd7DBAeWYfCoiS4VBAcqVyC3MuQ==",
                    CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true),
                    generatePayload()
                ),
            )
        }

        @JvmStatic
        fun unencryptedMessage(): List<Arguments> {
            return listOf(
                Arguments.of("unencryptedMessage"),
                Arguments.of("this is unencrypted message"),
                Arguments.of("this is not encrypted message")
            )
        }
    }

    private lateinit var objectUnderTest: JsonElement

    @ParameterizedTest
    @MethodSource("unencryptedMessage")
    fun `when history message is not encrypted string and crypto is configured should log error and return error in response and return unencrypted message `(
        unencryptedMessage: String
    ) {
        // given
        objectUnderTest = JsonPrimitive(unencryptedMessage)
        val cipherKey = "enigma"
        val cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true)
        val mapper = MapperManager()

        // when
        val (jsonElement, errorMessage) = objectUnderTest.tryDecryptMessage(cryptoModule, mapper)

        // then
        assertEquals(objectUnderTest, jsonElement)
        assertEquals("Crypto is configured but message is not encrypted.", errorMessage?.message)
    }

    @ParameterizedTest
    @MethodSource("cryptoModuleConfiguration")
    fun `when history message is encrypted string and crypto is configured should return decrypted message`(
        message: JsonElement,
        isJson: Boolean,
        encryptedMessage: String,
        cryptoModule: CryptoModule
    ) {
        // given
        objectUnderTest = JsonPrimitive(encryptedMessage)
        val mapper = MapperManager()

        // when
        val (jsonElement, errorMessage) = objectUnderTest.tryDecryptMessage(cryptoModule, mapper)

        // then
        if (isJson) {
            assertEquals(message, jsonElement)
        } else {
            assertEquals(message, jsonElement)
        }
    }

    @ParameterizedTest
    @MethodSource("cryptoModuleConfiguration")
    fun `when history message is not encrypted JSON object and crypto is configured should log error and return error in response and return unencrypted message `(
        message: JsonElement,
        isJson: Boolean,
        encryptedMessage: String,
        cryptoModule: CryptoModule
    ) {
        // given
        objectUnderTest = message
        val mapper = MapperManager()

        // when
        val (jsonElement, errorMessage) = objectUnderTest.tryDecryptMessage(cryptoModule, mapper)

        // then
        assertEquals(objectUnderTest, jsonElement)
        assertEquals("Crypto is configured but message is not encrypted.", errorMessage?.message)
    }

    @ParameterizedTest
    @MethodSource("cryptoModuleConfiguration")
    fun `when history message contains JSON object with pn_other property and crypto is configured should decrypt content of pn_other`(
        message: JsonElement,
        isJson: Boolean,
        encryptedMessage: String,
        cryptoModule: CryptoModule
    ) {
        // given
        val message = generateMessageWithPNOther(JsonPrimitive(encryptedMessage))
        objectUnderTest = message
        val mapper = MapperManager()

        // when
        val (jsonElement, errorMessage) = objectUnderTest.tryDecryptMessage(cryptoModule, mapper)

        // then
        assertEquals(message, jsonElement)
    }

    private fun generateMessageWithPNOther(pnOtherEncryptedValue: JsonElement): JsonObject {
        return JsonObject().apply {
            add("pn_other", pnOtherEncryptedValue)
        }
    }
}

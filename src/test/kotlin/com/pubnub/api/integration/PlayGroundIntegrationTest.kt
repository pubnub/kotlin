package com.pubnub.api.integration

import com.pubnub.api.decomposeAndVerifySignature
import com.pubnub.api.encodedParam
import com.pubnub.api.getSpecialCharsMap
import com.pubnub.api.listen
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Stream


class PlayGroundIntegrationTest : BaseIntegrationTest() {

    @Test
    fun testSpecialCharsPathAndUrl() {
        val expectedChannel = randomValue()
        val keyName = "special_char"
        val value = getSpecialCharsMap().map { it.regular }.shuffled().joinToString("")

        val success = AtomicBoolean()

        server.publish().apply {
            channel = expectedChannel
            message = value
            shouldStore = true
            meta = mapOf(
                keyName to value
            )
            queryParam = mapOf(
                "za" to value,
                "aa" to value,
                "s" to value,
                "Zz" to value,
                "ZZZ" to value,
                "123" to value
            )
        }.async { _, status ->
            assertFalse(status.error)
            decomposeAndVerifySignature(server.configuration, status.clientRequest!!)
            success.set(true)
        }

        success.listen()

        Awaitility.await()
            .pollInterval(Durations.FIVE_HUNDRED_MILLISECONDS)
            .atMost(Durations.FIVE_SECONDS)
            .until {
                val (messages, _, _) = server.history().apply {
                    channel = expectedChannel
                    includeMeta = true
                }.sync()!!

                assertEquals(1, messages.size)
                assertEquals(value, messages[0].meta!!.asJsonObject[keyName].asString)
                assertEquals(value, messages[0].entry.asString)
                true
            }

    }

    @DisplayName("EncodingTestSuite")
    @ParameterizedTest(name = "testWith \"{0}\" {1}")
    @MethodSource("provideSpecialCharsStream")
    fun testVerifySignature(name: String, regular: String, encoded: String) {
        println("testVerifySignature: $name $regular $encoded")

        val success = AtomicBoolean()

        server.configuration.includeRequestIdentifier = false
        server.configuration.includeInstanceIdentifier = false

        val expectedChannel = randomValue()
        val expectedMessage = "msg${regular}msg"
        val expectedMetadata = "meta${regular}meta"

        val propertyName = "target"

        server.publish().apply {
            channel = expectedChannel
            message = expectedMessage
            meta = expectedMetadata
            queryParam = mapOf(propertyName to regular)
            shouldStore = true
            usePost = false
        }.async { result, status ->
            assertFalse(status.error)
            // val encodedParam = status.clientRequest!!.url().encodedQuery()!!.encodedParam(propertyName)
            val encodedParam = status.encodedParam(propertyName)
            println("Checkking $name $encoded $encodedParam")
            assertEquals(encoded, encodedParam)
            decomposeAndVerifySignature(server.configuration, status.clientRequest!!)
            success.set(true)
        }

        success.listen()

        val historyTest = {
            Awaitility.await()
                .pollInterval(Durations.FIVE_HUNDRED_MILLISECONDS)
                .atMost(Durations.FIVE_SECONDS)
                .until {
                    val latch = CountDownLatch(1)
                    server.history().apply {
                        channel = expectedChannel
                        includeMeta = true
                    }.async { result, status ->
                        assertEquals(1, result!!.messages.size)
                        decomposeAndVerifySignature(server.configuration, status.clientRequest!!)
                        assertFalse(status.error)
                        assertEquals(
                            expectedMessage,
                            result.messages[0].entry.asString
                        )
                        assertEquals(
                            expectedMetadata,
                            result.messages[0].meta!!.asString
                        )
                        assertEquals(
                            regular,
                            result.messages[0].entry.asString.replace("msg", "")
                        )
                        assertEquals(
                            regular,
                            result.messages[0].meta!!.asString.replace("meta", "")
                        )
                        latch.countDown()
                    }
                    latch.await(1, TimeUnit.SECONDS)
                }
        }

        historyTest.invoke()
    }


    companion object {

        @JvmStatic
        fun provideSpecialCharsStream(): Stream<Arguments> {
            return getSpecialCharsMap()
                .map { Arguments.of(it.name, it.regular, it.encoded) }
                .toList()
                .shuffled()
                .stream()
        }
    }

    override fun provideAuthKey() = ""

}
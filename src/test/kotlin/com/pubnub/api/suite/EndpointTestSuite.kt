package com.pubnub.api.suite

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.PNStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

typealias MoreChecks = (status: PNStatus) -> Unit

abstract class EndpointTestSuite<T : Endpoint<*, R>, R> : BaseTest() {

    private lateinit var expectedStub: StubMapping

    abstract fun telemetryParamName(): String
    abstract fun pnOperation(): PNOperationType
    abstract fun requiredKeys(): Int
    abstract fun snippet(): T
    abstract fun verifyResultExpectations(result: R)
    abstract fun successfulResponseBody(): String
    abstract fun unsuccessfulResponseBodyList(): List<String>
    abstract fun mappingBuilder(): MappingBuilder
    abstract fun affectedChannelsAndGroups(): Pair<List<String>, List<String>>

    open fun optionalScenarioList(): List<OptionalScenario> = emptyList()

    override fun onBefore() {
        super.onBefore()
        expectedStub = stubFor(mappingBuilder().willReturn(aResponse().withBody(successfulResponseBody())))
    }

    @Test
    fun testTelemetryParameter() {
        val success = AtomicBoolean()

        stubTimeEndpoint()

        lateinit var telemetryParamName: String

        snippet().eval { _, status ->
            assertFalse(status.error)
            assertEquals(pnOperation(), status.operation)
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals(telemetryParamName(), telemetryParamName)
        }

        pubnub.time().async { _, status ->
            assertFalse(status.error)
            assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testSuccessAsync() {
        snippet().eval { result, status ->
            assertFalse(status.error)
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            assertEquals(pnOperation(), status.operation)
            assertEquals(status.affectedChannels, affectedChannelsAndGroups().first)
            assertEquals(status.affectedChannelGroups, affectedChannelsAndGroups().second)
            verifyResultExpectations(result!!)
        }
    }

    @Test
    fun testSuccessSync() {
        runSync()
    }

    @Test
    fun testKeys() {
        testSubscribeKey()
        testPublishKey()
        testAuthKeySync()
        testAuthKeyAsync()
    }

    @Test
    fun testWrongResponsesSync() {
        wireMockServer.removeStub(expectedStub)

        unsuccessfulResponseBodyList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(aResponse().withBody(it)))
            try {
                testSuccessSync()
                failTest()
            } catch (e: Exception) {
                // assertPnException(PubNubError.PARSING_ERROR, e)
            }
            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testWrongResponsesAsync() {
        wireMockServer.removeStub(expectedStub)

        unsuccessfulResponseBodyList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(aResponse().withBody(it)))
            snippet().eval { _, status ->
                assertTrue(status.error)
                assertPnException(PubNubError.PARSING_ERROR, status)
            }
            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testUsualWrongResponsesSync() {
        wireMockServer.removeStub(expectedStub)

        val list = listOf(emptyJson(), noContent(), aResponse().withBody("{}"))

        list.forEach {
            val stub = stubFor(mappingBuilder().willReturn(it))
            try {
                testSuccessSync()
                failTest()
            } catch (e: Exception) {
                assertPnException(PubNubError.PARSING_ERROR, e)
            }
            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testOptionalResponsesSync() {
        wireMockServer.removeStub(expectedStub)

        optionalScenarioList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(it.build()))

            try {
                snippet().sync()!!
                if (it.result == Result.FAIL) {
                    failTest()
                }
            } catch (e: Exception) {
                if (it.result == Result.FAIL) {
                    it.pnError?.let { pubNubError ->
                        assertPnException(pubNubError, e)
                    }
                }
            }

            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testOptionalResponsesAsync() {
        wireMockServer.removeStub(expectedStub)

        optionalScenarioList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(it.build()))

            snippet().eval { result, status ->
                it.additionalChecks.invoke(status)
                if (it.result == Result.SUCCESS) {
                    assertFalse(status.error)
                    result!!
                } else if (it.result == Result.FAIL) {
                    assertTrue(status.error)
                    assertNull(result)
                    it.pnError?.let { pubNubError ->
                        assertPnException(pubNubError, status)
                    }
                }
            }

            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testUrlEncoding() {
        snippet().apply {
            queryParam = getSpecialCharsMap().map {
                it.name to it.regular
            }.toMap()
        }.eval { result, status ->
            assertFalse(status.error)
            assertNotNull(result)

            getSpecialCharsMap().shuffled().forEach {
                val encodedParam = status.encodedParam(it.name)
                assertEquals(it.encoded, encodedParam)
            }
        }
    }

    private fun testSubscribeKey() {
        pubnub.configuration.subscribeKey = " "
        try {
            testSuccessSync()
            if (requiredKeys().contains(SUB)) {
                failTest()
            }
        } catch (e: Exception) {
            if (requiredKeys().contains(SUB)) {
                assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
            }
        }
        pubnub.configuration.subscribeKey = "mySubscribeKey"
    }

    private fun testPublishKey() {
        pubnub.configuration.publishKey = " "
        try {
            testSuccessSync()
            if (requiredKeys().contains(PUB)) {
                failTest()
            }
        } catch (e: Exception) {
            if (requiredKeys().contains(PUB)) {
                assertPnException(PubNubError.PUBLISH_KEY_MISSING, e)
            }
        }
        pubnub.configuration.publishKey = "myPublishKey"
    }

    private fun testAuthKeySync() {
        pubnub.configuration.authKey = "someAuthKey"
        testSuccessSync()

        val requests = findAll(anyRequestedFor(mappingBuilder().build().request.urlMatcher))

        if (requiredKeys().contains(AUTH)) {
            assertEquals(pubnub.configuration.authKey, requests.last().queryParameter("auth").firstValue())
        } else {
            requests.forEach {
                assertFalse(it.queryParams.containsKey("auth"))
            }
        }
    }

    private fun testAuthKeyAsync() {
        pubnub.configuration.authKey = "someAuthKey"

        snippet().eval { result, status ->
            assertFalse(status.error)

            if (requiredKeys().contains(AUTH)) {
                assertEquals(pubnub.configuration.authKey, status.param("auth"))
            } else {
                assertNull(status.param("auth"))
            }
        }
    }

    private fun runSync() {
        val result = snippet().sync()!!
        verifyResultExpectations(result)
    }

    private fun stubTimeEndpoint() {
        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )
    }
}

private fun extractKeys(n: Int): List<Int> {
    val keys = mutableListOf<Int>()
    var n = n
    while (n > 0) {
        val power = highestPowerOf2(n);
        keys.add(power)
        n -= power
    }
    return keys
}

private fun highestPowerOf2(n: Int): Int {
    var res = 0
    for (i in n downTo 1) {
        // If i is a power of 2
        if (i and i - 1 == 0) {
            res = i
            break
        }
    }
    return res
}

val SUB = 0b001
val PUB = 0b010
val AUTH = 0b100

private fun Int.contains(sub: Int): Boolean {
    return extractKeys(this).contains(sub)
}

private fun <Input, Output> Endpoint<Input, Output>.eval(pieceOfCode: (result: Output?, status: PNStatus) -> Unit) {
    val success = AtomicBoolean()
    async { result, status ->
        pieceOfCode.invoke(result, status)
        success.set(true)
    }
    success.listen()
}

class OptionalScenario {
    var responseBuilder: ResponseDefinitionBuilder.() -> ResponseDefinitionBuilder = { this }

    var additionalChecks: MoreChecks = {}
    var result: Result = Result.SUCCESS
    var pnError: PubNubError? = null

    internal fun build(): ResponseDefinitionBuilder {
        return aResponse().responseBuilder()
    }
}

enum class Result {
    SUCCESS,
    FAIL
}

class SpecialChar(
    val name: String,
    val regular: String,
    val encoded: String
)
package com.pubnub.internal.suite

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.noContent
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import com.pubnub.api.PubNubError
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.CoreEndpoint
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.CommonUtils.emptyJson
import com.pubnub.test.CommonUtils.failTest
import com.pubnub.test.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

typealias AsyncCheck<T> = (result: com.pubnub.api.v2.callbacks.Result<T>) -> Unit

abstract class CoreEndpointTestSuite<T : CoreEndpoint<*, R>, R> : BaseTest() {
    private lateinit var expectedStub: StubMapping

    abstract fun pnOperation(): PNOperationType

    abstract fun requiredKeys(): Int

    abstract fun snippet(): T

    abstract fun verifyResultExpectations(result: R)

    abstract fun successfulResponseBody(): String

    abstract fun unsuccessfulResponseBodyList(): List<String>

    abstract fun mappingBuilder(): MappingBuilder

    abstract fun affectedChannelsAndGroups(): Pair<List<String>, List<String>>

    open fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<R>> = emptyList()

    open fun voidResponse() = false

    override fun onBefore() {
        super.onBefore()
        expectedStub = stubFor(mappingBuilder().willReturn(aResponse().withBody(successfulResponseBody())))
        pubnub.configuration.includeInstanceIdentifier = false
        pubnub.configuration.includeRequestIdentifier = false
    }

    @Test
    fun testSuccessAsync() {
        snippet().await { result ->
            assertFalse(result.isFailure)
            verifyResultExpectations(result.getOrThrow())
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
//        testAuthKeyAsync() // TODO
        testSecretKey()
    }

    @Test
    fun testUnsuccessfulResponsesSync() {
        wireMockServer.removeStub(expectedStub)

        unsuccessfulResponseBodyList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(aResponse().withBody(it)))
            try {
                testSuccessSync()
                failTest()
            } catch (e: Exception) {
                e.printStackTrace()
                assertPnException(PubNubError.PARSING_ERROR, e)
            }
            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testUnsuccessfulResponsesAsync() {
        wireMockServer.removeStub(expectedStub)

        unsuccessfulResponseBodyList().forEach {
            val stub = stubFor(mappingBuilder().willReturn(aResponse().withBody(it)))
            snippet().await { result ->
                assertTrue(result.isFailure)
                assertPnException(PubNubError.PARSING_ERROR, result.exceptionOrNull())
            }
            wireMockServer.removeStub(stub)
        }
    }

    @Test
    fun testUsualWrongResponses() {
        wireMockServer.removeStub(expectedStub)

        val map =
            hashMapOf(
                "empty_json" to emptyJson(),
                "no_content" to noContent(),
                "malformed" to aResponse().withBody("{"),
            )

        if (pnOperation() == PNOperationType.PNSubscribeOperation) {
            map.remove("empty_json")
        }

        map.forEach {
            val stub = stubFor(mappingBuilder().willReturn(it.value))
            try {
                val result = snippet().sync()
                if (voidResponse()) {
                    assertNotNull(result)
                } else {
                    failTest(it.key)
                }
            } catch (e: Exception) {
                if (voidResponse()) {
                    failTest(it.key)
                }
                assertPnException(PubNubError.PARSING_ERROR, e)
            }

            snippet().await { result ->
                if (!voidResponse()) {
                    assertTrue(result.isFailure)
                } else {
                    assertFalse(result.isFailure)
                }

                if (!voidResponse()) {
                    assertPnException(PubNubError.PARSING_ERROR, result.exceptionOrNull())
                }
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
                snippet().sync()
                if (it.result == com.pubnub.internal.suite.Result.FAIL) {
                    failTest()
                }
            } catch (e: Exception) {
                if (it.result == com.pubnub.internal.suite.Result.FAIL) {
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

            snippet().await { result ->
                it.additionalChecks.invoke(result)
                if (it.result == com.pubnub.internal.suite.Result.SUCCESS) {
                    assertFalse(result.isFailure)
                } else if (it.result == com.pubnub.internal.suite.Result.FAIL) {
                    assertTrue(result.isFailure)
                    it.pnError?.let { pubNubError ->
                        assertPnException(pubNubError, result.exceptionOrNull())
                    }
                }
            }

            wireMockServer.removeStub(stub)
        }
    }

//    @Test // TODO can't test url encoding this way
//    fun testUrlEncoding() {
//        snippet().apply {
//            queryParam += getSpecialCharsMap().map {
//                it.name to it.regular
//            }.toMap()
//        }.await { result ->
//            assertFalse(result.isFailure)
//
//            getSpecialCharsMap().shuffled().forEach {
//                val encodedParam = status.encodedParam(it.name)
//                assertEquals(it.encoded, encodedParam)
//            }
//        }
//    }

    private fun testSubscribeKey() {
        pubnub.configuration.subscribeKey = " "
        try {
            testSuccessSync()
            if (requiredKeys().contains(com.pubnub.internal.suite.SUB)) {
                failTest()
            }
        } catch (e: Exception) {
            if (requiredKeys().contains(com.pubnub.internal.suite.SUB)) {
                assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
            }
        }
        pubnub.configuration.subscribeKey = "mySubscribeKey"
    }

    private fun testPublishKey() {
        pubnub.configuration.publishKey = " "
        try {
            testSuccessSync()
            if (requiredKeys().contains(com.pubnub.internal.suite.PUB)) {
                failTest()
            }
        } catch (e: Exception) {
            if (requiredKeys().contains(com.pubnub.internal.suite.PUB)) {
                assertPnException(PubNubError.PUBLISH_KEY_MISSING, e)
            }
        }
        pubnub.configuration.publishKey = "myPublishKey"
    }

    private fun testAuthKeySync() {
        pubnub.configuration.authKey = "someAuthKey"
        testSuccessSync()

        val requests = findAll(anyRequestedFor(mappingBuilder().build().request.urlMatcher))

        if (requiredKeys().contains(com.pubnub.internal.suite.AUTH)) {
            assertEquals(pubnub.configuration.authKey, requests.last().queryParameter("auth").firstValue())
        } else {
            requests.forEach {
                assertFalse(it.queryParams.containsKey("auth"))
            }
        }
    }

//    private fun testAuthKeyAsync() { // TODO can't look at params this way
//        pubnub.configuration.authKey = "someAuthKey"
//
//        snippet().await { result ->
//            assertFalse(result.isFailure)
//
//            if (requiredKeys().contains(AUTH)) {
//                assertEquals(pubnub.configuration.authKey, status.param("auth"))
//            } else {
//                assertNull(status.param("auth"))
//            }
//        }
//    }

    private fun testSecretKey() {
        pubnub.configuration.secretKey = " "
        try {
            testSuccessSync()
            if (requiredKeys().contains(com.pubnub.internal.suite.SEC)) {
                failTest()
            }
        } catch (e: Exception) {
            if (requiredKeys().contains(com.pubnub.internal.suite.SEC)) {
                assertPnException(PubNubError.SECRET_KEY_MISSING, e)
            }
        }
        pubnub.configuration.secretKey = "mySecretKey"
    }

    private fun runSync() {
        val result = snippet().sync()
        verifyResultExpectations(result)
    }

    private fun stubTimeEndpoint() {
        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(
                    aResponse()
                        .withBody("[1000]"),
                ),
        )
    }
}

private fun extractKeys(value: Int): List<Int> {
    val keys = mutableListOf<Int>()
    var n = value
    while (n > 0) {
        val power =
            {
                var res = 0
                for (i in n downTo 1) {
                    if (i and i - 1 == 0) {
                        res = i
                        break
                    }
                }
                res
            }.invoke()
        keys.add(power)
        n -= power
    }
    return keys
}

val SUB = 0b001
val PUB = 0b010
val AUTH = 0b100
val SEC = 0b1000

private fun Int.contains(sub: Int): Boolean {
    return com.pubnub.internal.suite.extractKeys(this).contains(sub)
}

class OptionalScenario<R> {
    var responseBuilder: ResponseDefinitionBuilder.() -> ResponseDefinitionBuilder = { this }

    var additionalChecks: com.pubnub.internal.suite.AsyncCheck<R> = { result: com.pubnub.api.v2.callbacks.Result<R> -> }
    var result: com.pubnub.internal.suite.Result = com.pubnub.internal.suite.Result.SUCCESS
    var pnError: PubNubError? = null

    internal fun build(): ResponseDefinitionBuilder {
        return aResponse().responseBuilder()
    }
}

enum class Result {
    SUCCESS,
    FAIL,
}

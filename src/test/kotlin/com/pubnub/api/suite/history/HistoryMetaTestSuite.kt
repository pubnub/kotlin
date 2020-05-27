package com.pubnub.api.suite.history

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.PubNubError
import com.pubnub.api.endpoints.History
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.suite.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class HistoryMetaTestSuite : EndpointTestSuite<History, PNHistoryResult>() {

    override fun telemetryParamName() = "l_hist"

    override fun pnOperation() = PNOperationType.PNHistoryOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): History {
        return pubnub.history().apply {
            channel = "foo"
            includeMeta = true
            includeTimetoken = true
        }
    }

    override fun verifyResultExpectations(result: PNHistoryResult) {
        assertEquals(100, result.startTimetoken)
        assertEquals(200, result.endTimetoken)
        assertEquals(2, result.messages.size)
        assertEquals("msg1", result.messages[0].entry.asString)
        assertEquals("msg2", result.messages[1].entry.asString)
        assertTrue(result.messages[0].meta!!.asString.isBlank())
        assertEquals(
            mapOf("color" to "red"), Gson().fromJson(
                result.messages[1].meta!!.asJsonObject,
                object : TypeToken<Map<String, String>?>() {}.type
            )
        )
        assertEquals(100, result.messages[0].timetoken)
        assertEquals(200, result.messages[1].timetoken)
    }

    override fun successfulResponseBody() = """
        [
         [
          {
           "message": "msg1",
           "timetoken": 100,
           "meta": ""
          },
          {
           "message": "msg2",
           "timetoken": 200,
           "meta": {
            "color": "red"
           }
          }
         ],
         100,
         200
        ]
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = listOf(
        "[]",
        "[{}]"
    )

    override fun mappingBuilder(): MappingBuilder {
        return get(
            urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/foo")
        )
            .withQueryParam("include_token", equalTo("true"))
            .withQueryParam("count", equalTo("100"))
            .withQueryParam("include_meta", equalTo("true"))
            .withQueryParam("reverse", equalTo("false"))
    }

    override fun affectedChannelsAndGroups() = listOf("foo") to emptyList<String>()

    override fun optionalScenarioList() = listOf(
        OptionalScenario().apply {
            responseBuilder = {
                withBody("""["First Element Not An Array",0,0]""")
            }
            additionalChecks = { status ->
                assertTrue(status.error)
                assertEquals(status.exception!!.errorMessage, "History is disabled")
            }
            result = Result.FAIL
            pnError = PubNubError.HTTP_ERROR
        }
    )
}
package com.pubnub.api.suite.history.v3

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.OptionalScenario
import com.pubnub.api.suite.Result
import com.pubnub.api.suite.SUB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class FetchMessagesMetaActionsTestSuite : EndpointTestSuite<FetchMessages, PNFetchMessagesResult>() {

    override fun telemetryParamName() = "l_hist"

    override fun pnOperation() = PNOperationType.PNFetchMessagesOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): FetchMessages =
        pubnub.fetchMessages(
            channels = listOf("ch1"),
            includeMeta = true,
            includeMessageActions = true
        )

    override fun verifyResultExpectations(result: PNFetchMessagesResult) {
        assertEquals(1, result.channels.size)
        assertTrue(result.channels.containsKey("ch1"))
        assertEquals("bar", result.channels["ch1"]!![0].message.asString)
        assertEquals(
            JsonObject().apply { addProperty("color", "red") },
            result.channels["ch1"]!![0].meta
        )
        assertEquals(100, result.channels["ch1"]!![0].timetoken)

        val actions = result.channels["ch1"]!![0].actions
        assertEquals(1, actions!!.keys.size)
        assertEquals(1, actions["reaction"]!!["smile"]!!.size)
        assertEquals("publishersUuid", actions["reaction"]!!["smile"]!![0].uuid)
        assertEquals("200", actions["reaction"]!!["smile"]!![0].actionTimetoken)
    }

    override fun successfulResponseBody() = """
        {
         "channels": {
          "ch1": [
           {
            "message": "bar",
            "meta": {
             "color": "red"
            },
            "timetoken": 100,
            "actions": {
             "reaction": {
              "smile": [
               {
                "uuid": "publishersUuid",
                "actionTimetoken": "200"
               }
              ]
             }
            }
           }
          ]
         }
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = listOf(
        """
            {
             "channels": {
              "ch1": [
               {
                "timetoken": 100
               }
              ]
             }
            }
        """.trimIndent(),
        """
            {
             "channels": {
              "ch1": [
               {
                "entry": "hello",
                "timetoken": 100
               }
              ],
              "ch2": [],
              "ch3": null
             }
            }
        """.trimIndent()
    )

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v3/history-with-actions/sub-key/mySubscribeKey/channel/ch1"))
            .withQueryParam("max", equalTo("25"))
            .withQueryParam("include_meta", equalTo("true"))
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList(): List<OptionalScenario<PNFetchMessagesResult>> {
        return listOf(
            OptionalScenario<PNFetchMessagesResult>().apply {
                responseBuilder = { withBody("""{"channels":{"ch3":null}}""") }
                result = Result.FAIL
                pnError = PubNubError.PARSING_ERROR
                additionalChecks = { status: PNStatus, result: PNFetchMessagesResult? ->
                    assertTrue(status.error)
                    assertNull(result)
                    assertEquals(PNStatusCategory.PNMalformedResponseCategory, status.category)
                }
            }
        )
    }
}

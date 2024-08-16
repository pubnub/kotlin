package com.pubnub.internal.suite.history.v3

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class FetchMessagesTestSuite : com.pubnub.internal.suite.CoreEndpointTestSuite<FetchMessages, PNFetchMessagesResult>() {
    override fun pnOperation() = PNOperationType.PNFetchMessagesOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): FetchMessages =
        pubnub.fetchMessages(
            channels = listOf("ch1"),
        )

    override fun verifyResultExpectations(result: PNFetchMessagesResult) {
        assertEquals(1, result.channels.size)
        assertTrue(result.channels.containsKey("ch1"))
        assertEquals("hello", result.channels["ch1"]!![0].message.asString)
        assertEquals(100L, result.channels["ch1"]!![0].timetoken)
        assertNull(result.channels["ch1"]!![0].meta)
    }

    override fun successfulResponseBody() =
        """
        {
         "channels": {
          "ch1": [
           {
            "message": "hello",
            "timetoken": 100
           }
          ]
         }
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() =
        listOf(
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
            """.trimIndent(),
        )

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/ch1"))
            .withQueryParam("max", equalTo("100"))
            .withQueryParam("include_meta", absent())
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNFetchMessagesResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNFetchMessagesResult>().apply {
                responseBuilder = { withBody("""{"channels":{"ch3":null}}""") }
                result = com.pubnub.internal.suite.Result.FAIL
                pnError = PubNubError.PARSING_ERROR
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
//                    assertEquals(PNStatusCategory.PNMalformedResponseCategory, status.category)
                }
            },
        )
    }
}

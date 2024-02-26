package com.pubnub.internal.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class AllChannelsChannelGroupTestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<AllChannelsChannelGroup, PNChannelGroupsAllChannelsResult>() {
    override fun pnOperation() = PNOperationType.PNChannelsForGroupOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): AllChannelsChannelGroup {
        return pubnub.listChannelsForChannelGroup(
            channelGroup = "cg1",
        )
    }

    override fun verifyResultExpectations(result: PNChannelGroupsAllChannelsResult) {
        assertEquals(listOf("ch1", "ch2"), result.channels)
    }

    override fun successfulResponseBody() =
        """
        {
         "status": 200,
         "payload": {
          "channels": [
           "ch1",
           "ch2"
          ],
          "group": "cg1"
         },
         "service": "channel-registry",
         "error": false
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() =
        listOf(
            """{"payload":{"channels":null,"group":null}}""",
            """{"payload":{"channels":null}}""",
            """{"payload":{}}""",
            """{"payload":null}""",
        )

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/cg1"))
    }

    override fun affectedChannelsAndGroups() = emptyList<String>() to listOf("cg1")

    override fun optionalScenarioList() =
        listOf(
            com.pubnub.internal.suite.OptionalScenario<PNChannelGroupsAllChannelsResult>().apply {
                responseBuilder = {
                    withBody(
                        """
                        {
                         "payload": {
                          "channels": [],
                          "group": "cg1"
                         }
                        }
                        """.trimIndent(),
                    )
                }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertTrue(result.getOrThrow().channels.isEmpty())
                }
            },
            com.pubnub.internal.suite.OptionalScenario<PNChannelGroupsAllChannelsResult>().apply {
                responseBuilder = {
                    withBody(
                        """
                        {
                         "payload": {
                          "group": "cg1"
                         }
                        }
                        """.trimIndent(),
                    )
                }
                pnError = PubNubError.PARSING_ERROR
                result = com.pubnub.internal.suite.Result.FAIL
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
                }
            },
        )
}

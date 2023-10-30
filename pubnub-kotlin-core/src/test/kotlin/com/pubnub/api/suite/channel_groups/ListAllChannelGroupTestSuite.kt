package com.pubnub.api.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.OptionalScenario
import com.pubnub.api.suite.Result
import com.pubnub.api.suite.SUB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class ListAllChannelGroupTestSuite : EndpointTestSuite<ListAllChannelGroup, PNChannelGroupsListAllResult>() {

    override fun telemetryParamName() = "l_cg"

    override fun pnOperation() = PNOperationType.PNChannelGroupsOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): ListAllChannelGroup {
        return pubnub.listAllChannelGroups()
    }

    override fun verifyResultExpectations(result: PNChannelGroupsListAllResult) {
        assertEquals(listOf("cg1", "cg2"), result.groups)
    }

    override fun successfulResponseBody() = """{"payload":{"groups":["cg1","cg2"]}}"""

    override fun unsuccessfulResponseBodyList() = listOf(
        """{"payload":{"groups":null}}""",
        """{"payload":{"groups":{}}}""",
        """{"payload":{}}""",
        """{"payload":null}"""
    )

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
    }

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun optionalScenarioList(): List<OptionalScenario<PNChannelGroupsListAllResult>> {
        return listOf(
            OptionalScenario<PNChannelGroupsListAllResult>().apply {
                responseBuilder = {
                    withBody("""{"payload":{"groups":[]}}""")
                }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertTrue(result.getOrThrow().groups.isEmpty())
                }
                result = Result.SUCCESS
            }
        )
    }
}

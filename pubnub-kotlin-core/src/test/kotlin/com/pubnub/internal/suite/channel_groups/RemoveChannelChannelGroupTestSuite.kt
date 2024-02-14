package com.pubnub.internal.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.internal.suite.AUTH
import com.pubnub.internal.suite.EndpointTestSuite
import com.pubnub.internal.suite.OptionalScenario
import com.pubnub.internal.suite.Result
import com.pubnub.internal.suite.SUB
import org.junit.Assert.assertTrue

class RemoveChannelChannelGroupTestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<RemoveChannelChannelGroup, PNChannelGroupsRemoveChannelResult>() {

    override fun telemetryParamName() = "l_cg"

    override fun pnOperation() = PNOperationType.PNRemoveChannelsFromGroupOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): RemoveChannelChannelGroup {
        return pubnub.removeChannelsFromChannelGroup(
            channelGroup = "cg1",
            channels = listOf("ch1", "ch2")
        )
    }

    override fun verifyResultExpectations(result: PNChannelGroupsRemoveChannelResult) {}

    override fun successfulResponseBody() = """{"payload":{"groups":["cg1","cg2"]}}"""

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/cg1"))
            .withQueryParam("remove", equalTo("ch1,ch2"))
    }

    override fun affectedChannelsAndGroups() = listOf("ch1", "ch2") to listOf("cg1")

    override fun voidResponse() = true

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNChannelGroupsRemoveChannelResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNChannelGroupsRemoveChannelResult>().apply {
                responseBuilder = { withBody("").withStatus(400) }
                result = com.pubnub.internal.suite.Result.FAIL
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
                }
            }
        )
    }
}

package com.pubnub.api.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.suite.*
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class RemoveChannelChannelGroupTestSuite :
    EndpointTestSuite<RemoveChannelChannelGroup, PNChannelGroupsRemoveChannelResult>() {

    override fun telemetryParamName() = "l_cg"

    override fun pnOperation() = PNOperationType.PNRemoveChannelsFromGroupOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): RemoveChannelChannelGroup {
        return pubnub.removeChannelsFromChannelGroup().apply {
            channelGroup = "cg1"
            channels = listOf("ch1", "ch2")
        }
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

    override fun optionalScenarioList(): List<OptionalScenario<PNChannelGroupsRemoveChannelResult>> {
        return listOf(
            OptionalScenario<PNChannelGroupsRemoveChannelResult>().apply {
                responseBuilder = { withBody("").withStatus(400) }
                result = Result.FAIL
                additionalChecks = { pnStatus: PNStatus, result: PNChannelGroupsRemoveChannelResult? ->
                    assertTrue(pnStatus.error)
                    assertNull(result)
                }
            }
        )
    }

}
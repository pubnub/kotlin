package com.pubnub.internal.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroupEndpoint

class AddChannelChannelGroupTestSuite :
    com.pubnub.internal.suite.CoreEndpointTestSuite<AddChannelChannelGroupEndpoint, PNChannelGroupsAddChannelResult>() {
    override fun pnOperation() = PNOperationType.PNAddChannelsToGroupOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): AddChannelChannelGroupEndpoint {
        return pubnub.addChannelsToChannelGroup(
            channelGroup = "cg1",
            channels = listOf("ch1", "ch2"),
        )
    }

    override fun verifyResultExpectations(result: PNChannelGroupsAddChannelResult) {
    }

    override fun successfulResponseBody() =
        """
        {
         "status": 200,
         "message": "OK",
         "service": "channel-registry",
         "error": false
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/cg1"))
            .withQueryParam("add", equalTo("ch1,ch2"))
    }

    override fun affectedChannelsAndGroups() = listOf("ch1", "ch2") to listOf("cg1")

    override fun voidResponse() = true
}

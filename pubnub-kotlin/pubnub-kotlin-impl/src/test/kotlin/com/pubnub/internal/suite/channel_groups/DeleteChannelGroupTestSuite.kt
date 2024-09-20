package com.pubnub.internal.suite.channel_groups

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

class DeleteChannelGroupTestSuite :
    com.pubnub.internal.suite.CoreEndpointTestSuite<DeleteChannelGroup, PNChannelGroupsDeleteGroupResult>() {
    override fun pnOperation() = PNOperationType.PNRemoveGroupOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): DeleteChannelGroup {
        return pubnub.deleteChannelGroup(
            channelGroup = "cg1",
        )
    }

    override fun verifyResultExpectations(result: PNChannelGroupsDeleteGroupResult) {
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
        return get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/cg1/remove"))
    }

    override fun affectedChannelsAndGroups() = emptyList<String>() to listOf("cg1")

    override fun voidResponse() = true
}

package com.pubnub.internal.suite.push.add

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.internal.endpoints.push.AddChannelsToPush

class AddChannelsToPushV2TestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<AddChannelsToPush, PNPushAddChannelResult>() {

    override fun telemetryParamName() = "l_push"

    override fun pnOperation() = PNOperationType.PNAddPushNotificationsOnChannelsOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): AddChannelsToPush {
        return pubnub.addPushNotificationsOnChannels(
            pushType = PNPushType.APNS2,
            channels = listOf("ch1", "ch2"),
            deviceId = "12345",
            topic = "news"
        )
    }

    override fun verifyResultExpectations(result: PNPushAddChannelResult) {
    }

    override fun successfulResponseBody(): String {
        return """[1, "Modified Channels"]"""
    }

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/12345"))
            .withQueryParam("type", absent())
            .withQueryParam("add", equalTo("ch1,ch2"))
            .withQueryParam("environment", equalTo("development"))
            .withQueryParam("topic", equalTo("news"))

    override fun affectedChannelsAndGroups() = listOf("ch1", "ch2") to emptyList<String>()

    override fun voidResponse() = true
}

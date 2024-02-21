package com.pubnub.internal.suite.push.remove

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.internal.endpoints.push.RemoveChannelsFromPush

class RemoveChannelsFromPushV1TestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<RemoveChannelsFromPush, PNPushRemoveChannelResult>() {

    override fun telemetryParamName() = "l_push"

    override fun pnOperation() = PNOperationType.PNRemovePushNotificationsFromChannelsOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): RemoveChannelsFromPush {
        return pubnub.removePushNotificationsFromChannels(
            pushType = PNPushType.FCM,
            channels = listOf("ch1", "ch2"),
            deviceId = "12345"
        )
    }

    override fun verifyResultExpectations(result: PNPushRemoveChannelResult) {
    }

    override fun successfulResponseBody(): String {
        return """[1, "Modified Channels"]"""
    }

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/12345"))
            .withQueryParam("type", equalTo("gcm"))
            .withQueryParam("remove", equalTo("ch1,ch2"))
            .withQueryParam("environment", absent())
            .withQueryParam("topic", absent())

    override fun affectedChannelsAndGroups() = listOf("ch1", "ch2") to emptyList<String>()

    override fun voidResponse() = true
}

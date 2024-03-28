package com.pubnub.internal.suite.push.remove

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDeviceEndpoint

class RemoveAllFromPushV1TestSuite :
    com.pubnub.internal.suite.CoreEndpointTestSuite<RemoveAllPushChannelsForDeviceEndpoint, PNPushRemoveAllChannelsResult>() {
    override fun pnOperation() = PNOperationType.PNRemoveAllPushNotificationsOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): RemoveAllPushChannelsForDeviceEndpoint {
        return pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            pushType = PNPushType.FCM,
            deviceId = "12345",
        )
    }

    override fun verifyResultExpectations(result: PNPushRemoveAllChannelsResult) {
    }

    override fun successfulResponseBody(): String {
        return """[1, "Removed Device"]"""
    }

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/12345/remove"))
            .withQueryParam("type", equalTo("gcm"))
            .withQueryParam("environment", absent())
            .withQueryParam("topic", absent())

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun voidResponse() = true
}

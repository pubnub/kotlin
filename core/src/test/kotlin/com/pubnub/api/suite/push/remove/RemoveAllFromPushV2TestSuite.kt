package com.pubnub.api.suite.push.remove

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.SUB

class RemoveAllFromPushV2TestSuite :
    EndpointTestSuite<RemoveAllPushChannelsForDevice, PNPushRemoveAllChannelsResult>() {

    override fun telemetryParamName() = "l_push"

    override fun pnOperation() = PNOperationType.PNRemoveAllPushNotificationsOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): RemoveAllPushChannelsForDevice {
        return pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            pushType = PNPushType.APNS2,
            deviceId = "12345",
            topic = "news"
        )
    }

    override fun verifyResultExpectations(result: PNPushRemoveAllChannelsResult) {
    }

    override fun successfulResponseBody(): String {
        return """[1, "Removed Device"]"""
    }

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/12345/remove"))
            .withQueryParam("type", absent())
            .withQueryParam("environment", equalTo("development"))
            .withQueryParam("topic", equalTo("news"))

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun voidResponse() = true
}

package com.pubnub.internal.suite.push.add

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.internal.endpoints.push.AddChannelsToPush
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class AddChannelsToPushV1TestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<AddChannelsToPush, PNPushAddChannelResult>() {

    override fun pnOperation() = PNOperationType.PNAddPushNotificationsOnChannelsOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): AddChannelsToPush {
        return pubnub.addPushNotificationsOnChannels(
            pushType = PNPushType.FCM,
            channels = listOf("ch1", "ch2"),
            deviceId = "12345"
        )
    }

    override fun verifyResultExpectations(result: PNPushAddChannelResult) {
    }

    override fun successfulResponseBody(): String {
        return """[1, "Modified Channels"]"""
    }

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/12345"))
            .withQueryParam("type", equalTo("gcm"))
            .withQueryParam("add", equalTo("ch1,ch2"))
            .withQueryParam("environment", absent())
            .withQueryParam("topic", absent())

    override fun affectedChannelsAndGroups() = listOf("ch1", "ch2") to emptyList<String>()

    override fun voidResponse() = true

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNPushAddChannelResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNPushAddChannelResult>().apply {
                val body = "{\"error\":\"Use of the mobile push notifications API requires Push Notifications" +
                    " which is not enabled for this subscribe key. Login to your PubNub Dashboard Account" +
                    " and enable Push Notifications. " +
                    "Contact support@pubnub.com if you require further assistance.\"}"
                result = com.pubnub.internal.suite.Result.FAIL
                responseBuilder = { withBody(body).withStatus(400) }
                pnError = PubNubError.HTTP_ERROR
                additionalChecks = { result ->
                    assertTrue(voidResponse())
                    assertEquals(body, (result.exceptionOrNull() as? PubNubException)?.jso)
                }
            }
        )
    }
}
